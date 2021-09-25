import React, { useEffect, useState } from 'react'
import { Input, Select, DatePicker, InputNumber } from 'antd'
import { useHistory, useParams } from 'react-router'
import moment from 'moment-timezone'
import { v4 as uuid } from 'uuid'
import { BsTrashFill } from 'react-icons/bs'

import Spinner from '../../components/common/Spinner'
import useAirplaneProfiles from '../../hooks/useAirplaneProfiles'
import useAirports from '../../hooks/useAirports'
import { useSessionStore } from '../../hooks/Store'
import { FlightService } from '../../api'
import useFlights from '../../hooks/useFlights'

const { Option } = Select

const EditFlight = () => {
  /* ------------------------ Preliminary Data Fetching ----------------------- */

  const { id: flightId } = useParams()
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)
  const history = useHistory()

  const [validUser, setValidUser] = useState(false)

  useEffect(() => {
    if (!token || !user || user.userType !== 'airline') {
      setValidUser(false)
      history.push('/')
    } else {
      setValidUser(true)
    }
  }, [token, user, history])

  /* -------------------------------------------------------------------------- */

  const {
    data: flights,
    isSuccess: flightsSuccess,
    error: flightsError
    // refetch: refetchFlights
  } = useFlights(token)

  const {
    data: airplaneProfiles,
    isError: airplaneProfilesError,
    isSuccess: airplaneProfilesSuccess,
    refetch: refetchAirplaneProfiles
  } = useAirplaneProfiles()
  const {
    data: airports,
    isError: airportsError,
    isSuccess: airportsSuccess,
    refetch: refetchAirports
  } = useAirports()

  useEffect(() => {
    if (!airplaneProfiles) {
      refetchAirplaneProfiles()
    }
    if (!airports) {
      refetchAirports()
    }
  }, [
    airplaneProfiles,
    airports,
    refetchAirplaneProfiles,
    refetchAirports
  ])

  useEffect(() => {
    if (airplaneProfilesError) {
      history.push('/')
    }

    if (airportsError) {
      history.push('/')
    }

    if (flightsError) {
      history.push('/')
    }
  }, [airportsError, airplaneProfilesError, history, flightsError])

  /* -------------------------------------------------------------------------- */
  /*                                 Form State                                 */
  /* -------------------------------------------------------------------------- */

  const [state, setState] = useState(null)

  // Initialise form
  useEffect(() => {
    if (
      flightsSuccess &&
      flights &&
      !state &&
      airportsSuccess &&
      airplaneProfilesSuccess
    ) {
      const flight = flights.find((f) => f.id === flightId)

      // console.log('Retrieved flight:', flight)
      // console.log('Airports:', airports)

      const flightProfile = airplaneProfiles.find(
        (ap) =>
          ap.code === flight.profile.code &&
          ap.name === flight.profile.name
      )

      const flightStopovers = flight.stopOvers
        ? flight.stopOvers.map((stopover) => ({
            arrival: moment(stopover.arrivalLocal),
            departure: moment(stopover.departureLocal),
            location: airports.find(
              (a) => a.code === stopover.location.code
            ).id
          }))
        : []

      const flightOrigin = {
        ...airports.find((a) => a.code === flight.origin.code)
      }
      const flightDestination = {
        ...airports.find((a) => a.code === flight.destination.code)
      }

      const flightState = {
        code: flight.code,
        profile: flightProfile,
        origin: flightOrigin,
        destination: flightDestination,
        departure: moment(flight.departureLocal),
        arrival: moment(flight.arrivalLocal),
        firstClassCost: flight.firstClassCost,
        businessClassCost: flight.businessClassCost,
        economyClassCost: flight.economyClassCost,
        stopOvers: flightStopovers
      }

      // console.log('Initial Flight State:', flightState)

      setState(flightState)
    }
  }, [
    airplaneProfiles,
    airports,
    flightId,
    flights,
    flightsSuccess,
    state
  ])

  const [loading, setLoading] = useState(false)

  const handleSubmit = (e) => {
    e.preventDefault()

    const updatedFlight = {
      code: state.code,
      profile: state.profile.id,
      origin: state.origin.id,
      destination: state.destination.id,
      departure: state.departure.utc().format(),
      arrival: state.arrival.utc().format(),
      firstClassCost: state.firstClassCost,
      businessClassCost: state.businessClassCost,
      economyClassCost: state.economyClassCost,
      stopOvers: state.stopOvers.map((s) => ({
        location: s.location,
        arrival: s.arrival.utc().format(),
        departure: s.departure.utc().format()
      }))
    }

    // console.log('Update to flight:', updatedFlight)

    setLoading(true)

    FlightService.updateFlight({
      data: { token, id: flightId, flight: updatedFlight },
      onSuccess: () => {
        setLoading(false)
        history.push(`/dashboard/view/flights/${flightId}`)
      }
    })

    setLoading(false)
  }

  /* -------------------------------------------------------------------------- */

  const invalidDeparture = (current) => current < moment()
  const invalidArrival = (current) => current <= state.departure

  /* -------------------------------------------------------------------------- */

  const invalidStopoverArrival = (current) => {
    const beforeFlightDeparture = current <= state.departure
    const afterFlightArrival = current >= state.arrival
    // const beforePreviousStopoverDeparture =
    //   index - 1 > 0 && current < state.stopOvers[index].departure
    // const afterNextStopoverArrival =
    //   index + 1 < state.stopOvers.length &&
    //   current > state.stopOvers[index + 1].arrival

    return beforeFlightDeparture || afterFlightArrival
  }

  const invalidStopoverDeparture = (current, index) => {
    const beforeFlightDeparture = current <= state.departure
    const afterFlightArrival = current >= state.arrival
    const beforeCurrentStopoverArrival =
      current <= state.stopOvers[index].arrival

    // const afterNextStopoverArrival =
    //   index + 1 < state.stopOvers.length &&
    //   current > state.stopOvers[index + 1].arrival

    return (
      beforeFlightDeparture ||
      afterFlightArrival ||
      beforeCurrentStopoverArrival
    )
  }

  const addStopover = () => {
    const stopOvers = [...state.stopOvers]
    const lastStopoverIndex = stopOvers.length - 1

    stopOvers.push({
      location: null,
      departure:
        lastStopoverIndex >= 0
          ? stopOvers[lastStopoverIndex].departure
          : state.departure,
      arrival:
        lastStopoverIndex >= 0
          ? stopOvers[lastStopoverIndex].departure
          : state.departure
    })

    setState((oldState) => ({ ...oldState, stopOvers }))
  }

  const removeStopover = (index) => {
    const stopOvers = [...state.stopOvers]
    stopOvers.splice(index, 1)

    setState((oldState) => ({ ...oldState, stopOvers }))
  }

  const handleStopoverSelect = (key, index) => {
    const stopOvers = [...state.stopOvers]
    stopOvers[index].location = key

    setState((oldState) => ({ ...oldState, stopOvers }))
  }

  const handleStopoverArrivalChange = (date, index) => {
    const stopOvers = [...state.stopOvers]
    stopOvers[index].arrival = date
    setState({ ...state, stopOvers })
  }

  const handleStopoverDepartureChange = (date, index) => {
    const stopOvers = [...state.stopOvers]
    stopOvers[index].departure = date
    setState({ ...state, stopOvers })
  }

  const getStopoversWithoutSameLocations = (airportId) => {
    const stopOvers = [...state.stopOvers].map((stopover) => {
      if (stopover.location === airportId) {
        return {
          ...stopover,
          location: undefined
        }
      }
      return stopover
    })

    return stopOvers
  }

  const renderStopovers = () => {
    if (!validUser || !state) return null

    // For each stopover:
    // - Can select an airport that is not the origin or destination
    // - Can select a stopover arrival datetime that is after the flight departure datetime
    // - Can select a stopover departure datetime that is before the flight arrival datetime

    return state.stopOvers.map((stopover, index) => (
      <div
        className='grid items-center grid-cols-12 col-span-12 gap-2'
        key={uuid()}
      >
        <p className='col-span-12 font-semibold'>
          Stopover {index + 1}
        </p>
        <main className='grid items-center grid-cols-12 col-span-11 gap-4'>
          <Select
            className='col-span-12'
            placeholder='Select stopover airport'
            onSelect={(key) => handleStopoverSelect(key, index)}
            value={stopover.location}
          >
            {airports
              .filter(
                (airport) =>
                  airport.id !== state.origin.id &&
                  airport.id !== state.destination.id
              )
              .map(({ id, name, code }) => (
                <Option key={id} value={id}>
                  <p>
                    ({code}) {name}
                  </p>
                </Option>
              ))}
          </Select>
          <p className='col-span-12 sm:col-span-2'>Arrival</p>
          <DatePicker
            className='col-span-7 sm:col-span-6'
            disabledDate={(current) =>
              invalidStopoverArrival(current, index)
            }
            allowClear={false}
            placeholder='Arrival at stopover'
            value={stopover.arrival}
            onChange={(date) =>
              handleStopoverArrivalChange(date, index)
            }
            format='YYYY-MM-DD HH:mm'
            showTime={{
              defaultValue: moment('00:00', 'HH:mm')
            }}
          />
          <Input
            className='col-span-5 sm:col-span-4'
            readOnly
            value={
              stopover.location && airports
                ? moment
                    .tz(
                      airports.find((a) => a.id === stopover.location)
                        .zoneId
                    )
                    .format('Z z')
                : moment.tz().format('Z z')
            }
          />
          <p className='justify-end col-span-12 sm:col-span-2'>
            Departure
          </p>
          <DatePicker
            className='col-span-7 sm:col-span-6'
            disabledDate={(current) =>
              invalidStopoverDeparture(current, index)
            }
            allowClear={false}
            placeholder='Departure at stopover'
            value={stopover.departure}
            onChange={(date) =>
              handleStopoverDepartureChange(date, index)
            }
            format='YYYY-MM-DD HH:mm'
            showTime={{
              defaultValue: moment('00:00', 'HH:mm')
            }}
          />
          <Input
            className='col-span-5 sm:col-span-4'
            readOnly
            value={
              stopover.location && airports
                ? moment
                    .tz(
                      airports.find((a) => a.id === stopover.location)
                        .zoneId
                    )
                    .format('Z z')
                : moment.tz().format('Z z')
            }
          />
        </main>
        <button
          type='button'
          onClick={() => removeStopover(index)}
          className='flex items-center self-stretch justify-center col-span-1 transition-colors bg-gray-600 rounded-lg hover:bg-red-600'
        >
          <BsTrashFill className='w-4 h-4 text-white cursor-pointer' />
        </button>
      </div>
    ))
  }

  return (
    <main
      style={{ maxHeight: '80vh' }}
      className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'
    >
      <section className='flex flex-col w-full h-full max-w-lg gap-4'>
        <h1 className='text-3xl font-bold'>Edit Flight</h1>
        <hr />
        {!validUser || !airplaneProfiles || !airports || !state ? (
          <Spinner size={6} />
        ) : (
          <form
            className='flex flex-col items-start w-full h-full max-h-full gap-4 overflow-y-auto'
            onSubmit={handleSubmit}
          >
            {state && (
              <main className='flex flex-col items-start w-full h-full max-h-full gap-4 overflow-y-auto'>
                <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
                  <p className='col-span-2 font-bold'>Flight Code</p>
                  <Input
                    value={state.code}
                    className='col-span-3'
                    placeholder='Enter flight code'
                    onChange={(e) =>
                      setState((oldState) => ({
                        ...oldState,
                        code: e.target.value
                      }))
                    }
                  />
                </section>

                <div className='grid w-full grid-flow-row gap-2 p-3 bg-gray-50'>
                  <section className='grid items-center w-full grid-cols-5 gap-2 '>
                    <p className='col-span-2 font-bold'>
                      Airplane Profile
                    </p>
                    <Select
                      className='col-span-3'
                      style={{ width: '100%' }}
                      placeholder='Select an airplane profile'
                      value={state.profile.id}
                      onSelect={(key) => {
                        setState((oldState) => ({
                          ...oldState,
                          profile: airplaneProfiles.find(
                            (profile) => profile.id === key
                          )
                        }))
                      }}
                    >
                      {airplaneProfiles &&
                        airplaneProfiles.map(({ id, code }) => (
                          <Option key={id}>
                            <article>
                              <p>{code}</p>
                            </article>
                          </Option>
                        ))}
                    </Select>
                    <span className='col-span-2' />
                    {Object.entries(state.profile).length !== 0 && (
                      <span className='col-span-3'>
                        <p>
                          <span className='font-semibold'>
                            First class seats:{' '}
                          </span>
                          {state.profile.firstClassRows *
                            state.profile.firstClassColumns}
                        </p>
                        <p>
                          <span className='font-semibold'>
                            Business class seats:{' '}
                          </span>
                          {state.profile.businessClassRows *
                            state.profile.businessClassColumns}
                        </p>
                        <p>
                          <span className='font-semibold'>
                            Economy class seats:{' '}
                          </span>
                          {state.profile.economyClassRows *
                            state.profile.economyClassColumns}
                        </p>
                      </span>
                    )}
                  </section>

                  <section className='grid items-center w-full grid-cols-5 gap-2'>
                    <p className='col-span-2'>First Class Cost</p>
                    <span className='col-span-3'>
                      <InputNumber
                        style={{ width: '100%' }}
                        placeholder='Enter a value'
                        formatter={(value) => `$ ${value}`}
                        min={1}
                        value={state.firstClassCost}
                        onChange={(value) => {
                          setState((oldState) => ({
                            ...oldState,
                            firstClassCost: value
                              ? Number.parseInt(value, 10)
                              : 1
                          }))
                        }}
                      />
                    </span>
                  </section>

                  <section className='grid items-center w-full grid-cols-5 gap-2'>
                    <p className='col-span-2'>Business Class Cost</p>
                    <span className='col-span-3'>
                      <InputNumber
                        style={{ width: '100%' }}
                        placeholder='Enter a value'
                        formatter={(value) => `$ ${value}`}
                        min={1}
                        value={state.businessClassCost}
                        onChange={(value) => {
                          setState((oldState) => ({
                            ...oldState,
                            businessClassCost: value
                              ? Number.parseInt(value, 10)
                              : 1
                          }))
                        }}
                      />
                    </span>
                  </section>

                  <section className='grid items-center w-full grid-cols-5 gap-2'>
                    <p className='col-span-2'>Economy Class Cost</p>
                    <span className='col-span-3'>
                      <InputNumber
                        style={{ width: '100%' }}
                        placeholder='Enter a value'
                        formatter={(value) => `$ ${value}`}
                        min={1}
                        value={state.economyClassCost}
                        onChange={(value) => {
                          setState((oldState) => ({
                            ...oldState,
                            economyClassCost: value
                              ? Number.parseInt(value, 10)
                              : 1
                          }))
                        }}
                      />
                    </span>
                  </section>
                </div>

                <section className='grid items-center w-full grid-cols-12 gap-4 p-3 sm:gap-2 bg-gray-50'>
                  <p className='col-span-3 font-bold sm:col-span-4'>
                    Departure
                  </p>
                  <Select
                    className='col-span-9 sm:col-span-8'
                    placeholder='Select origin airport'
                    value={state.origin.id}
                    onSelect={(key) => {
                      const newOrigin = airports.find(
                        (airport) => airport.id === key
                      )
                      const newStopovers =
                        getStopoversWithoutSameLocations(key)

                      setState((oldState) => ({
                        ...oldState,
                        origin: newOrigin,
                        stopOvers: newStopovers
                      }))
                    }}
                  >
                    {airports &&
                      airports
                        .filter((a) =>
                          state.destination
                            ? a.id !== state.destination.id
                            : a
                        )
                        .map(({ id, name, code }) => (
                          <Option key={id}>
                            <p>
                              ({code}) {name}
                            </p>
                          </Option>
                        ))}
                  </Select>
                  <span className='hidden sm:block sm:col-span-4' />
                  <DatePicker
                    className='col-span-8 sm:col-span-5'
                    disabledDate={invalidDeparture}
                    disabledTime={invalidDeparture}
                    allowClear={false}
                    placeholder='Departure date & time'
                    value={state.departure}
                    onChange={(date) =>
                      setState((oldState) => ({
                        ...oldState,
                        departure: date
                      }))
                    }
                    format='YYYY-MM-DD HH:mm'
                    showTime={{
                      defaultValue: moment('00:00', 'HH:mm')
                    }}
                  />
                  <Input
                    className='col-span-4 sm:col-span-3'
                    readOnly
                    value={moment
                      .tz(state.origin.zoneId)
                      .format('Z z')}
                  />
                </section>

                <section className='grid items-center w-full grid-cols-12 gap-4 p-3 sm:gap-2 bg-gray-50'>
                  <p className='col-span-3 font-bold sm:col-span-4'>
                    Arrival
                  </p>
                  <Select
                    className='col-span-9 sm:col-span-8'
                    placeholder='Select destination airport'
                    value={state.destination.id}
                    onSelect={(key) => {
                      const newDestination = airports.find(
                        (airport) => airport.id === key
                      )

                      const newStopovers =
                        getStopoversWithoutSameLocations(key)

                      setState((oldState) => ({
                        ...oldState,
                        destination: newDestination,
                        stopOvers: newStopovers
                      }))
                    }}
                  >
                    {airports &&
                      airports
                        .filter((a) =>
                          state.origin ? a.id !== state.origin.id : a
                        )
                        .map(({ id, name, code }) => (
                          <Option key={id}>
                            <p>
                              ({code}) {name}
                            </p>
                          </Option>
                        ))}
                  </Select>
                  <span className='hidden sm:block sm:col-span-4' />
                  <DatePicker
                    className='col-span-8 sm:col-span-5'
                    disabledDate={invalidArrival}
                    disabledTime={invalidArrival}
                    allowClear={false}
                    placeholder='Arrival date & time'
                    value={state.arrival}
                    onChange={(date) =>
                      setState((oldState) => ({
                        ...oldState,
                        arrival: date
                      }))
                    }
                    format='YYYY-MM-DD HH:mm'
                    showTime={{
                      defaultValue: moment('00:00', 'HH:mm')
                    }}
                  />
                  <Input
                    className='col-span-4 sm:col-span-3'
                    readOnly
                    value={moment
                      .tz(state.destination.zoneId)
                      .format('Z z')}
                  />
                </section>

                <section className='grid items-center w-full grid-cols-12 gap-4 p-3 sm:gap-2 bg-gray-50'>
                  <p className='col-span-8 font-bold sm:col-span-9'>
                    Stopovers
                  </p>
                  <button
                    className='col-span-4 py-2 font-semibold text-white transition-colors bg-yellow-600 hover:bg-yellow-500 sm:col-span-3'
                    type='button'
                    onClick={addStopover}
                  >
                    Add Stopover
                  </button>
                  {renderStopovers()}
                </section>
              </main>
            )}

            <button
              type='submit'
              className='self-end p-2 font-semibold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
            >
              {loading ? <Spinner size={6} /> : 'Submit'}
            </button>
          </form>
        )}
      </section>
    </main>
  )
}

export default EditFlight
