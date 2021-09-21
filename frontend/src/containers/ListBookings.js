/* eslint-disable no-unused-vars */
/* eslint-disable jsx-a11y/no-noninteractive-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */

import React from 'react'
import moment from 'moment'
import { string } from 'prop-types'
// import { useHistory } from 'react-router'
import { Link } from 'react-router-dom'
// import { NavLink } from 'react-router-dom'

// Hooks
import { useSessionStore } from '../hooks/Store'

// Containers and Components
import Spinner from '../components/Spinner'
import useBookings from '../hooks/useBookings'

const ListBookings = ({ bookingsStatus }) => {
  const selectBooking = (id) => {
    console.log(id)
  }

  const token = useSessionStore((state) => state.token)
  const { data, isLoading } = useBookings(token)

  const heading =
    bookingsStatus === 'previous' ? (
      <h2 className='text-bold'>Your previous bookings</h2>
    ) : (
      <h2 className='text-bold'>Your current bookings</h2>
    )

  const bookings = isLoading ? (
    <Spinner size={6} />
  ) : (
    data.map((booking) => {
      if (
        bookingsStatus === 'previous' &&
        (booking.returnFlight
          ? moment() >= moment(booking.returnFlight.arrival)
          : moment() >= moment(booking.flight.arrival))
      ) {
        return (
          <Link href={`/dashboard/bookings/${booking.id}`}>
            <h2>
              Your trip to {booking.flight.destination.location} with{' '}
              {booking.flight.airline.name}!
            </h2>
          </Link>
        )
      }
      if (
        bookingsStatus === 'current' &&
        (booking.returnFlight
          ? moment() < moment(booking.returnFlight.arrival)
          : moment() < moment(booking.flight.arrival))
      ) {
        return (
          <Link href={`/dashboard/bookings/${booking.id}`}>
            <h2>
              Your trip to {booking.flight.destination.location} with{' '}
              {booking.flight.airline.name}!
            </h2>
          </Link>
        )
      }
      return null
    })
  )

  return (
    <main>
      {heading}
      <div>{bookings}</div>
    </main>
  )
}

ListBookings.propTypes = {
  bookingsStatus: string.isRequired
}

export default ListBookings
