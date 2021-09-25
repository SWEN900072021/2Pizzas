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
import { useHistory, useParams } from 'react-router'
import Spinner from '../../components/common/Spinner'
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

  useEffect(() => {
    if (!token || !user || user.userType !== 'airline') {
      setValidUser(false)
      history.push('/')
    } else {
      setValidUser(true)
    }
  }, [token, user, history])

  /* -------------------------------------------------------------------------- */

  const { id } = useParams()
  const resetSession = useSessionStore((state) => state.resetSession)
  const {
    data,
    isLoading,
    isSuccess,
    isError,
    refetch: refetchFlights
  } = useFlights(token)
  const { data: airports } = useAirports()
  const { data: passengers, refetch: refetchPassengers } =
    useFlightPassengers(token, id)

  const [flight, setFlight] = useState(null)

  useEffect(() => {
    if (isError) {
      resetSession()
      history.push('/')
    }

    if (isSuccess && data && id) {
      const currentFlight = data.find((b) => b.id === id)
      // console.log('Current flight:', currentFlight)
      setFlight(currentFlight)
    }

    if (token && id && !passengers) {
      refetchPassengers()
    }
  }, [
    data,
    isSuccess,
    id,
    history,
    isError,
    resetSession,
    flight,
    passengers,
    refetchPassengers,
    token
  ])

  const goBack = () => {
    history.goBack()
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
      onError: () => {
        setIsUpdating(false)
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
      onError: () => {
        setIsUpdating(false)
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
      onError: () => {
        setIsUpdating(false)
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
          <Spinner size={6} />
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
                <p className='font-bold'>Edit</p>
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
                <p className='col-span-6 font-semibold '>Status</p>
                <p className='col-span-6 pl-3 border-l'>
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
                </p>
              </>
              <>
                <p className='col-span-6 font-semibold '>
                  Flight Code
                </p>
                <p className='col-span-6 pl-3 font-medium border-l'>
                  {flight.code}
                </p>
              </>
              <>
                <p className='col-span-6 font-semibold '>
                  Origin Airport
                </p>
                <p className='grid-flow-row col-span-6 pl-3 border-l'>
                  <p className='font-medium'>{flight.origin.name}</p>
                  <p>{flight.origin.code}</p>
                  <p>{flight.origin.location}</p>
                </p>
              </>
              <>
                <p className='col-span-6 font-semibold '>
                  Departure Date/Time
                </p>
                <p className='col-span-6 pl-3 border-l'>
                  <p>
                    {moment(flight.departureLocal).format(
                      'YYYY/MM/DD'
                    )}
                  </p>
                  <p>
                    {moment(flight.departureLocal).format(
                      'HH:mm Z z'
                    )}
                  </p>
                </p>
              </>
              <>
                <p className='col-span-6 font-semibold '>
                  Destination Airport
                </p>
                <p className='grid-flow-row col-span-6 pl-3 border-l'>
                  <p className='font-medium'>{flight.origin.name}</p>
                  <p>{flight.origin.code}</p>
                  <p>{flight.origin.location}</p>
                </p>
              </>
              <>
                <p className='col-span-6 font-semibold '>
                  Arrival Date/Time
                </p>
                <p className='col-span-6 pl-3 border-l'>
                  <p>
                    {moment(flight.arrivalLocal).format('YYYY/MM/DD')}
                  </p>
                  <p>
                    {moment(flight.arrivalLocal).format('HH:mm Z z')}
                  </p>
                </p>
              </>
              <>
                {flight.stopOvers ? (
                  <>
                    <p className='col-span-3 font-semibold '>
                      Stopovers
                    </p>
                    <p className='grid-flow-row col-span-9 divide-y-2 divide-gray-200'>
                      {flight.stopOvers.map((stopover) => (
                        <>
                          <span
                            key={stopover.location.id}
                            className='min-h-full'
                          >
                            <article className='pl-3 border-l'>
                              <p className='font-medium'>
                                {stopover.location.name} (
                                {stopover.location.code})
                              </p>
                              <p>
                                Arriving at{' '}
                                {moment(stopover.arrivalLocal).format(
                                  'YYYY/MM/DD HH:mm Z z'
                                )}
                              </p>
                              <p>
                                Departing at{' '}
                                {moment(
                                  stopover.departureLocal
                                ).format('YYYY/MM/DD HH:mm Z z')}
                              </p>
                            </article>
                          </span>
                        </>
                      ))}
                    </p>
                  </>
                ) : (
                  <>
                    <p className='col-span-6 font-semibold'>
                      Stopovers
                    </p>
                    <p className='col-span-6 pl-3 border-l'>
                      <p>N/A</p>
                    </p>
                  </>
                )}
              </>
              <>
                <p className='col-span-6 font-semibold '>
                  Seat Cost by Cabin Class
                </p>
                <p className='col-span-6 pl-3 border-l'>
                  <p>
                    <span className='font-medium'>First:</span> $
                    {flight.firstClassCost}/seat
                  </p>
                  <p>
                    <span className='font-medium'>Business:</span> $
                    {flight.businessClassCost}/seat
                  </p>
                  <p>
                    <span className='font-medium'>First:</span> $
                    {flight.economyClassCost}/seat
                  </p>
                </p>
              </>
              <hr className='col-span-12' />
              <p className='col-span-12 font-semibold'>
                Passenger Details
              </p>
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
