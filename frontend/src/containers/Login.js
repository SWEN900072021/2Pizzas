import React from 'react'
import Button from '../components/Button'
import TextField from '../components/TextField'
import NavBar from '../components/NavBar'
import thailandPicture from '../assets/thailand.png'
import { useFormStore } from '../hooks/Store'

const Login = () => {
  const username = useFormStore((state) => state.username)
  const password = useFormStore((state) => state.password)
  const setUsername = useFormStore((state) => state.setUsername)
  const setPassword = useFormStore((state) => state.setPassword)

  const handleUsernameChange = (e) => {
    setUsername(e.target.value)
  }

  const handlePasswordChange = (e) => {
    setPassword(e.target.value)
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    console.log(username, password)
  }

  return (
    <main className='h-screen'>
      <NavBar />
      <section className='h-screen flex flex-wrap justify-center content-center'>
        <img
          draggable={false}
          src={thailandPicture}
          alt='Landscape with stone structures in Thailand'
          className='fixed h-screen w-screen object-cover object-center'
        />
        <form
          onSubmit={handleSubmit}
          className='z-10 flex flex-wrap flex-col justify-center content-center mx-auto p-5 space-y-4 rounded-xl bg-yellow-50'
        >
          <TextField
            value={username}
            onChange={handleUsernameChange}
            placeholder='Username'
          />
          <TextField
            value={password}
            onChange={handlePasswordChange}
            placeholder='Password'
            password
          />
          <Button submit label='Log In' />
          <button
            type='button'
            onClick={() => {}}
            className='self-center rounded-sm text-sm px-1 text-yellow-800 hover:underline focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:underline'
          >
            Don&apos;t have an account?
          </button>
        </form>
      </section>
    </main>
  )
}

export default Login
