/* eslint-disable no-unused-vars */
/* eslint-disable no-unused-expressions */
import React, { useState } from 'react'
import PropTypes, { string } from 'prop-types';
import { useHistory } from 'react-router'
import { Route, Switch, NavLink } from 'react-router-dom'

// Hooks
// import { useFormStore, useSessionStore } from '../hooks/Store'

// Containers and Components
import ListBookings from '../containers/ListBookings'
import UserInfo from '../containers/UserInfo'
import NavBar from './NavBar'
// import Spinner from '../components/Spinner'

const DashboardSideNav = ({sectionOpened}) => {

    let opened = null
    if (sectionOpened === 'myInfo') opened = <UserInfo />
    else if (sectionOpened === 'currentBookings') opened = <ListBookings bookingsStatus='current'/>
    else if (sectionOpened === 'previousBookings') opened = <ListBookings bookingsStatus='previous' />

    return (
        <main className='relative h-screen flex flex-col'>
            <NavBar />
            <div className='flex h-full'>
                <section className='h-full bg-yellow-600 text-blue-100 w-64 px-2'>
                    <NavLink to='/dashboard/myInfo' className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'>
                        My Info
                    </NavLink>
                    <NavLink to='/dashboard/currentBookings' className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'>
                        Current Bookings
                    </NavLink>
                    <NavLink to='/dashboard/previousBookings' className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'>
                        Previous Bookings
                    </NavLink>
                </section>
                {opened}
            </div>
        </main>
    )
}

DashboardSideNav.propTypes = {
    sectionOpened: string.isRequired
}

export default DashboardSideNav