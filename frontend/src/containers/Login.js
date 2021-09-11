import React from 'react'
import Button from '../components/Button'
import TextField from '../components/TextField'

const Login = () => (
  <div className='flex flex-wrap justify-center content-center h-screen'>
    <div className='flex flex-wrap flex-col justify-center content-center mx-auto px-5 py-5 space-y-4 rounded-xl bg-purple-50'>
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
        className='self-center rounded-sm text-sm px-1 text-purple-800 hover:underline focus:outline-none focus:ring-2 focus:ring-purple-400 focus:underline'
      >
        Don&apos;t have an account?
      </button>
    </div>
  </div>
)

export default Login
