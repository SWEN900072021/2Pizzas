/* eslint-disable no-unused-vars */
/* eslint-disable no-unused-expressions */
import React, { useEffect, useState } from 'react'
import { element } from 'prop-types'
import { Route, Switch, NavLink } from 'react-router-dom'

// Hooks
import { useSessionStore } from '../../hooks/Store'

// Containers and Components
import ListBookings from './ListBookings'
import UserInfo from './UserInfo'
import NavBar from '../../components/common/NavBar'
import ViewBooking from './ViewBooking'
// import Spinner from '../components/Spinner'

const DashboardSideNav = ({ sectionOpened }) => {
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)

  useEffect(() => {
    if (!user) {
      window.location.href = window.location.origin
    }
  }, [user])

  const adminDashboard = (
    <div className='flex h-full'>
      <section className='w-64 h-full px-2 text-blue-100 bg-yellow-600'>
        <NavLink
          to='/dashboard/manage/airlines'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          View Airlines
        </NavLink>
        <NavLink
          to='/dashboard/manage/airports'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          View Airports
        </NavLink>
        <NavLink
          to='/dashboard/manage/airlines/create'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          Add Airline
        </NavLink>
        <NavLink
          to='/dashboard/manage/airports/create'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          Add Airport
        </NavLink>
      </section>
      {sectionOpened}
    </div>
  )

  const airlineDashboard = (
    <div className='flex h-full'>
      <section className='w-64 h-full px-2 text-blue-100 bg-yellow-600'>
        <NavLink
          to='/dashboard/manage/flights'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          View Flights
        </NavLink>
        <NavLink
          to='/dashboard/manage/flights/create'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          Create Flight
        </NavLink>
      </section>
      {sectionOpened}
    </div>
  )

  const userDashboard = (
    <div className='flex h-full'>
      <section className='w-64 h-full px-2 text-blue-100 bg-yellow-600'>
        <NavLink
          to='/dashboard/'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          My Info
        </NavLink>
        <NavLink
          to='/dashboard/current-bookings'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          Current Bookings
        </NavLink>
        <NavLink
          to='/dashboard/previous-bookings'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          Previous Bookings
        </NavLink>
      </section>
      {sectionOpened}
    </div>
  )
  return (
    <main className='relative flex flex-col h-screen'>
      <NavBar />
      {user && user.userType === 'customer' && userDashboard}
      {user && user.userType === 'airline' && airlineDashboard}
      {user && user.userType === 'administrator' && adminDashboard}
    </main>
  )
}

DashboardSideNav.propTypes = {
  sectionOpened: element.isRequired
}

export default DashboardSideNav
