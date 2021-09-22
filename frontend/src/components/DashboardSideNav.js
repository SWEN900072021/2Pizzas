import React from 'react'
import { element } from 'prop-types'
import { Link } from 'react-router-dom'

import NavBar from './NavBar'

const DashboardSideNav = ({ sectionOpened }) => (
  <section className='relative flex flex-col h-screen'>
    <NavBar />
    <main className='flex flex-col w-full h-full md:flex-row'>
      {/* Side Nav */}
      <section className='flex flex-row items-center justify-center gap-2 px-4 py-4 bg-yellow-600 md:min-w-max md:flex-col '>
        <Link
          to='/dashboard'
          className='text-center block py-2.5 px-4 rounded-lg transition duration-200 text-white hover:text-white hover:bg-yellow-500 font-bold shadow-sm'
        >
          My Info
        </Link>
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
      </section>
      {/* Page Content */}
      {sectionOpened}
    </main>
  </section>
)

DashboardSideNav.propTypes = {
  sectionOpened: element.isRequired
}

export default DashboardSideNav
