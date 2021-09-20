import React from 'react'
// import { v4 as uuid } from 'uuid'

import FlightCard from '../components/FlightCard'
// eslint-disable-next-line no-unused-vars
import FlightFilter from '../components/FlightFilter'
import FlightSearch from '../components/FlightSearch'
import NavBar from '../components/NavBar'

const FlightListings = () => {
  const flights = [
    {
      flight: {
        airlineName: 'QANTAS',
        airlineCode: 'QA',
        origin: 'Melbourne',
        destination: 'Sydney',
        departure: '2020/10/10 09:00',
        arrival: '2020/10/10 15:00',
        stopovers: ['Perth', 'Darwin']
      },
      returnFlight: {
        airlineName: 'QANTAS',
        airlineCode: 'QA',
        origin: 'Sydney',
        destination: 'Melbourne',
        departure: '2020/10/10 21:00',
        arrival: '2020/10/11 00:00',
        stopovers: ['Perth', 'Darwin']
      },
      cost: 500
    },
    {
      flight: {
        airlineName: 'QANTAS',
        airlineCode: 'QA',
        origin: 'Melbourne',
        destination: 'Sydney',
        departure: '2020/10/10 09:00',
        arrival: '2020/10/10 15:00',
        stopovers: ['Perth', 'Darwin']
      },
      returnFlight: {
        airlineName: 'QANTAS',
        airlineCode: 'QA',
        origin: 'Sydney',
        destination: 'Melbourne',
        departure: '2020/10/10 21:00',
        arrival: '2020/10/11 00:00',
        stopovers: ['Perth', 'Darwin']
      },
      cost: 500
    },
    {
      flight: {
        airlineName: 'QANTAS',
        airlineCode: 'QA',
        origin: 'Melbourne',
        destination: 'Sydney',
        departure: '2020/10/10 09:00',
        arrival: '2020/10/10 15:00',
        stopovers: ['Perth', 'Darwin']
      },
      returnFlight: {
        airlineName: 'QANTAS',
        airlineCode: 'QA',
        origin: 'Sydney',
        destination: 'Melbourne',
        departure: '2020/10/10 21:00',
        arrival: '2020/10/11 00:00',
        stopovers: ['Perth', 'Darwin']
      },
      cost: 500
    },
    {
      flight: {
        airlineName: 'QANTAS',
        airlineCode: 'QA',
        origin: 'Melbourne',
        destination: 'Sydney',
        departure: '2020/10/10 09:00',
        arrival: '2020/10/10 15:00',
        stopovers: ['Perth', 'Darwin']
      },
      returnFlight: {
        airlineName: 'QANTAS',
        airlineCode: 'QA',
        origin: 'Sydney',
        destination: 'Melbourne',
        departure: '2020/10/10 21:00',
        arrival: '2020/10/11 00:00',
        stopovers: ['Perth', 'Darwin']
      },
      cost: 500
    }
  ]

  return (
    <main className='flex flex-col items-start w-full h-screen'>
      <NavBar />
      <section className='flex flex-col items-stretch self-center justify-start w-full h-full mt-8 md:max-w-screen-md gap-10'>
        <FlightSearch />
        {/* <FlightFilter /> */}
        <section className='flex flex-col items-end justify-center gap-5'>
          {flights.map((flight) => (
            <FlightCard flight={flight} />
          ))}
        </section>
      </section>
    </main>
  )
}

export default FlightListings
