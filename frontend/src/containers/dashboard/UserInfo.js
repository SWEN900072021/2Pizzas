import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router'

import { useSessionStore } from '../../hooks/Store'

const UserInfo = () => {
  const token = useSessionStore((state) => state.token)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  useEffect(() => {
    if (!token) {
      setValidUser(false)
      history.push('/')
    } else {
      setValidUser(true)
    }
  }, [history, token])

  const user = useSessionStore((state) => state.user)

  const heading = (
    <header className='flex flex-col gap-3'>
      <h2 className='text-3xl font-bold'>Your information</h2>
      {user.userType === 'customer' && (
        <span className='text-gray-500'>Customer</span>
      )}
      {user.userType === 'administrator' && (
        <span className='text-gray-500'>Administrator</span>
      )}
      {user.userType === 'airline' && (
        <span className='text-gray-500'>Airline</span>
      )}
    </header>
  )

  const userDetails = (
    <section className='grid grid-flow-row'>
      <span className='grid items-center justify-center grid-cols-2'>
        <span className='font-semibold'>Username</span>
        <span>{user.username}</span>
      </span>
    </section>
  )

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser ? (
        <p>Loading...</p>
      ) : (
        <section className='flex flex-col w-full h-full max-w-lg gap-4'>
          {heading}
          <hr />
          {userDetails}
        </section>
      )}
    </main>
  )
}

export default UserInfo
