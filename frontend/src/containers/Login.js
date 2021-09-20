import React, { useState } from 'react'
import { useHistory } from 'react-router'
import { Link } from 'react-router-dom'

// Hooks
import { useFormStore, useSessionStore } from '../hooks/Store'

// Containers and Components
import TextField from '../components/TextField'
import NavBar from '../components/NavBar'
import Spinner from '../components/Spinner'

// Assets
import thailandPicture from '../assets/thailand.png'
import { login } from '../api'

const Login = () => {
  const history = useHistory()
  const setToken = useSessionStore((state) => state.setToken)
  const setSessionValue = useSessionStore(
    (state) => state.setSessionValue
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

    login(user)
      .then((res) => {
        if (res.status === 200) {
          setToken(res.data.token)
          setSessionValue('username', user.username)
          setLoading(false)
          history.push('/')
        }
      })
      .catch((err) => {
        if (err.response.status === 401) {
          setLoading(false)
          setErrorMessage('Invalid username or password.')
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
        <h1 className='z-10 text-4xl font-bold text-white'>Log In</h1>
        <form
          onSubmit={handleSubmit}
          className='z-10 flex flex-col flex-wrap items-stretch justify-center p-5 mx-auto space-y-4 text-center rounded-xl bg-yellow-50'
        >
          <TextField
            required
            value={username}
            onChange={handleUsernameChange}
            placeholder='Username'
          />
          <TextField
            required
            value={password}
            onChange={handlePasswordChange}
            placeholder='Password'
            password
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
          <Link to='/signup'>
            <button
              type='button'
              onClick={() => {}}
              className='self-center px-1 text-sm text-yellow-800 rounded-sm hover:underline focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:underline'
            >
              Don&apos;t have an account?
            </button>
          </Link>
        </form>
      </section>
    </main>
  )
}

export default Login
