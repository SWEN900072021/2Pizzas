import React from 'react'
import FlightCard from '../components/FlightCard'
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
    }
  ]
  return (
    <main className='h-screen flex justify-center items-center'>
      <NavBar />
      <div className='flex flex-col justify-center items-center gap-10'>
        {flights.map((flight) => (
          <FlightCard flight={flight} />
        ))}
      </div>
    </main>
  )
}

export default FlightListings
