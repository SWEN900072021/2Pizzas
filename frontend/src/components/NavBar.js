import React from 'react'
import { useHistory } from 'react-router'
import { Menu, Dropdown } from 'antd'
import { NavLink } from 'react-router-dom'
import { FaPizzaSlice } from 'react-icons/fa'
import { BsChevronDown, BsPerson } from 'react-icons/bs'
import { IoIosAirplane, IoIosLogOut } from 'react-icons/io'

import { useSessionStore } from '../hooks/Store'
import Button from './Button'

const NavBar = () => {
  const history = useHistory()
  const token = useSessionStore((state) => state.token)
  const setToken = useSessionStore((state) => state.setToken)
  const username = useSessionStore((state) => state.username)

  const handleLogout = (e) => {
    e.preventDefault()
    setToken('')
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
          <IoIosLogOut className='h-5 w-5 text-gray-600' /> Log out
        </button>
      </Menu.Item>
    </Menu>
  )

  return (
    <nav className='w-screen top-0 z-30 px-5 py-3 h-16 bg-white shadow-md focus:outline-none flex items-center justify-between'>
      <NavLink to='/' className='group inline-flex gap-3 '>
        <FaPizzaSlice className='text-yellow-600 h-8 w-8 group-hover:text-yellow-700 transition-colors' />
        <h1 className='font-medium text-2xl text-gray-800 group-hover:text-gray-600 transition-colors'>
          Pepperoni Planes
        </h1>
      </NavLink>

      {token ? (
        <Dropdown
          overlay={loggedInMenu}
          trigger={['click']}
          placement='bottomRight'
        >
          <span className='flex gap-2'>
            <span className='select-none cursor-pointer hover:underline'>
              Welcome, <span className='font-bold'>{username}</span>
            </span>
            <BsChevronDown className='h-5 w-5 text-gray-600' />
          </span>
        </Dropdown>
      ) : (
        <NavLink
          to='/login'
          className={`${
            history.location.pathname === '/login' && 'hidden'
          }`}
        >
          <Button label='Log In' submit onClick={() => {}} />
        </NavLink>
      )}
    </nav>
  )
}

export default NavBar
