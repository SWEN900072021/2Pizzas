import React from 'react'
import { FaPizzaSlice } from 'react-icons/fa'
import { NavLink } from 'react-router-dom'
import Button from './Button'

const NavBar = () => (
  <nav className='fixed w-screen top-0 z-30 px-5 py-3 bg-white shadow-md focus:outline-none'>
    <div className='rounded-md focus:outline-none flex items-center justify-between'>
      <NavLink to='/' className='group inline-flex gap-3 '>
        <FaPizzaSlice className='text-yellow-600 h-8 w-8 group-hover:text-yellow-700 transition-colors' />
        <h1 className='font-medium text-2xl text-gray-800 group-hover:text-gray-600 transition-colors'>
          Pepperoni Planes
        </h1>
      </NavLink>
      <NavLink to='/login'>
        <Button label='Log In' submit onClick={() => {}} />
      </NavLink>
    </div>
  </nav>
)

export default NavBar
