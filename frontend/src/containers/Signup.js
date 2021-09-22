import React, { useState } from 'react'
import { useHistory } from 'react-router'

// Containers and Components
// import Button from '../components/Button'
import TextField from '../components/TextField'
import NavBar from '../components/NavBar'
import Spinner from '../components/Spinner'

// Hooks
import { useFormStore, useSessionStore } from '../hooks/Store'
import { AuthenticationService } from '../api'

// Assets
import pisaPicture from '../assets/pisa.jpg'

const Signup = () => {
  const history = useHistory()
  const setToken = useSessionStore((state) => state.setToken)
  const setUser = useSessionStore((state) => state.setUser)

  const username = useFormStore((state) => state.username)
  const givenName = useFormStore((state) => state.givenName)
  const surname = useFormStore((state) => state.surname)
  const email = useFormStore((state) => state.email)
  const [password, setPassword] = useState('')

  const setUsername = useFormStore((state) => state.setUsername)
  const setGivenName = useFormStore((state) => state.setGivenName)
  const setSurname = useFormStore((state) => state.setSurname)
  const setEmail = useFormStore((state) => state.setEmail)

  const [loading, setLoading] = useState(false)
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

    setLoading(true)

    AuthenticationService.signup({
      data: user,
      onSuccess: (res) => {
        if (res.status === 200) {
          setToken(res.data.token)
          setUser({
            username: res.data.username,
            userType: res.data.userTYpe
          })
          setLoading(false)
          history.push('/')
        }
      },
      onError: () => {
        // console.log(err)
        setLoading(false)
        setErrorMessage('Cannot create user.')
      }
    })
  }

  return (
    <main className='flex flex-col h-screen'>
      <NavBar />
      <section className='flex flex-col items-center justify-center flex-grow h-full gap-5'>
        <img
          draggable={false}
          src={pisaPicture}
          alt='Leaning Tower of Pisa in Italy'
          className='fixed object-cover object-center w-screen h-screen'
        />
        <h1 className='z-10 text-4xl font-bold text-white'>
          Sign Up
        </h1>
        <form
          onSubmit={handleSubmit}
          className='z-10 flex flex-col flex-wrap p-5 mx-auto space-y-4 bg-blue-50 rounded-xl'
        >
          <TextField
            required
            className='focus:ring-blue-500'
            value={username}
            name='Username'
            onChange={handleChange}
            placeholder='Username'
          />
          <TextField
            required
            className='focus:ring-blue-500'
            value={givenName}
            name='Given Name'
            onChange={handleChange}
            placeholder='Given Name'
          />
          <TextField
            required
            className='focus:ring-blue-500'
            value={surname}
            name='Surname'
            onChange={handleChange}
            placeholder='Surname'
          />
          <TextField
            required
            className='focus:ring-blue-500'
            value={email}
            name='Email'
            onChange={handleChange}
            placeholder='Email'
          />
          <TextField
            required
            className='focus:ring-blue-500'
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
            className='p-3 font-bold text-white transition-colors bg-blue-500 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400'
          >
            <span className={`${loading && 'hidden'}`}>Sign Up</span>
            <span
              className={`${
                !loading && 'hidden'
              } flex justify-center items-center gap-3`}
            >
              Signing up...
              <Spinner />
            </span>
          </button>
        </form>
      </section>
    </main>
  )
}

export default Signup
