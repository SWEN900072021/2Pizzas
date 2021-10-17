/* eslint-disable no-nested-ternary */
import React, { useEffect, useState } from 'react'
import { Table, Tag } from 'antd'
import moment from 'moment-timezone'
import {
  FaChevronLeft,
  FaPen
  // FaPlane,
  // FaPlaneArrival,
  // FaPlaneDeparture
} from 'react-icons/fa'
import { v4 as uuid } from 'uuid'
import { useHistory, useParams } from 'react-router'
import { useQueryClient } from 'react-query'
import { useSessionStore } from '../../hooks/Store'
import useFlights from '../../hooks/useFlights'
import useAirports from '../../hooks/useAirports'
import useFlightPassengers from '../../hooks/useFlightPassengers'
import { FlightService } from '../../api'

const { Column } = Table

const ViewFlight = () => {
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  const resetSession = useSessionStore((state) => state.resetSession)
  const queryClient = useQueryClient()

  useEffect(() => {
    if (!token || !user || user.userType !== 'airline') {
      setValidUser(false)
      resetSession()
      queryClient.clear()
      history.push('/login')
    } else {
      setValidUser(true)
    }
  }, [token, user, history, resetSession, queryClient])

  /* -------------------------------------------------------------------------- */

  const { id } = useParams()

  const {
    data,
    isLoading,
    isSuccess,
    isError: isFlightsError,
    refetch: refetchFlights
  } = useFlights(token)
  const { data: airports } = useAirports()
  const {
    data: passengers,
    refetch: refetchPassengers,
    isError: isPassengersError
  } = useFlightPassengers(token, id)

  const [flight, setFlight] = useState(null)

  useEffect(() => {
    if (isFlightsError || isPassengersError) {
      resetSession()
      queryClient.clear()
      history.push('/login')
    }

    if (isSuccess && data && id) {
      refetchFlights().then((res) => {
        const currentFlight = res.data.find((b) => b.id === id)
        setFlight(currentFlight)
      })
      // console.log('Current flight:', currentFlight)
    }

    refetchPassengers()
  }, [
    data,
    isSuccess,
    id,
    history,
    resetSession,
    flight,
    passengers,
    refetchPassengers,
    token,
    refetchFlights,
    queryClient,
    isFlightsError,
    isPassengersError
  ])

  const goBack = () => {
    history.push('/dashboard/view/flights')
  }

  const sort = (a, b) => {
    if (a < b) return -1
    if (a > b) return 1
    return 0
  }

  /* -------------------------------------------------------------------------- */

  const editFlight = () => {
    history.push(`/dashboard/edit/flights/${id}`)
  }

  /* -------------------------------------------------------------------------- */

  const [isUpdating, setIsUpdating] = useState(false)

  const cancelFlight = () => {
    setIsUpdating(true)

    FlightService.updateFlight({
      data: { token, id, flight: { status: 'CANCELLED' } },
      onSuccess: () => {
        refetchFlights().then(() => {
          setIsUpdating(false)
        })
      },
      onError: (err) => {
        setIsUpdating(false)
        if (
          err.response &&
          err.response.status &&
          err.response.status === 401
        ) {
          resetSession()
          queryClient.clear()
          history.push('/login')
        }
        // console.log('Error cancelling flight:', err.response)
      }
    })
  }

  const delayFlight = () => {
    setIsUpdating(true)

    FlightService.updateFlight({
      data: { token, id, flight: { status: 'DELAYED' } },
      onSuccess: () => {
        refetchFlights().then(() => {
          setIsUpdating(false)
        })
      },
      onError: (err) => {
        setIsUpdating(false)

        if (
          err.response &&
          err.response.status &&
          err.response.status === 401
        ) {
          resetSession()
          queryClient.clear()
          history.push('/login')
        }
        // console.log('Error delaying flight:', err.response)
      }
    })
  }

  const scheduleFlight = () => {
    setIsUpdating(true)

    FlightService.updateFlight({
      data: { token, id, flight: { status: 'TO_SCHEDULE' } },
      onSuccess: () => {
        refetchFlights().then(() => {
          setIsUpdating(false)
        })
      },
      onError: (err) => {
        setIsUpdating(false)
        if (err.response && err.response.status === 401) {
          resetSession()
          queryClient.clear()
          history.push('/')
        }
        // console.log('Error setting flight to schedule:', err.response)
      }
    })
  }

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser ||
      !isSuccess ||
      !flight ||
      !airports ||
      isLoading ||
      isUpdating ? (
        <div>
          <div>Loading...</div>
        </div>
      ) : (
        <section className='flex flex-col w-full max-w-lg gap-4'>
          <header className='flex flex-col items-start self-start w-full gap-2'>
            <div className='flex items-center justify-between w-full'>
              <h1 className='text-3xl font-bold'>Flight details</h1>
              <button
                type='button'
                className='flex items-center justify-center gap-2 p-2 text-white transition-colors bg-gray-400 rounded-md shadow-sm hover:bg-gray-600'
                onClick={editFlight}
              >
                <div className='font-bold'>Edit</div>
                <FaPen className='w-5 h-5' />
              </button>
            </div>
            <button
              type='button'
              onClick={goBack}
              className='flex items-center justify-center gap-2 transition-colors hover:text-yellow-600'
            >
              <FaChevronLeft /> Back
            </button>
          </header>
          <hr />
          <section className='flex items-stretch self-start gap-2'>
            {flight.status !== 'CANCELLED' && (
              <>
                <button
                  type='button'
                  disabled={isUpdating}
                  className='px-4 py-2 font-bold text-white transition-colors bg-red-500 rounded-lg hover:bg-red-600'
                  onClick={cancelFlight}
                >
                  Cancel Flight
                </button>
                {flight.status !== 'DELAYED' ? (
                  <button
                    type='button'
                    disabled={isUpdating}
                    className='px-4 py-2 font-bold text-white transition-colors bg-blue-400 rounded-lg hover:bg-blue-600'
                    onClick={delayFlight}
                  >
                    Mark as Delayed
                  </button>
                ) : (
                  <button
                    type='button'
                    disabled={isUpdating}
                    className='px-4 py-2 font-bold text-white transition-colors bg-green-400 rounded-lg hover:bg-green-600'
                    onClick={scheduleFlight}
                  >
                    Mark as To Schedule
                  </button>
                )}
              </>
            )}
            {flight.status === 'CANCELLED' && (
              <button
                type='button'
                disabled={isUpdating}
                className='px-4 py-2 font-bold text-white transition-colors bg-green-400 rounded-lg hover:bg-green-600'
                onClick={scheduleFlight}
              >
                Mark as To Schedule
              </button>
            )}
          </section>
          <main className='h-full overflow-y-auto max-h-96'>
            <section className='grid grid-cols-12 gap-y-4'>
              <>
                <div className='col-span-6 font-semibold '>
                  Status
                </div>
                <div className='col-span-6 pl-3 border-l'>
                  <Tag
                    color={
                      flight.status === 'CANCELLED'
                        ? 'volcano'
                        : flight.status === 'DELAYED'
                        ? 'geekblue'
                        : 'green'
                    }
                  >
                    {flight.status}
                  </Tag>
                </div>
              </>
              <>
                <div className='col-span-6 font-semibold '>
                  Flight Code
                </div>
                <div className='col-span-6 pl-3 font-medium border-l'>
                  {flight.code}
                </div>
              </>
              <>
                <div className='col-span-6 font-semibold '>
                  Origin Airport
                </div>
                <div className='grid-flow-row col-span-6 pl-3 border-l'>
                  <div className='font-medium'>
                    {flight.origin.name}
                  </div>
                  <div>{flight.origin.code}</div>
                  <div>{flight.origin.location}</div>
                </div>
              </>
              <>
                <div className='col-span-6 font-semibold '>
                  Departure Date/Time
                </div>
                <div className='col-span-6 pl-3 border-l'>
                  <div>
                    {moment(flight.departureLocal).format(
                      'YYYY/MM/DD'
                    )}
                  </div>
                  <div>
                    {moment(flight.departureLocal).format(
                      'HH:mm Z z'
                    )}
                  </div>
                </div>
              </>
              <>
                <div className='col-span-6 font-semibold '>
                  Destination Airport
                </div>
                <div className='grid-flow-row col-span-6 pl-3 border-l'>
                  <div className='font-medium'>
                    {flight.destination.name}
                  </div>
                  <div>{flight.destination.code}</div>
                  <div>{flight.destination.location}</div>
                </div>
              </>
              <>
                <div className='col-span-6 font-semibold '>
                  Arrival Date/Time
                </div>
                <div className='col-span-6 pl-3 border-l'>
                  <div>
                    {moment(flight.arrivalLocal).format('YYYY/MM/DD')}
                  </div>
                  <div>
                    {moment(flight.arrivalLocal).format('HH:mm Z z')}
                  </div>
                </div>
              </>
              <>
                {flight.stopOvers ? (
                  <>
                    <div className='col-span-3 font-semibold '>
                      Stopovers
                    </div>
                    <div className='grid-flow-row col-span-9 divide-y-2 divide-gray-200'>
                      {flight.stopOvers.map((stopover) => (
                        <span key={uuid()} className='min-h-full'>
                          <div className='pl-3 border-l'>
                            <div className='font-medium'>
                              {stopover.location.name} (
                              {stopover.location.code})
                            </div>
                            <div>
                              Arriving at{' '}
                              {moment(stopover.arrivalLocal).format(
                                'YYYY/MM/DD HH:mm Z z'
                              )}
                            </div>
                            <div>
                              Departing at{' '}
                              {moment(stopover.departureLocal).format(
                                'YYYY/MM/DD HH:mm Z z'
                              )}
                            </div>
                          </div>
                        </span>
                      ))}
                    </div>
                  </>
                ) : (
                  <>
                    <div className='col-span-6 font-semibold'>
                      Stopovers
                    </div>
                    <div className='col-span-6 pl-3 border-l'>
                      <div>N/A</div>
                    </div>
                  </>
                )}
              </>
              <>
                <div className='col-span-6 font-semibold '>
                  Seat Cost by Cabin Class
                </div>
                <div className='col-span-6 pl-3 border-l'>
                  <div>
                    <span className='font-medium'>First:</span> $
                    {flight.firstClassCost}/seat
                  </div>
                  <div>
                    <span className='font-medium'>Business:</span> $
                    {flight.businessClassCost}/seat
                  </div>
                  <div>
                    <span className='font-medium'>First:</span> $
                    {flight.economyClassCost}/seat
                  </div>
                </div>
              </>
              <hr className='col-span-12' />
              <div className='col-span-12 font-semibold'>
                Passenger Details
              </div>
              <Table
                className='col-span-12'
                dataSource={
                  passengers
                    ? passengers.map((passenger) => {
                        const seat = flight.seats.find(
                          (s) => s.name === passenger.seatName
                        )

                        return {
                          givenName: passenger.givenName,
                          surname: passenger.surname,
                          passportNumber: passenger.passportNumber,
                          dateOfBirth: moment(
                            passenger.dateOfBirth
                          ).format('YYYY/MM/DD'),
                          nationality: passenger.nationality,
                          booking: passenger.booking,
                          seatName: seat.name,
                          seatClass: seat.seatClass
                        }
                      })
                    : []
                }
                bordered
                size='middle'
                scroll={{ x: 1000, y: 200 }}
                pagination={false}
              >
                <Column
                  title='Given Name'
                  dataIndex='givenName'
                  width={100}
                  sorter={{
                    compare: (a, b) => sort(a.givenName, b.givenName)
                  }}
                />
                <Column
                  title='Surname'
                  dataIndex='surname'
                  width={80}
                  sorter={{
                    compare: (a, b) => sort(a.surname, b.surname)
                  }}
                />
                <Column
                  title='Passport No.'
                  dataIndex='passportNumber'
                  width={80}
                  sorter={{
                    compare: (a, b) =>
                      sort(a.passportNumber, b.passportNumber)
                  }}
                />
                <Column
                  title='Nationality'
                  dataIndex='nationality'
                  width={80}
                  sorter={{
                    compare: (a, b) =>
                      sort(a.passportNumber, b.passportNumber)
                  }}
                />
                <Column
                  title='Date of Birth'
                  dataIndex='dateOfBirth'
                  width={80}
                />
                <Column
                  title='Seat'
                  dataIndex='seatName'
                  fixed='right'
                  width={35}
                  sorter={{
                    compare: (a, b) => sort(a.seatName, b.seatName)
                  }}
                />
                <Column
                  title='Cabin Class'
                  dataIndex='seatClass'
                  fixed='right'
                  width={80}
                  sorter={{
                    compare: (a, b) => sort(a.seatClass, b.seatClass)
                  }}
                  render={(cabinClass) => {
                    let colour = 'green'

                    if (cabinClass === 'BUSINESS') {
                      colour = 'geekblue'
                    }
                    if (cabinClass === 'FIRST') {
                      colour = 'gold'
                    }

                    return (
                      <Tag color={colour} key={cabinClass}>
                        {cabinClass}
                      </Tag>
                    )
                  }}
                />
              </Table>
            </section>
          </main>
        </section>
      )}
    </main>
  )
}

export default ViewFlight
