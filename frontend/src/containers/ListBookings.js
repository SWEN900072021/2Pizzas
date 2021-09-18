
import React from 'react'
import { string } from 'prop-types';
// import { useHistory } from 'react-router'
// import { Link } from 'react-router-dom'
// import { NavLink } from 'react-router-dom'

// Hooks
// import { useFormStore, useSessionStore } from '../hooks/Store'

// Containers and Components
// import NavBar from '../components/NavBar'
// import Spinner from '../components/Spinner'

const ListBookings = ({bookingsStatus}) => {
    // mock data
    const bookingData = {
        'bookings': [{
            'destination': 'New York',
            'status': 'current'},
        {
            'destination': 'Miami',
            'status': 'previous'}]
      }

    const bookings = bookingData.bookings.map(booking => {
        if (booking.status === bookingsStatus) {
            return (
                <li>
                    <h2>Your trip to {booking.destination}!</h2>
                </li>
            )
        }
        return null
    })

    return (
        // eslint-disable-next-line react/jsx-filename-extension
        <main>
            {/* {bookings == null ? 'hello' : 'yoyo'} */}
            <ul>
                {bookings}
            </ul>
        </main>
    )
}

ListBookings.propTypes = {
    bookingsStatus: string.isRequired
}

export default ListBookings