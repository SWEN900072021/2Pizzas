import React, { useEffect, useState } from 'react'
import { Steps } from 'antd'
import moment from 'moment-timezone'
import {
  FaChevronLeft,
  FaPlane,
  FaPlaneArrival,
  FaPlaneDeparture
} from 'react-icons/fa'
import { useHistory, useParams } from 'react-router'
import Spinner from '../../components/common/Spinner'
import { useSessionStore } from '../../hooks/Store'
import useBookings from '../../hooks/useBookings'

const ViewBooking = () => {
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  useEffect(() => {
    if (!token || !user || user.userType !== 'customer') {
      setValidUser(false)
      history.push('/')
    } else {
      setValidUser(true)
    }
  }, [token, user, history])

  /* -------------------------------------------------------------------------- */

  const { id } = useParams()
  const resetSession = useSessionStore((state) => state.resetSession)
  const { data, isLoading, isSuccess, isError } = useBookings(token)
  const [booking, setBooking] = useState(null)

  useEffect(() => {
    if (isError) {
      resetSession()
      history.push('/')
    }

    if (isSuccess && data && id) {
      const currentBooking = data.find((b) => b.id === id)
      setBooking(currentBooking)
    }
  }, [data, isSuccess, id, history, isError, resetSession])

  const goBack = () => {
    history.goBack()
  }

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser || !isSuccess || !booking ? (
        <div>{isLoading && <Spinner size={6} />}</div>
      ) : (
        <section className='flex flex-col w-full max-w-lg gap-4'>
          <span className='flex flex-col items-start self-start gap-2'>
            <h1 className='text-3xl font-bold'>Booking Details</h1>
            <button
              type='button'
              onClick={goBack}
              className='flex items-center justify-center gap-2 transition-colors hover:text-yellow-600'
            >
              <FaChevronLeft /> Back
            </button>
          </span>
          <hr />
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
                  icon={
                    <FaPlaneArrival className='text-yellow-500' />
                  }
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
                        {moment(
                          booking.returnFlight.departure
                        ).format('Do MMMM YYYY')}
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
                          {moment(
                            booking.returnFlight.arrival
                          ).format('hh:mm A z')}
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
