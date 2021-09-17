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
      <section className='h-screen flex flex-col justify-center items-center gap-5'>
        <img
          draggable={false}
          src={pisaPicture}
          alt='Leaning Tower of Pisa in Italy'
          className='fixed h-screen w-screen object-cover object-center'
        />
        <h1 className='z-10 text-white text-4xl font-bold'>
          Sign up
        </h1>
        <form
          onSubmit={handleSubmit}
          className='z-10 bg-green-600 flex flex-wrap flex-col mx-auto space-y-4 p-5 rounded-xl'
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
                : 'flex py-1 text-red-500 text-xs self-center'
            }`}
          >
            {errorMessage}
          </div>
          <button
            type='submit'
            className='self-center rounded-sm text-sm px-1 text-yellow-800 hover:underline focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:underline'
          >
            Confirm
          </button>
        </form>
      </section>
    </main>
  )
}

export default Signup
