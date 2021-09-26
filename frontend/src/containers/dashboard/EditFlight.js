import React, { useEffect, useState } from 'react'
import { Input, Select, DatePicker, InputNumber } from 'antd'
import { useHistory, useParams } from 'react-router'
import moment from 'moment-timezone'
import { v4 as uuid } from 'uuid'

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
  const { isError: airportsError, refetch: refetchAirports } =
    useAirports()

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
      airports &&
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
    airplaneProfilesSuccess,
    airports,
    flightId,
    flights,
    flightsSuccess,
    state
  ])

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const validate = () => {
    if (state.arrival.isBefore(state.departure)) {
      return 'Arrival time must be after departure time'
    }

    return null
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    setError(null)

    const updatedFlight = {
      departure: state.departure.utc().format(),
      arrival: state.arrival.utc().format()
    }

    const flightError = validate(updatedFlight)

    if (flightError) {
      setError(flightError)
    } else {
      setLoading(true)
      // console.log('Update to flight:', updatedFlight)

      FlightService.updateFlight({
        data: { token, id: flightId, flight: updatedFlight },
        onSuccess: () => {
          setLoading(false)
          history.push(`/dashboard/view/flights/${flightId}`)
        },
        onError: (err) => {
          setLoading(false)
          setError(err)
        }
      })
    }
  }

  /* -------------------------------------------------------------------------- */

  const invalidDeparture = (current) => current < moment()
  const invalidArrival = (current) =>
    current < moment() || current <= state.departure

  /* -------------------------------------------------------------------------- */

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
            disabled
            allowClear={false}
            placeholder='Arrival at stopover'
            value={stopover.arrival}
            format='YYYY-MM-DD HH:mm'
            showTime={{
              defaultValue: moment('00:00', 'HH:mm')
            }}
          />
          <Input
            className='col-span-5 sm:col-span-4'
            disabled
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
            disabled
            allowClear={false}
            placeholder='Departure at stopover'
            value={stopover.departure}
            format='YYYY-MM-DD HH:mm'
            showTime={{
              defaultValue: moment('00:00', 'HH:mm')
            }}
          />
          <Input
            className='col-span-5 sm:col-span-4'
            disabled
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
          <p>Loading...</p>
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
                    disabled
                    className='col-span-3'
                    placeholder='Enter flight code'
                  />
                </section>

                <div className='grid w-full grid-flow-row gap-2 p-3 bg-gray-50'>
                  <section className='grid items-start w-full grid-cols-5 gap-2 '>
                    <p className='col-span-2 font-bold'>
                      Airplane Profile
                    </p>

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
                        disabled
                        style={{ width: '100%' }}
                        placeholder='Enter a value'
                        formatter={(value) => `$ ${value}`}
                        min={1}
                        value={state.firstClassCost}
                      />
                    </span>
                  </section>

                  <section className='grid items-center w-full grid-cols-5 gap-2'>
                    <p className='col-span-2'>Business Class Cost</p>
                    <span className='col-span-3'>
                      <InputNumber
                        disabled
                        style={{ width: '100%' }}
                        placeholder='Enter a value'
                        formatter={(value) => `$ ${value}`}
                        min={1}
                        value={state.businessClassCost}
                      />
                    </span>
                  </section>

                  <section className='grid items-center w-full grid-cols-5 gap-2'>
                    <p className='col-span-2'>Economy Class Cost</p>
                    <span className='col-span-3'>
                      <InputNumber
                        disabled
                        style={{ width: '100%' }}
                        placeholder='Enter a value'
                        formatter={(value) => `$ ${value}`}
                        min={1}
                        value={state.economyClassCost}
                      />
                    </span>
                  </section>
                </div>

                <section className='grid items-center w-full grid-cols-12 gap-4 p-3 sm:gap-2 bg-gray-50'>
                  <p className='col-span-3 font-bold sm:col-span-4'>
                    Departure
                  </p>
                  <Select
                    disabled
                    className='col-span-9 sm:col-span-8'
                    placeholder='Select origin airport'
                    value={state.origin.id}
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
                        departure: moment.tz(
                          date.format('YYYY-MM-DD HH:mm'),
                          state.origin.zoneId
                        )
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
                    disabled
                    className='col-span-9 sm:col-span-8'
                    placeholder='Select destination airport'
                    value={state.destination.id}
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
                        arrival: moment.tz(
                          date.format('YYYY-MM-DD HH:mm'),
                          state.destination.zoneId
                        )
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
                  <p className='col-span-12 font-bold sm:col-span-9'>
                    Stopovers
                  </p>
                  {renderStopovers()}
                </section>
              </main>
            )}

            <span className='flex items-center justify-end w-full gap-3'>
              <p className='text-red-500'>{error || ''}</p>
              <button
                type='submit'
                className='self-end p-2 font-semibold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
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

export default EditFlight
