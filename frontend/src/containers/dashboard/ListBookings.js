import React, { useEffect, useState } from 'react'
import moment from 'moment'
import { string } from 'prop-types'
import { Link } from 'react-router-dom'
import { useHistory } from 'react-router'
import { FaSadTear } from 'react-icons/fa'

// Hooks
import { useSessionStore } from '../../hooks/Store'

// Containers and Components
import Spinner from '../../components/common/Spinner'
import useBookings from '../../hooks/useBookings'

const ListBookings = ({ bookingsStatus }) => {
  const token = useSessionStore((state) => state.token)

  const user = useSessionStore((state) => state.user)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  useEffect(() => {
    if (!token || !user || user.userType !== 'customer') {
      history.push('/')
    } else {
      setValidUser(true)
    }
  }, [token, user, history])

  /* -------------------------------------------------------------------------- */

  const {
    data: customerBookings,
    isLoading,
    isSuccess
  } = useBookings(token)

  const heading = (
    <header className='flex flex-col gap-3'>
      {bookingsStatus === 'previous' ? (
        <h2 className='text-3xl font-bold'>Your previous bookings</h2>
      ) : (
        <h2 className='text-3xl font-bold'>Your current bookings</h2>
      )}
      <nav className='flex items-center gap-6'>
        <span className='flex gap-2'>
          <div className='w-5 h-5 bg-green-500 rounded-xl' />
          <span>One-way</span>
        </span>
        <span className='flex gap-2'>
          <div className='w-5 h-5 bg-blue-500 rounded-xl' />
          <span>Return</span>
        </span>
      </nav>
    </header>
  )

  const renderBookings = () => {
    const bookings =
      bookingsStatus === 'previous'
        ? customerBookings.filter((pb) =>
            pb.returnFlight
              ? moment() >= moment(pb.returnFlight.arrival)
              : moment() >= moment(pb.flight.arrival)
          )
        : customerBookings.filter((cb) =>
            cb.returnFlight
              ? moment() < moment(cb.returnFlight.arrival)
              : moment() < moment(cb.flight.arrival)
          )

    return (
      <section className='flex flex-col max-w-lg gap-4'>
        {!bookings ? (
          <div>
            No bookings yet <FaSadTear />
          </div>
        ) : (
          bookings.map((booking) => (
            <Link to={`/dashboard/bookings/${booking.id}`}>
              <article
                className={`${
                  booking.returnFlight
                    ? 'bg-blue-500 hover:bg-blue-400'
                    : 'bg-green-500 hover:bg-green-400'
                } transition-colors flex flex-col w-full text-white group px-4 py-2`}
              >
                <span className='flex justify-between w-full'>
                  <span>
                    {booking.flight.origin.code} -{' '}
                    {booking.flight.destination.code}
                  </span>
                  <span>
                    Booked on{' '}
                    {moment(booking.dateTime).format('YYYY/MM/DD')}
                  </span>
                </span>
              </article>
            </Link>
          ))
        )}
      </section>
    )
  }

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser ? (
        <Spinner size={6} />
      ) : (
        <section className='flex flex-col w-full h-full max-w-lg gap-4'>
          {heading}
          <hr />
          {isLoading && <Spinner size={6} />}
          {isSuccess && renderBookings()}
        </section>
      )}
    </main>
  )
}

ListBookings.propTypes = {
  bookingsStatus: string.isRequired
}

export default ListBookings
