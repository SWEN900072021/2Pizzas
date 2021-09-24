/* eslint-disable no-unused-vars */
/* eslint-disable no-unused-expressions */
import React, { useState } from 'react'
import { element } from 'prop-types'
import { useHistory } from 'react-router'
import { Route, Switch, NavLink } from 'react-router-dom'

// Hooks
import { useSessionStore } from '../../hooks/Store'

// Containers and Components
import ListBookings from './ListBookings'
import UserInfo from './UserInfo'
import NavBar from '../../components/NavBar'
import ViewBooking from './ViewBooking'
// import Spinner from '../components/Spinner'

const DashboardSideNav = ({ sectionOpened }) => {
  const token = useSessionStore((state) => state.token)

  const adminDashboard = (
    <div className='flex h-full'>
      <section className='h-full bg-yellow-600 text-blue-100 w-64 px-2'>
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
      <section className='h-full bg-yellow-600 text-blue-100 w-64 px-2'>
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
      <section className='h-full bg-yellow-600 text-blue-100 w-64 px-2'>
        <NavLink
          to='/dashboard/'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          My Info
        </NavLink>
        <NavLink
          to='/dashboard/currentBookings'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          Current Bookings
        </NavLink>
        <NavLink
          to='/dashboard/previousBookings'
          className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
        >
          Previous Bookings
        </NavLink>
      </section>
      {sectionOpened}
    </div>
  )
  return (
    <main className='relative h-screen flex flex-col'>
      <NavBar />
      {userDashboard}
    </main>
  )
}

DashboardSideNav.propTypes = {
  sectionOpened: element.isRequired
}

export default DashboardSideNav
