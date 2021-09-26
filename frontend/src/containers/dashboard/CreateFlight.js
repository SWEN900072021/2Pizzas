/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from 'react'
import { Input, Select, DatePicker, InputNumber } from 'antd'
import { useHistory } from 'react-router'
import moment from 'moment-timezone'
import { v4 as uuid } from 'uuid'
import { BsTrashFill } from 'react-icons/bs'

import Spinner from '../../components/common/Spinner'
import useAirplaneProfiles from '../../hooks/useAirplaneProfiles'
import useAirports from '../../hooks/useAirports'
import { useSessionStore } from '../../hooks/Store'
import { FlightService } from '../../api'

const { Option } = Select

const CreateFlight = () => {
  /* ------------------------ Preliminary Data Fetching ----------------------- */

  const token = useSessionStore((state) => state.token)

  const {
    data: airplaneProfiles,
    error: airplaneProfilesError,
    refetch: refetchAirplaneProfiles
  } = useAirplaneProfiles()
  const { error: airportsError, refetch: refetchAirports } =
    useAirports()

  const history = useHistory()

  const [airports, setAirports] = useState(null)

  useEffect(() => {
    if (!airplaneProfiles) {
      refetchAirplaneProfiles()
    }
    if (!airports) {
      refetchAirports().then((res) => {
        const activeAirports = res.data.filter(
          (airport) => airport.status === 'ACTIVE'
        )
        setAirports(activeAirports)
      })
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
  }, [airportsError, airplaneProfilesError, history])

  /* -------------------------------------------------------------------------- */
  /*                                 Form State                                 */
  /* -------------------------------------------------------------------------- */

  const [state, setState] = useState({
    code: '',
    profile: {},
    origin: {},
    destination: {},
    departure: moment(),
    arrival: moment(),
    firstClassCost: 1,
    businessClassCost: 1,
    economyClassCost: 1,
    stopOvers: []
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const validate = () => {
    if (state.code === '') {
      return 'Flight code is required'
    }

    if (state.profile === {}) {
      return 'Airplane profile is required'
    }

    if (!state.origin.id) {
      return 'Origin is required'
    }

    if (!state.destination.id) {
      return 'Destination is required'
    }

    if (state.arrival.isBefore(state.departure)) {
      return 'Arrival time must be after departure time'
    }

    if (state.firstClassCost <= 0) {
      return 'First class cost must be greater than 0'
    }

    if (state.businessClassCost <= 0) {
      return 'Business class cost must be greater than 0'
    }

    if (state.economyClassCost <= 0) {
      return 'Economy class cost must be greater than 0'
    }

    if (state.stopOvers.length === 0) {
      let stopoverIndex = 1

      state.stopOvers.forEach((stopOver) => {
        if (stopOver.arrival.isAfter(stopOver.departure)) {
          return `Arrival at stopover ${stopoverIndex} must be after departure from it`
        }

        if (stopOver.departure.isBefore(stopOver.arrival)) {
          return `Departure at stopover ${stopoverIndex} must be before arrival to it`
        }

        if (
          stopOver.arrival.isBefore(state.departure) ||
          stopOver.departure.isBefore(state.departure)
        ) {
          return `Duration at stopover ${stopoverIndex} must be after flight departure`
        }

        if (
          stopOver.arrival.isAfter(state.arrival) ||
          stopOver.departure.isAfter(state.arrival)
        ) {
          return `Duration at ${stopoverIndex} must happen before flight arrival`
        }

        stopoverIndex += 1

        return null
      })
    }
    return null
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    setError(null)

    const flight = {
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

    const flightError = validate()

    if (flightError) {
      setError(flightError)
    } else {
      setLoading(true)
      FlightService.createFlight({
        data: { token, flight },
        onSuccess: () => {
          setLoading(false)
          history.push('/dashboard/view/flights')
        },
        onError: (err) => {
          setLoading(false)
          console.log(err)
        }
      })
    }
  }

  const invalidDeparture = (current) => {
    const originZoneId = state.origin.zoneId
    const zonedCurrent = moment.tz(
      current.format('YYYY-MM-DD HH:mm'),
      originZoneId
    )

    const zonedNow = moment.tz(
      moment().format('YYYY-MM-DD HH:mmZ'),
      originZoneId
    )

    return zonedCurrent.isSameOrBefore(zonedNow)
  }
  const invalidArrival = (current) => {
    const destinationZoneId = state.destination.zoneId
    const zonedCurrent = moment.tz(
      current.format('YYYY-MM-DD HH:mm'),
      destinationZoneId
    )

    const zonedDeparture = moment.tz(
      state.departure.format('YYYY-MM-DD HH:mmZ'),
      destinationZoneId
    )

    return zonedCurrent.isSameOrBefore(zonedDeparture)
  }

  const invalidStopoverArrival = (current, index) => {
    const stopoverZoneId = airports.find(
      (a) => a.id === state.stopOvers[index].location
    ).zoneId
    const zonedCurrent = moment.tz(
      current.format('YYYY-MM-DD HH:mm'),
      stopoverZoneId
    )

    const zonedDeparture = moment.tz(
      state.departure.format('YYYY-MM-DD HH:mmZ'),
      stopoverZoneId
    )

    const zonedArrival = moment.tz(
      state.arrival.format('YYYY-MM-DD HH:mmZ'),
      stopoverZoneId
    )

    const beforeLastStopoverDeparture =
      index - 1 >= 0
        ? zonedCurrent.isBefore(
            moment.tz(
              state.stopOvers[index - 1].departure.format(
                'YYYY-MM-DD HH:mmZ'
              ),
              stopoverZoneId
            ),
            'day'
          )
        : false

    const beforeFlightDeparture = zonedCurrent.isBefore(
      zonedDeparture,
      'day'
    )
    const afterFlightArrival = zonedCurrent.isAfter(
      zonedArrival,
      'day'
    )

    // const beforePreviousStopoverDeparture =
    //   index - 1 > 0 && current < state.stopOvers[index].departure
    // const afterNextStopoverArrival =
    //   index + 1 < state.stopOvers.length &&
    //   current > state.stopOvers[index + 1].arrival

    // console.log(
    //   'Zoned current:\n -',
    //   zonedCurrent.format(),
    //   '\n - Departure:\n',
    //   zonedDeparture.format(),
    //   '\n - Arrival:\n',
    //   zonedArrival.format(),
    //   '\nIs before departure:',
    //   beforeFlightDeparture,
    //   '\nIs after arrival:',
    //   afterFlightArrival
    // )

    return (
      beforeLastStopoverDeparture ||
      beforeFlightDeparture ||
      afterFlightArrival
    )
  }

  const invalidStopoverDeparture = (current, index) => {
    const stopoverZoneId = airports.find(
      (a) => a.id === state.stopOvers[index].location
    ).zoneId
    const zonedCurrent = moment.tz(
      current.format('YYYY-MM-DD HH:mm'),
      stopoverZoneId
    )
    // .set('hour', zonedDeparture.hour())
    // .set('minute', zonedDeparture.add(1, 'minute').minute())

    const zonedDeparture = moment.tz(
      state.departure.format('YYYY-MM-DD HH:mmZ'),
      stopoverZoneId
    )

    const zonedArrival = moment.tz(
      state.arrival.format('YYYY-MM-DD HH:mmZ'),
      stopoverZoneId
    )

    const beforeFlightDeparture = zonedCurrent.isBefore(
      zonedDeparture,
      'day'
    )
    const afterFlightArrival = zonedCurrent.isAfter(
      zonedArrival,
      'day'
    )

    // const beforePreviousStopoverDeparture =
    //   index - 1 > 0 && current < state.stopOvers[index].departure
    // const afterNextStopoverArrival =
    //   index + 1 < state.stopOvers.length &&
    //   current > state.stopOvers[index + 1].arrival

    // console.log(
    //   'Zoned current:\n -',
    //   zonedCurrent.format(),
    //   '\n - Departure:\n',
    //   zonedDeparture.format(),
    //   '\n - Arrival:\n',
    //   zonedArrival.format(),
    //   '\nIs before departure:',
    //   beforeFlightDeparture,
    //   '\nIs after arrival:',
    //   afterFlightArrival
    // )

    return beforeFlightDeparture || afterFlightArrival
  }

  const addStopover = () => {
    const stopOvers = [...state.stopOvers]

    stopOvers.push({
      location: null,
      departure: moment(),
      arrival: moment()
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
    if (!state.stopOvers) return null

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
            disabled={!stopover.location}
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
            required
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
            disabled={!stopover.location || !stopover.arrival}
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
        <h1 className='text-3xl font-bold'>Create Flight</h1>
        <hr />
        {!airplaneProfiles || !airports ? (
          <p>Loading...</p>
        ) : (
          <form
            className='flex flex-col items-start w-full h-full max-h-full gap-4 overflow-y-auto'
            onSubmit={handleSubmit}
          >
            <main className='flex flex-col items-start w-full h-full max-h-full gap-4 overflow-y-auto'>
              <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
                <p className='col-span-2 font-bold'>Flight Code</p>
                <Input
                  required
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
                      required
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
                      required
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
                      required
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
                  disabled={!state.origin.id}
                  disabledDate={invalidDeparture}
                  disabledTime={invalidDeparture}
                  allowClear={false}
                  placeholder='Departure date & time'
                  value={state.departure}
                  onChange={(date) => {
                    // console.log('Origin Zone:', state.origin.zoneId)
                    // console.log(
                    //   'Changed departure date:',
                    //   '\n',
                    //   date.format(),
                    //   '\n',
                    //   moment
                    //     .tz(
                    //       date.format('YYYY-MM-DD HH:mm'),
                    //       state.origin.zoneId
                    //     )
                    //     .format()
                    // )
                    setState((oldState) => ({
                      ...oldState,
                      departure: moment.tz(
                        date.format('YYYY-MM-DD HH:mm'),
                        state.origin.zoneId
                      )
                    }))
                  }}
                  format='YYYY-MM-DD HH:mm'
                  showTime={{
                    defaultValue: moment('00:00', 'HH:mm')
                  }}
                />
                <Input
                  required
                  className='col-span-4 sm:col-span-3'
                  readOnly
                  value={moment.tz(state.origin.zoneId).format('Z z')}
                />
              </section>

              <section className='grid items-center w-full grid-cols-12 gap-4 p-3 sm:gap-2 bg-gray-50'>
                <p className='col-span-3 font-bold sm:col-span-4'>
                  Arrival
                </p>
                <Select
                  className='col-span-9 sm:col-span-8'
                  placeholder='Select destination airport'
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
                  disabled={!state.destination.id}
                  disabledDate={invalidArrival}
                  disabledTime={invalidArrival}
                  allowClear={false}
                  placeholder='Arrival date & time'
                  value={state.arrival}
                  onChange={(date) => {
                    // console.log(
                    //   'Destination Zone:',
                    //   state.destination.zoneId
                    // )
                    // console.log(
                    //   'Changed arrival date:',
                    //   '\n',
                    //   date.format(),
                    //   '\n',
                    //   moment
                    //     .tz(
                    //       date.format('YYYY-MM-DD HH:mm'),
                    //       state.destination.zoneId
                    //     )
                    //     .format()
                    // )

                    setState((oldState) => ({
                      ...oldState,
                      arrival: moment.tz(
                        date.format('YYYY-MM-DD HH:mm'),
                        state.destination.zoneId
                      )
                    }))
                  }}
                  format='YYYY-MM-DD HH:mm'
                  showTime={{
                    defaultValue: moment('00:00', 'HH:mm')
                  }}
                />
                <Input
                  required
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
                  className={`${
                    !state.origin.id || !state.destination.id
                      ? 'bg-gray-400 cursor-not-allowed'
                      : 'bg-yellow-600 hover:bg-yellow-500'
                  } col-span-4 py-2 font-semibold text-white transition-colors sm:col-span-3`}
                  type='button'
                  disabled={!state.origin.id || !state.destination.id}
                  onClick={addStopover}
                >
                  Add Stopover
                </button>
                {renderStopovers()}
              </section>
            </main>

            <span className='flex items-center justify-end w-full gap-3'>
              <p className='text-red-500'>{error || ''}</p>
              <button
                type='submit'
                className='w-20 p-2 font-semibold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
              >
                {loading ? <Spinner size={6} /> : 'Submit'}
              </button>
            </span>
          </form>
        )}
      </section>
    </main>
  )
}

export default CreateFlight
