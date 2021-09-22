import React from 'react'
// import { useHistory } from 'react-router'
// import { Link } from 'react-router-dom'
// import { NavLink } from 'react-router-dom'

// Hooks
// import Spinner from '../components/Spinner'
import { useSessionStore } from '../hooks/Store'
// import useUsers from '../hooks/useUsers'

// Containers and Components
// import NavBar from '../components/NavBar'
// import Spinner from '../components/Spinner'

const UserInfo = () => {
  const username = useSessionStore((state) => state.username)

  const heading = (
    <header className='flex flex-col gap-3'>
      <h2 className='text-3xl font-bold'>Your information</h2>
    </header>
  )

  const userDetails = (
    <section className='grid grid-flow-row'>
      <span className='grid items-center justify-center grid-cols-2'>
        <span className='font-semibold'>Username</span>
        <span>{username}</span>
      </span>
    </section>
  )

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      <section className='flex flex-col w-full h-full max-w-lg gap-4'>
        {heading}
        <hr />
        {userDetails}
      </section>
    </main>
  )
}

export default UserInfo
