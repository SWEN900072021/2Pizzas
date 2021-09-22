import React, { useEffect } from 'react'
import { element } from 'prop-types'
import { Link } from 'react-router-dom'
import { useHistory } from 'react-router'

import NavBar from './NavBar'
import { useSessionStore } from '../hooks/Store'

const DashboardSideNav = ({ sectionOpened }) => {
  const history = useHistory()
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)

  const customerLinks = (
    <>
      <Link
        to='/dashboard/current-bookings'
        className='text-center block py-2.5 px-4 rounded-lg transition duration-200  text-white hover:text-white hover:bg-yellow-500 font-bold shadow-sm'
      >
        Current Bookings
      </Link>
      <Link
        to='/dashboard/previous-bookings'
        className='text-center block py-2.5 px-4 rounded-lg transition duration-200  text-white hover:text-white hover:bg-yellow-500 font-bold shadow-sm'
      >
        Previous Bookings
      </Link>
    </>
  )

  const administratorLinks = (
    <>
      <Link
        to='/dashboard/airlines'
        className='text-center block py-2.5 px-4 rounded-lg transition duration-200  text-white hover:text-white hover:bg-yellow-500 font-bold shadow-sm'
      >
        Airlines List
      </Link>
      <Link
        to='/dashboard/airports'
        className='text-center block py-2.5 px-4 rounded-lg transition duration-200  text-white hover:text-white hover:bg-yellow-500 font-bold shadow-sm'
      >
        Airports List
      </Link>
    </>
  )

  const airlineLinks = (
    <>
      <Link
        to='/dashboard/flights'
        className='text-center block py-2.5 px-4 rounded-lg transition duration-200  text-white hover:text-white hover:bg-yellow-500 font-bold shadow-sm'
      >
        My Flights
      </Link>
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
      {token && user && (
        <main className='flex flex-col w-full h-full md:flex-row'>
          {/* Side Nav */}
          <section className='flex flex-row items-center justify-center gap-2 px-4 py-4 bg-yellow-600 md:min-w-max md:flex-col '>
            <Link
              to='/dashboard'
              className='text-center block py-2.5 px-4 rounded-lg transition duration-200 text-white hover:text-white hover:bg-yellow-500 font-bold shadow-sm'
            >
              My Info
            </Link>
            {user.userType === 'customer' && customerLinks}
            {user.userType === 'administrator' && administratorLinks}
            {user.userType === 'airline' && airlineLinks}
          </section>
          {/* Page Content */}
          {sectionOpened}
        </main>
      )}
    </section>
  )
}

DashboardSideNav.propTypes = {
  sectionOpened: element.isRequired
}

export default DashboardSideNav
