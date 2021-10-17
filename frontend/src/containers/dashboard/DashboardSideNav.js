import React, { useEffect } from 'react'
import { element } from 'prop-types'
import { NavLink } from 'react-router-dom'
import { useHistory } from 'react-router'

import { useSessionStore } from '../../hooks/Store'

// Containers and Components
import NavBar from '../../components/common/NavBar'
// import Spinner from '../components/common/Spinner'

const DashboardSideNav = ({ sectionOpened }) => {
  const history = useHistory()
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)

  useEffect(() => {
    if (!user) {
      window.location.href = window.location.origin
    }
  }, [user])

  const adminDashboard = (
    <>
      <NavLink
        data-cy='my-info-link'
        to='/dashboard'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        My Info
      </NavLink>
      <NavLink
        data-cy='view-airlines-link'
        to='/dashboard/view/airlines'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        View Airlines
      </NavLink>
      <NavLink
        data-cy='view-airports-link'
        to='/dashboard/view/airports'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        View Airports
      </NavLink>
      <NavLink
        data-cy='add-airline-link'
        to='/dashboard/create/airlines'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        Add Airline
      </NavLink>
      <NavLink
        data-cy='add-airport-link'
        to='/dashboard/create/airports'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        Add Airport
      </NavLink>
    </>
  )

  const airlineDashboard = (
    <>
      <NavLink
        data-cy='my-info-link'
        to='/dashboard'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        My Info
      </NavLink>
      <NavLink
        data-cy='view-flights-link'
        to='/dashboard/view/flights'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        View Flights
      </NavLink>
      <NavLink
        data-cy='create-flight-link'
        to='/dashboard/create/flights'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        Create Flight
      </NavLink>
    </>
  )

  const userDashboard = (
    <>
      <NavLink
        data-cy='my-info-link'
        to='/dashboard'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        My Info
      </NavLink>
      <NavLink
        data-cy='current-bookings-link'
        to='/dashboard/view/bookings/current'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        Current Bookings
      </NavLink>
      <NavLink
        data-cy='previous-bookings-link'
        to='/dashboard/view/bookings/previous'
        className='block py-2.5 px-4 rounded-lg transition duration-200 hover:bg-yellow-500 hover:text-white font-bold shadow-sm'
      >
        Previous Bookings
      </NavLink>
    </>
  )

  useEffect(() => {
    if (!token) {
      history.push('/')
    }
  }, [token, history])

  return (
    <section className='relative flex flex-col h-screen'>
      <NavBar />
      <main className='flex flex-col w-full md:flex-row md:h-full '>
        <nav
          data-cy='dashboard-side-nav'
          className='flex flex-row items-center justify-center gap-2 px-4 py-4 text-center text-white bg-yellow-600 md:min-w-max md:flex-col '
        >
          {user && user.userType === 'customer' && userDashboard}
          {user && user.userType === 'airline' && airlineDashboard}
          {user &&
            user.userType === 'administrator' &&
            adminDashboard}
        </nav>

        {sectionOpened}
      </main>
    </section>
  )
}

DashboardSideNav.propTypes = {
  sectionOpened: element.isRequired
}

export default DashboardSideNav
