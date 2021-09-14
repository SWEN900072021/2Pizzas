import React from 'react'
import { Link } from 'react-router-dom'
import Button from '../components/Button'
import TextField from '../components/TextField'
import NavBar from '../components/NavBar'
import thailandPicture from '../assets/thailand.png'

const Login = () => (
  <main className='h-screen'>
    <NavBar />
    <section className='h-screen flex flex-wrap justify-center content-center'>
      <img
        src={thailandPicture}
        alt='Landscape with stone structures in Thailand'
        className='fixed'
      />
      <section className='z-10 flex flex-wrap flex-col justify-center content-center mx-auto p-5 space-y-4 rounded-xl bg-yellow-50'>
        <TextField placeholder='Username' />
        <TextField placeholder='Password' password />
        <Link to='/'>
          <Button label='Log In' />
        </Link>
        <button
          type='button'
          onClick={() => {}}
          className='self-center rounded-sm text-sm px-1 text-yellow-800 hover:underline focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:underline'
        >
          Don&apos;t have an account?
        </button>
      </section>
    </section>
  </main>
)

export default Login
