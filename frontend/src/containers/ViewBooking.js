import React from 'react'

import Spinner from '../components/Spinner'

import { useSessionStore } from '../hooks/Store'
import useBookings from '../hooks/useBookings'

const ViewBooking = () => {
    const token = useSessionStore((state) => state.token)
    const { data, isLoading } = useBookings(token)

    const seatingAllocations = data.seatsAllocations(seat => 
        <li>
            {seat.givenName} {seat.surname} : seat {seat.seatName}
        </li>
    )

    const bookingInfo = isLoading ? <Spinner size={6} /> : (
        <div>
            <h1>Your trip to {data.flight.destination.location} with {data.airline.name}</h1>
            <div>
                <h2>DEPARTING {data.flight.origin.name} at {data.departure}</h2>
                <h2>ARRIVING {data.flight.destination.name} at {data.arrival}</h2>
                <div>
                    <h2>SEATING ARRANGEMENTS</h2>
                    <ul>
                        {seatingAllocations}
                    </ul>
                </div>
            </div>
        </div>
    )

    return (
        <main>
            {bookingInfo}
        </main>
    )
}

export default ViewBooking