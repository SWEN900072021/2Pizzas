import { Input } from 'antd'
import React, { useState } from 'react'
import { useQueryClient } from 'react-query'
import { useHistory } from 'react-router'
import { AirlineService } from '../../api'
import Spinner from '../../components/common/Spinner'
import { useSessionStore } from '../../hooks/Store'

const CreateAirline = () => {
  const token = useSessionStore((state) => state.token)
  const history = useHistory()

  const [state, setState] = useState({
    name: '',
    code: '',
    username: '',
    password: ''
  })

  const [loading, setLoading] = useState(false)

  const handleChange = (e) =>
    setState((oldState) => ({
      ...oldState,
      [e.target.id]: e.target.value
    }))

  const [error, setError] = useState(false)
  const queryClient = useQueryClient()
  const resetSession = useSessionStore((st) => st.resetSession)

  const validate = () => {
    if (
      !state.name ||
      !state.code ||
      !state.username ||
      !state.password
    ) {
      return 'All fields are required'
    }

    return null
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    setError(null)

    const airline = {
      name: state.name,
      code: state.code,
      username: state.username,
      password: state.password
    }

    const airlineError = validate()

    if (airlineError) {
      setError(airlineError)
      return
    }

    setLoading(true)

    AirlineService.createAirline({
      data: { token, airline },
      onSuccess: () => {
        setLoading(false)
        history.push('/dashboard/view/airlines')
      },
      onError: (err) => {
        setLoading(false)
        // console.log(err)
        if (
          err.response &&
          err.response.status &&
          err.response.status === 401
        ) {
          queryClient.clear()
          resetSession()
          history.push('/login')
        }

        if (
          err.response &&
          err.response.data &&
          err.response.data.message
        ) {
          setError(err.response.data.message)
        }
      }
    })
  }

  return (
    <main
      style={{ maxHeight: '80vh' }}
      className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'
    >
      <section className='flex flex-col w-full h-full max-w-lg gap-4'>
        <h1 className='text-3xl font-bold'>Create Airline</h1>
        <hr />
        <form
          className='flex flex-col items-start w-full h-full max-h-full gap-4 overflow-y-auto'
          onSubmit={handleSubmit}
        >
          <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
            <p className='col-span-2 font-bold'>Name</p>
            <Input
              required
              id='name'
              className='col-span-3'
              placeholder='Enter airline name'
              onChange={handleChange}
            />
          </section>
          <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
            <p className='col-span-2 font-bold'>Code</p>
            <Input
              required
              id='code'
              className='col-span-3'
              placeholder='Enter airline code'
              onChange={handleChange}
            />
          </section>
          <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
            <p className='col-span-2 font-bold'>Username</p>
            <Input
              required
              id='username'
              className='col-span-3'
              placeholder='Enter username'
              onChange={handleChange}
            />
          </section>
          <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
            <p className='col-span-2 font-bold'>Password</p>
            <Input.Password
              required
              id='password'
              className='col-span-3'
              placeholder='Enter password'
              onChange={handleChange}
            />
          </section>
          <span className='flex items-center justify-end w-full gap-3'>
            <p className='text-red-500'>{error || ''}</p>
            <button
              type='submit'
              onClick={handleSubmit}
              className='flex items-center self-end justify-center w-20 h-10 p-2 font-semibold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
            >
              {loading ? <Spinner size={5} /> : 'Submit'}
            </button>
          </span>
        </form>
      </section>
    </main>
  )
}

export default CreateAirline
