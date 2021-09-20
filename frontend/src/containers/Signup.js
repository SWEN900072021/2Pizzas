import React, { useState } from 'react'
import { useHistory } from 'react-router'

// Containers and Components
// import Button from '../components/Button'
import TextField from '../components/TextField'
import NavBar from '../components/NavBar'

// Hooks
import { useFormStore, useSessionStore } from '../hooks/Store'

// Assets
import pisaPicture from '../assets/pisa.jpg'
import { signup } from '../api'

const Signup = () => {
  const history = useHistory()
  const setToken = useSessionStore((state) => state.setToken)
  const setSessionValue = useSessionStore(
    (state) => state.setSessionValue
  )

  const username = useFormStore((state) => state.username)
  const givenName = useFormStore((state) => state.givenName)
  const surname = useFormStore((state) => state.surname)
  const email = useFormStore((state) => state.email)
  const [password, setPassword] = useState('')

  const setUsername = useFormStore((state) => state.setUsername)
  const setGivenName = useFormStore((state) => state.setGivenName)
  const setSurname = useFormStore((state) => state.setSurname)
  const setEmail = useFormStore((state) => state.setEmail)

  const [errorMessage, setErrorMessage] = useState('')

  const handleChange = (e) => {
    if (e.target.name === 'Username') {
      setUsername(e.target.value)
    } else if (e.target.name === 'Given Name') {
      setGivenName(e.target.value)
    } else if (e.target.name === 'Surname') {
      setSurname(e.target.value)
    } else if (e.target.name === 'Email') {
      setEmail(e.target.value)
    } else if (e.target.name === 'Password') {
      setPassword(e.target.value)
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    const user = {
      username,
      givenName,
      surname,
      email,
      password
    }

    signup(user)
      .then((res) => {
        if (res.status === 200) {
          setToken(res.data.token)
          setSessionValue('username', user.username)
          history.push('/')
        }
      })
      .catch((err) => {
        console.log(err)
        setErrorMessage('Cannot create user.')
      })
  }

  return (
    <main className='h-screen'>
      <NavBar />
      <section className='flex flex-col items-center justify-center h-screen gap-5'>
        <img
          draggable={false}
          src={pisaPicture}
          alt='Leaning Tower of Pisa in Italy'
          className='fixed object-cover object-center w-screen h-screen'
        />
        <h1 className='z-10 text-lg font-bold text-white'>Signup</h1>
        <form
          onSubmit={handleSubmit}
          className='z-10 flex flex-col flex-wrap p-5 mx-auto bg-green-600 space-y-4 rounded-xl'
        >
          <TextField
            value={username}
            name='Username'
            onChange={handleChange}
            placeholder='Username'
          />
          <TextField
            value={givenName}
            name='Given Name'
            onChange={handleChange}
            placeholder='Given Name'
          />
          <TextField
            value={surname}
            name='Surname'
            onChange={handleChange}
            placeholder='Surname'
          />
          <TextField
            value={email}
            name='Email'
            onChange={handleChange}
            placeholder='Email'
          />
          <TextField
            value={password}
            name='Password'
            onChange={handleChange}
            password
            placeholder='Password'
          />
          <div
            className={`${
              !errorMessage
                ? 'hidden'
                : 'flex py-1 text-red-500 text-xs text-center'
            }`}
          >
            {errorMessage}
          </div>
          <button
            type='submit'
            className='self-center px-1 text-sm text-yellow-800 rounded-sm hover:underline focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:underline'
          >
            Confirm
          </button>
        </form>
      </section>
    </main>
  )
}

export default Signup
