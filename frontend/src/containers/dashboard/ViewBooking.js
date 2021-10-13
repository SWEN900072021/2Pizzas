import React, { Fragment, useEffect, useState } from 'react'
import { Steps, Table, Tag } from 'antd'
import { v4 as uuid } from 'uuid'
import moment from 'moment-timezone'
import {
  FaChevronLeft,
  FaPlane,
  FaPlaneArrival,
  FaPlaneDeparture
} from 'react-icons/fa'
import { Listbox, Transition } from '@headlessui/react'
import { useQueryClient } from 'react-query'
import { BiCheck, BiChevronDown } from 'react-icons/bi'
import { useHistory, useParams } from 'react-router'
import { useSessionStore } from '../../hooks/Store'
import useBookings from '../../hooks/useBookings'

const { Column } = Table

const ViewBooking = () => {
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  const resetSession = useSessionStore((state) => state.resetSession)
  const queryClient = useQueryClient()

  useEffect(() => {
    if (!token || !user || user.userType !== 'customer') {
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
  const { data, isLoading, isSuccess, isError } = useBookings(token)
  const [booking, setBooking] = useState(null)

  useEffect(() => {
    if (isError) {
      resetSession()
      queryClient.clear()
      history.push('/login')
    }

    if (isSuccess && data && id) {
      const currentBooking = data.find((b) => b.id === id)
      // console.log(currentBooking)
      setBooking(currentBooking)
    }
  }, [
    data,
    isSuccess,
    id,
    history,
    isError,
    resetSession,
    queryClient
  ])

  const goBack = () => {
    history.push('/dashboard/view/bookings/current')
  }

  /* -------------------------------------------------------------------------- */

  const detailType = [
    { name: 'Flight Details' },
    { name: 'Passenger Details' }
  ]

  const [detailToggle, setDetailToggle] = useState(detailType[0])

  /* -------------------------------------------------------------------------- */
  const flightDetails = () => {
    if (!booking) return null

    return (
      <>
        <article className='flex flex-col items-start justify-start flex-grow w-full gap-5'>
          <span className='text-lg font-bold'>
            <h2>Outbound Flight</h2>
          </span>
          <span className='flex items-center justify-center gap-3 text-md md:text-lg'>
            <h2 className='px-2 font-semibold text-white bg-green-500 rounded-xl'>
              {booking.flight.code}
            </h2>

            <div className='flex items-center justify-center gap-2 font-medium'>
              <span className='text-yellow-700'>
                {booking.flight.origin.location} (
                {booking.flight.origin.code})
              </span>
              <FaPlane className='text-gray-600' />
              <span className='text-yellow-700'>
                {booking.flight.destination.location} (
                {booking.flight.destination.code})
              </span>
            </div>
          </span>
          <section>
            <Steps direction='vertical' current={0}>
              <Steps.Step
                icon={
                  <FaPlaneDeparture className='text-yellow-500' />
                }
                title={
                  <span className='text-black'>
                    <span>Depart at </span>
                    <span className='text-lg font-semibold'>
                      {moment(booking.flight.departure).format(
                        'hh:mm A z'
                      )}
                    </span>
                    from{' '}
                    <span className='font-medium md:hidden'>
                      {booking.flight.origin.code}
                    </span>
                    <span className='hidden font-medium md:inline'>
                      {booking.flight.origin.name} (
                      {booking.flight.origin.code})
                    </span>
                  </span>
                }
                description={
                  <span className='font-semibold text-yellow-700'>
                    {moment(booking.flight.departure).format(
                      'Do MMMM YYYY'
                    )}
                  </span>
                }
              />

              <Steps.Step
                icon={<FaPlaneArrival className='text-yellow-500' />}
                title={
                  <span className='text-black'>
                    <span>Arrive at </span>
                    <span className='text-lg font-semibold'>
                      {moment(booking.flight.arrival).format(
                        'hh:mm A z'
                      )}
                    </span>
                    in{' '}
                    <span className='font-medium md:hidden'>
                      {booking.flight.destination.code}
                    </span>
                    <span className='hidden font-medium md:inline'>
                      {booking.flight.destination.name} (
                      {booking.flight.destination.code})
                    </span>
                  </span>
                }
                description={
                  <span className='font-semibold text-yellow-700'>
                    {moment(booking.flight.arrival).format(
                      'Do MMMM YYYY'
                    )}
                  </span>
                }
              />
            </Steps>
          </section>
        </article>
        {booking.returnFlight && <hr />}
        {booking.returnFlight && (
          <article className='flex flex-col items-start justify-start flex-grow w-full gap-5'>
            <span className='text-lg font-bold'>
              <h2>Return Flight</h2>
            </span>
            <span className='flex items-center justify-center gap-3 text-md md:text-lg'>
              <h2 className='px-2 font-semibold text-white bg-blue-500 rounded-xl'>
                {booking.returnFlight.code}
              </h2>

              <div className='flex items-center justify-center gap-2 font-medium'>
                <span className='text-yellow-700'>
                  {booking.returnFlight.origin.location} (
                  {booking.returnFlight.origin.code})
                </span>
                <FaPlane className='text-gray-600' />
                <span className='text-yellow-700'>
                  {booking.returnFlight.destination.location} (
                  {booking.returnFlight.destination.code})
                </span>
              </div>
            </span>
            <section>
              <Steps direction='vertical' current={0}>
                <Steps.Step
                  icon={
                    <FaPlaneDeparture className='text-yellow-500' />
                  }
                  title={
                    <span className='text-black'>
                      <span>Depart at </span>
                      <span className='text-lg font-semibold'>
                        {moment(
                          booking.returnFlight.departure
                        ).format('hh:mm A z')}
                      </span>
                      from{' '}
                      <span className='font-medium md:hidden'>
                        {booking.returnFlight.origin.code}
                      </span>
                      <span className='hidden font-medium md:inline'>
                        {booking.returnFlight.origin.name} (
                        {booking.returnFlight.origin.code})
                      </span>
                    </span>
                  }
                  description={
                    <span className='font-semibold text-yellow-700'>
                      {moment(booking.returnFlight.departure).format(
                        'Do MMMM YYYY'
                      )}
                    </span>
                  }
                />

                <Steps.Step
                  icon={
                    <FaPlaneArrival className='text-yellow-500' />
                  }
                  title={
                    <span className='text-black'>
                      <span>Arrive at </span>
                      <span className='text-lg font-semibold'>
                        {moment(booking.returnFlight.arrival).format(
                          'hh:mm A z'
                        )}
                      </span>
                      in{' '}
                      <span className='font-medium md:hidden'>
                        {booking.returnFlight.destination.code}
                      </span>
                      <span className='hidden font-medium md:inline'>
                        {booking.returnFlight.destination.name} (
                        {booking.returnFlight.destination.code})
                      </span>
                    </span>
                  }
                  description={
                    <span className='font-semibold text-yellow-700'>
                      {moment(booking.returnFlight.arrival).format(
                        'Do MMMM YYYY'
                      )}
                    </span>
                  }
                />
              </Steps>
            </section>
          </article>
        )}
      </>
    )
  }

  /* -------------------------------------------------------------------------- */

  const sort = (a, b) => {
    if (a < b) return -1
    if (a > b) return 1
    return 0
  }

  const passengerDetails = () => {
    if (!booking) return null

    return (
      <>
        <p className='text-lg font-bold'>Outbound Flight</p>
        <Table
          className='col-span-12'
          dataSource={
            booking.flight && booking.flight.seatAllocations
              ? booking.flight.seatAllocations.map(
                  (seatAllocation) => ({
                    seatName: seatAllocation.seat,
                    seatClass: seatAllocation.seatClass,
                    givenName: seatAllocation.givenName,
                    surname: seatAllocation.surname
                  })
                )
              : []
          }
          bordered
          size='middle'
          scroll={{ x: 600, y: 125 }}
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
        {booking.returnFlight && (
          <>
            <p className='text-lg font-bold'>Return Flight</p>
            <Table
              className='col-span-12'
              dataSource={
                booking.returnFlight &&
                booking.returnFlight.seatAllocations
                  ? booking.returnFlight.seatAllocations.map(
                      (seatAllocation) => ({
                        seatName: seatAllocation.seat,
                        seatClass: seatAllocation.seatClass,
                        givenName: seatAllocation.givenName,
                        surname: seatAllocation.surname
                      })
                    )
                  : []
              }
              bordered
              size='middle'
              scroll={{ x: 600, y: 125 }}
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
          </>
        )}
      </>
    )
  }

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser || !isSuccess || !booking ? (
        <div>{isLoading && <p>Loading...</p>}</div>
      ) : (
        <section className='flex flex-col w-full max-w-lg gap-4'>
          <header className='flex flex-col items-start self-start w-full gap-2'>
            <span className='flex justify-between w-full'>
              <h1 className='text-3xl font-bold'>Booking Details</h1>
              <div className='relative z-50'>
                <Listbox
                  value={detailToggle}
                  onChange={setDetailToggle}
                >
                  <Listbox.Button className='flex items-center justify-between grid-flow-col px-3 py-2 font-medium text-left text-yellow-900 rounded-lg shadow-md cursor-default w-44 bg-yellow-50 focus:outline-none focus-visible:ring-2 focus-visible:ring-opacity-75 focus-visible:ring-white focus-visible:ring-offset-orange-300 focus-visible:ring-offset-2 focus-visible:border-indigo-500 sm:text-sm'>
                    {detailToggle.name}
                    <BiChevronDown className='w-5 h-5' />
                  </Listbox.Button>
                  <Transition
                    as={Fragment}
                    leave='transition ease-in duration-100'
                    leaveFrom='opacity-100'
                    leaveTo='opacity-0'
                  >
                    <Listbox.Options className='absolute py-1 mt-1 overflow-auto text-base bg-white rounded-md shadow-lg min-w-max max-h-60 ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm'>
                      {detailType.map((type) => (
                        <Listbox.Option
                          key={uuid()}
                          value={type}
                          className='relative grid items-center justify-start grid-flow-col gap-1 px-3 py-2 text-gray-900 cursor-default select-none min-w-min hover:text-yellow-900 hover:bg-yellow-50'
                        >
                          <span className='justify-start'>
                            <BiCheck
                              className={`${
                                type.name !== detailToggle.name &&
                                'invisible'
                              } w-5 h-5`}
                            />
                          </span>
                          <span
                            className={`${
                              type.name === detailToggle.name
                                ? 'font-medium'
                                : 'font-normal'
                            }`}
                          >
                            {type.name}
                          </span>
                        </Listbox.Option>
                      ))}
                    </Listbox.Options>
                  </Transition>
                </Listbox>
              </div>
            </span>
            <button
              type='button'
              onClick={goBack}
              className='flex items-center justify-center gap-2 transition-colors hover:text-yellow-600'
            >
              <FaChevronLeft /> Back
            </button>
          </header>
          <hr />
          {detailToggle.name === 'Flight Details'
            ? flightDetails()
            : passengerDetails()}
          {booking.SeatAllocations && (
            <article className='w-full'>
              <div>
                <h2>SEATING ARRANGEMENTS</h2>
                <ul>
                  {booking.seatAllocations.map((seat) => (
                    <li>
                      {seat.givenName} {seat.surname} : seat{' '}
                      {seat.seatName}
                    </li>
                  ))}
                </ul>
              </div>
            </article>
          )}
          <hr />
          <section className='flex items-center justify-between w-full'>
            <span className='text-gray-500'>
              Booked on{' '}
              {moment(booking.dateTime).format('YYYY-MM-DD')}
            </span>
            <span className='text-2xl font-medium'>
              Total cost:{' '}
              <span className='font-bold'>${booking.totalCost}</span>
            </span>
          </section>
        </section>
      )}
    </main>
  )
}

export default ViewBooking
