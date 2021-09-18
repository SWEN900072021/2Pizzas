import React from 'react'
import { useStore } from '../hooks/Store'

const FlightSearch = () => {
  const origin = useStore((state) => state.origin)
  const destination = useStore((state) => state.destination)

  return (
    <section className='flex p-4 rounded-xl text-white bg-yellow-500 gap-5 justify-center items-center'>
      <button type='button' className='bg-gray-300' />
      <div className='flex flex-col gap-5'>
        <div className='font-bold'>Origin: {origin}</div>
        <div className='font-bold'>Depart: {origin}</div>
      </div>
      <div className='flex flex-col gap-5'>
        <div className='font-bold'>Destination: {destination}</div>
        <div className='font-bold'>Arrive: {origin}</div>
      </div>
    </section>
  )
}

export default FlightSearch
