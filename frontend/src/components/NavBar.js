import React from 'react'
import { useHistory } from 'react-router'
import { Menu, Dropdown } from 'antd'
import { NavLink } from 'react-router-dom'
import { FaPizzaSlice } from 'react-icons/fa'
import {
  BsChevronDown,
  BsChevronLeft,
  BsPerson
} from 'react-icons/bs'
import { IoIosAirplane, IoIosLogOut } from 'react-icons/io'

import { useSessionStore } from '../hooks/Store'

const NavBar = () => {
  const history = useHistory()
  const token = useSessionStore((state) => state.token)
  const username = useSessionStore((state) => state.username)
  const resetSession = useSessionStore((state) => state.resetSession)

  const handleLogout = (e) => {
    e.preventDefault()
    resetSession()
  }

  const loggedInMenu = (
    <Menu>
      <Menu.Item key='0'>
        <NavLink to='/' className='flex gap-2 items-center'>
          <IoIosAirplane className='h-5 w-5 text-gray-600' />
          Find flights
        </NavLink>
      </Menu.Item>
      <Menu.Item key='1'>
        <NavLink to='/' className='flex gap-2 items-center'>
          <BsPerson className='h-5 w-5 text-gray-600' />
          Account dashboard
        </NavLink>
      </Menu.Item>
      <Menu.Divider />
      <Menu.Item key='3'>
        <button
          type='button'
          onClick={handleLogout}
          className='flex gap-2 items-center'
        >
          <IoIosLogOut className='h-5 w-5 text-gray-600' />
          Log out
        </button>
      </Menu.Item>
    </Menu>
  )

  return (
    <nav className='w-full top-0 z-30 px-5 py-3 h-16 bg-white shadow-md focus:outline-none flex items-center justify-between'>
      <NavLink to='/' className='group inline-flex gap-3 '>
        <FaPizzaSlice className='text-yellow-600 h-8 w-8 group-hover:text-yellow-700 transition-colors' />
        <h1 className='font-medium text-2xl text-gray-800 group-hover:text-gray-600 transition-colors'>
          Pepperoni Planes
        </h1>
      </NavLink>
      {/* ----------------------------- LOGGED IN MENU ----------------------------- */}
      <Dropdown
        overlay={loggedInMenu}
        trigger={['click']}
        placement='bottomRight'
        className={`${!token && 'hidden'}`}
      >
        <span className='flex gap-2'>
          <span className='select-none cursor-pointer hover:underline'>
            Welcome, <span className='font-bold'>{username}</span>
          </span>
          <BsChevronDown className='h-5 w-5 text-gray-600' />
        </span>
      </Dropdown>
      {/* ----------------------------- LOGGED OUT MENU ---------------------------- */}
      <span className={`${token && 'hidden'}`}>
        {history.location.pathname === '/login' ||
        history.location.pathname === '/signup' ? (
          <span>
            <NavLink to='/'>
              <button
                type='button'
                className='p-2 sm:py-3 sm:px-4 rounded-lg transition-colors border-2 border-yellow-500 bg-yellow-500 hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-400 text-white font-bold'
              >
                <span className='flex gap-2 justify-center items-center'>
                  <BsChevronLeft />
                  Back to Home
                </span>
              </button>
            </NavLink>
          </span>
        ) : (
          <span className='flex justify-center items-center gap-3'>
            <NavLink to='/login'>
              <button
                type='button'
                className='p-2 sm:py-3 sm:px-4 rounded-lg transition-colors border-2 border-yellow-500 bg-yellow-500 hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-400 text-white font-bold'
              >
                Log In
              </button>
            </NavLink>
            <NavLink to='/signup'>
              <button
                type='button'
                className='p-2 sm:py-3 sm:px-4 rounded-lg border-2 border-yellow-500 bg-white text-yellow-600 font-bold hover:border-yellow-500 hover:bg-yellow-600 hover:text-white transition-colors'
              >
                Sign Up
              </button>
            </NavLink>
          </span>
        )}
      </span>
    </nav>
  )
}

export default NavBar
