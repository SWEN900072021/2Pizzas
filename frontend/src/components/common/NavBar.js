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
import { useQueryClient } from 'react-query'

import { useSessionStore } from '../../hooks/Store'

const NavBar = () => {
  const history = useHistory()
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)
  const resetSession = useSessionStore((state) => state.resetSession)
  const queryClient = useQueryClient()

  const handleLogout = (e) => {
    e.preventDefault()
    resetSession()
    queryClient.clear()
    history.push('/login')
  }

  const loggedInMenu = (
    <Menu>
      {user && user.userType === 'customer' && (
        <Menu.Item key='0'>
          <NavLink
            data-cy='find-flights-menu-button'
            to='/'
            className='flex items-center gap-2'
          >
            <IoIosAirplane className='w-5 h-5 text-gray-600' />
            Find flights
          </NavLink>
        </Menu.Item>
      )}
      <Menu.Item key='1'>
        <NavLink
          data-cy='dashboard-menu-button'
          to='/dashboard'
          className='flex items-center gap-2'
        >
          <BsPerson className='w-5 h-5 text-gray-600' />
          Account dashboard
        </NavLink>
      </Menu.Item>
      <Menu.Divider />
      <Menu.Item key='3'>
        <button
          data-cy='logout-menu-button'
          type='button'
          onClick={handleLogout}
          className='flex items-center gap-2'
        >
          <IoIosLogOut className='w-5 h-5 text-gray-600' />
          Log out
        </button>
      </Menu.Item>
    </Menu>
  )

  return (
    <nav className='z-30 flex items-center justify-between w-full h-16 px-5 py-3 bg-white shadow-md -0 focus:outline-none'>
      <NavLink
        to='/'
        className='inline-flex items-center gap-3 group'
      >
        <FaPizzaSlice className='w-8 h-8 text-yellow-600 transition-colors group-hover:text-yellow-700' />
        <h1 className='text-xl font-medium text-gray-800 transition-colors sm:text-2xl group-hover:text-gray-600'>
          Pepperoni Planes
        </h1>
      </NavLink>
      {/* ----------------------------- LOGGED IN MENU ----------------------------- */}
      {token && user && (
        <Dropdown
          overlay={loggedInMenu}
          trigger={['click']}
          placement='bottomRight'
        >
          <span className='flex gap-2'>
            <span className='cursor-pointer select-none hover:underline'>
              <span className='font-bold'>{user.username}</span>
            </span>
            <BsChevronDown className='w-5 h-5 text-gray-600' />
          </span>
        </Dropdown>
      )}
      {/* ----------------------------- LOGGED OUT MENU ---------------------------- */}
      <span className={`${token && 'hidden'}`}>
        {history.location.pathname === '/login' ||
        history.location.pathname === '/signup' ? (
          <span data-cy='logged-out-auth-menu'>
            <NavLink data-cy='back-to-home-menu-button' to='/'>
              <button
                type='button'
                className='p-2 font-bold text-white transition-colors bg-yellow-500 border-2 border-yellow-500 rounded-lg sm:py-3 sm:px-4 hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-400'
              >
                <span className='flex items-center justify-center gap-2'>
                  <BsChevronLeft />
                  Back to Home
                </span>
              </button>
            </NavLink>
          </span>
        ) : (
          <span
            data-cy='logged-out-menu'
            className='flex items-center justify-center gap-3'
          >
            <NavLink data-cy='login-menu-button' to='/login'>
              <button
                type='button'
                className='p-2 font-bold text-white transition-colors bg-yellow-500 border-2 border-yellow-500 rounded-lg sm:py-3 sm:px-4 hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-400'
              >
                Log In
              </button>
            </NavLink>
            <NavLink data-cy='signup-menu-button' to='/signup'>
              <button
                type='button'
                className='p-2 font-bold text-yellow-600 transition-colors bg-white border-2 border-yellow-500 rounded-lg sm:py-3 sm:px-4 hover:border-yellow-500 hover:bg-yellow-600 hover:text-white'
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
