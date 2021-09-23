import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router'

import Spinner from '../../components/Spinner'

import { useSessionStore } from '../../hooks/Store'
import useBookings from '../../hooks/useBookings'

const ViewBooking = () => {
  const { id } = useParams()

  const token = useSessionStore((state) => state.token)
  const { data, isLoading, isSuccess } = useBookings(token)
  const [booking, setBooking] = useState(null)
  useEffect(() => {
    if (isSuccess) setBooking(data.filter((b) => b.id === id)[0])
  }, [data, isSuccess, id])

  return (
    <main>
      {isLoading ? (
        <Spinner size={6} />
      ) : (
        <div>
          <h1>
            Your trip to {data.flight.destination.location} with{' '}
            {data.airline.name}
          </h1>
          <div>
            <h2>
              DEPARTING {data.flight.origin.name} at {data.departure}
            </h2>
            <h2>
              ARRIVING {data.flight.destination.name} at{' '}
              {data.arrival}
            </h2>
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
          </div>
        </div>
      )}
    </main>
  )
}

export default ViewBooking
