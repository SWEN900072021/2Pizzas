import React, { useState } from 'react'
import { Input, Select } from 'antd'
import moment from 'moment-timezone'
import { useHistory } from 'react-router'

import { AirportService } from '../../api'
import Spinner from '../../components/common/Spinner'
import { useSessionStore } from '../../hooks/Store'

const { Option } = Select

const CreateAirport = () => {
  const token = useSessionStore((state) => state.token)
  const history = useHistory()

  const [state, setState] = useState({
    name: '',
    code: '',
    location: '',
    zoneId: ''
  })

  const [loading, setLoading] = useState(false)

  const handleSubmit = (e) => {
    setLoading(true)

    e.preventDefault()
    const airport = {
      name: state.name,
      code: state.code,
      location: state.location,
      zoneId: state.zoneId
    }

    AirportService.createAirport({
      data: { airport, token },
      onSuccess: () => {
        setLoading(false)
        history.push('/dashboard/manage/airports')
      }
    })
  }

  const handleChange = (e) =>
    setState((oldState) => ({
      ...oldState,
      [e.target.id]: e.target.value
    }))

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
              placeholder='Enter airport name'
              onChange={handleChange}
            />
          </section>
          <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
            <p className='col-span-2 font-bold'>Code</p>
            <Input
              required
              id='code'
              className='col-span-3'
              placeholder='Enter airport code'
              onChange={handleChange}
            />
          </section>
          <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
            <p className='col-span-2 font-bold'>Location</p>
            <Input
              required
              id='location'
              className='col-span-3'
              placeholder='Enter airport location'
              onChange={handleChange}
            />
          </section>
          <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'>
            <p className='col-span-2 font-bold'>Time Zone</p>
            <Select
              required
              id='zoneId'
              className='col-span-3'
              placeholder='Select timezone'
              onSelect={(key) => {
                setState((oldState) => ({
                  ...oldState,
                  zoneId: key
                }))
              }}
            >
              {moment.tz.names().map((zoneId) => (
                <Option key={zoneId} value={zoneId}>
                  <span className='grid grid-flow-col'>
                    <p className='col-span-2'>
                      ({moment.tz(zoneId).format('Z z')})
                    </p>
                    <p className='justify-self-end'>{zoneId}</p>
                  </span>
                </Option>
              ))}
            </Select>
          </section>
          <button
            type='submit'
            onClick={handleSubmit}
            className='flex items-center self-end justify-center w-20 h-10 p-2 font-semibold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
          >
            {loading ? <Spinner size={5} /> : 'Submit'}
          </button>
        </form>
      </section>
    </main>
  )
}

export default CreateAirport
