/* eslint-disable no-unused-expressions */
import React, { useState } from 'react'
// import { useHistory } from 'react-router'
// import { Link } from 'react-router-dom'
import { Route } from 'react-router-dom'

// Hooks
// import { useFormStore, useSessionStore } from '../hooks/Store'

// Containers and Components

import ListBookings from './ListBookings'
// import NavBar from '../components/NavBar'
// import Spinner from '../components/Spinner'

const UserDashboard = () => {
    // const [infoOpen, setInfoOpen] = useState(true)
    // const [currentBookingsOpen, setCurrentBookingsOpen] = useState(false)
    // const [previousBookingsOpen, setPreviousBookingsOpen] = useState(false)
    const [sectionOpenURL, setSectionOpenURL] = useState('myInfo')

    const sectionHandler = (buttonType) => {
        switch(buttonType) {
            case 'myInfo':
                sectionOpenURL === 'myInfo' ? null : setSectionOpenURL('myInfo')
                // eslint-disable-next-line no-console
                console.log(sectionOpenURL)
                break
            case 'currentBookings':
                sectionOpenURL === 'currentBookings' ? null : setSectionOpenURL('currentBookings')
                // eslint-disable-next-line no-console
                console.log(sectionOpenURL)
                break
            case 'previousBookings':
                sectionOpenURL === 'previousBookings' ? null : setSectionOpenURL('previousBookings')
                // eslint-disable-next-line no-console
                console.log(sectionOpenURL)
                break
            default:
                null
        }
    }

    // const myInfo = (
    //     <section className='flex-1 p-10 text-2xl font-bold'>
    //         hi info
    //     </section>
    // )

    // const currentBookings = (
    //     <section className='flex-1 p-10 text-2xl font-bold'>
    //         hi current bookings
    //     </section>
    // )

    // const previousBookings = (
    //     <section className='flex-1 p-10 text-2xl font-bold'>
    //         hi previous bookings
    //     </section>
    // )

    return (
        <main className='relative min-h-screen flex'>
            {/* <NavBar /> */}
            {/* sidebar */}
            <section className='bg-blue-800 text-blue-100 w-64 px-2'>
                <button 
                    type='button'
                    onClick={() => sectionHandler('myInfo')}
                    className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-blue-700 hover:text-white'>
                        My Info
                </button>
                <button 
                    type='button'
                    onClick={() => sectionHandler('currentBookings')}
                    className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-blue-700 hover:text-white'>
                        Current Bookings
                </button>
                <button 
                    type='button' 
                    onClick={() => sectionHandler('previousBookings')}
                    className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-blue-700 hover:text-white'>
                        Previous Bookings
                </button>
            </section>
            <Route path={`/dashboard/${sectionOpenURL}`} exact component={ListBookings} />
        </main>
    )
}

export default UserDashboard