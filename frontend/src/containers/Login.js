import React, { useState } from 'react'
import { useHistory } from 'react-router'
import { Link } from 'react-router-dom'

// Hooks
import {
  useFormStore,
  useSessionStore,
  useBookingStore
} from '../hooks/Store'
import { AuthenticationService } from '../api'

// Containers and Components
import TextField from '../components/common/TextField'
import NavBar from '../components/common/NavBar'
import Spinner from '../components/common/Spinner'

// Assets
import thailandPicture from '../assets/thailand.png'

const Login = () => {
  const isCreatingBooking = useBookingStore(
    (state) => state.isCreatingBooking
  )
  const history = useHistory()
  const setToken = useSessionStore((state) => state.setToken)
  const setUser = useSessionStore((state) => state.setUser)
  const setCreatingBooking = useBookingStore(
    (state) => state.setCreatingBooking
  )

  const username = useFormStore((state) => state.username)
  const setUsername = useFormStore((state) => state.setUsername)
  const [password, setPassword] = useState('')

  const [loading, setLoading] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  const handleUsernameChange = (e) => {
    setUsername(e.target.value)
  }

  const handlePasswordChange = (e) => {
    setPassword(e.target.value)
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    const user = { username, password }

    setLoading(true)

    AuthenticationService.login({
      data: user,
      onSuccess: (res) => {
        setToken(res.data.token)
        setUser({
          username: res.data.username,
          userType: res.data.userType
        })
        setLoading(false)
        if (isCreatingBooking) {
          setCreatingBooking(false)
          if (res.data.userType !== 'customer') {
            history.push('/dashboard')
          } else {
            history.push('/booking/create')
          }
        } else if (res.data.userType !== 'customer') {
          history.push('/dashboard')
        } else {
          history.push('/')
        }
      },
      onError: (err) => {
        setLoading(false)
        if (err.response && err.response.status === 401) {
          setErrorMessage('Invalid username or password.')
        }
      }
    })
  }

  return (
    <main className='flex flex-col h-screen'>
      <NavBar />
      <section className='flex flex-col items-center justify-center flex-grow h-full gap-5'>
        <img
          draggable={false}
          src={thailandPicture}
          alt='Landscape with stone structures in Thailand'
          className='fixed object-cover object-center w-screen h-screen'
        />
        <h1
          data-cy='login-title'
          className='z-10 text-4xl font-bold text-white'
        >
          Log In
        </h1>
        <form
          onSubmit={handleSubmit}
          className='z-10 flex flex-col flex-wrap p-5 mx-auto space-y-4 rounded-lg bg-yellow-50'
        >
          <TextField
            datacy='username-input'
            required
            value={username}
            onChange={handleUsernameChange}
            placeholder='Username'
          />
          <TextField
            datacy='password-input'
            password
            required
            value={password}
            onChange={handlePasswordChange}
            placeholder='Password'
          />
          <div
            data-cy='error-text'
            className={`${
              !errorMessage
                ? 'hidden'
                : 'flex py-1 text-red-500 text-xs self-center'
            }`}
          >
            {errorMessage}
          </div>
          <button
            data-cy='login-button'
            type='submit'
            className='p-3 font-bold text-white transition-colors bg-yellow-500 rounded-lg hover:bg-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-400'
          >
            <span className={`${loading && 'hidden'}`}>Log In</span>
            <span
              className={`${
                !loading && 'hidden'
              } flex justify-center items-center gap-3`}
            >
              Logging in...
              <Spinner />
            </span>
          </button>
          <Link
            data-cy='signup-link'
            to='/signup'
            className='self-center px-1 text-sm text-center text-yellow-700 rounded-sm max-w-max hover:text-yellow-700 hover:underline focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:underline'
          >
            Don&apos;t have an account?
          </Link>
        </form>
      </section>
    </main>
  )
}

export default Login
