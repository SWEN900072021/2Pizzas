import React from 'react'
import Button from '../components/Button'
import TextField from '../components/TextField'

const Login = () => (
  <main className='h-screen flex flex-wrap justify-center content-center'>
    <section className='flex flex-wrap flex-col justify-center content-center mx-auto p-5 space-y-4 rounded-xl bg-yellow-50'>
      <TextField placeholder='Username' />
      <TextField placeholder='Password' password />
      <Button
        label='Log In'
        onClick={() => {
          window.location.href = '/home'
        }}
        submit
      />
      <button
        type='button'
        onClick={() => {}}
        className='self-center rounded-sm text-sm px-1 text-yellow-800 hover:underline focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:underline'
      >
        Don&apos;t have an account?
      </button>
    </section>
  </main>
)

export default Login
