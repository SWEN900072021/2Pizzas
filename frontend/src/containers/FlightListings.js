/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router'
// import { v4 as uuid } from 'uuid'

import FlightCard from '../components/FlightCard'
// import FlightFilter from '../components/FlightFilter'
import FlightSearch from '../components/FlightSearch'
import NavBar from '../components/NavBar'
import {
  useFlightStore,
  useTestDataStore,
  useSessionStore,
  useBookingStore
} from '../hooks/Store'

const FlightListings = () => {
  const history = useHistory()
  const token = useSessionStore((state) => state.token)
  const setCreatingBooking = useBookingStore(
    (state) => state.setCreatingBooking
  )
  const returnFlights = useTestDataStore(
    (state) => state.returnFlights
  )
  const oneWayFlights = useTestDataStore(
    (state) => state.oneWayFlights
  )

  const isReturn = useFlightStore((state) => state.isReturn)
  const [flights, setFlights] = useState(
    isReturn ? returnFlights : oneWayFlights
  )

  useEffect(() => {
    if (isReturn) {
      setFlights(returnFlights)
    } else {
      setFlights(oneWayFlights)
    }
  }, [isReturn, returnFlights, oneWayFlights])

  const submitHandler = () => {
    if (token) history.push('/booking/create')
    else {
      setCreatingBooking(true)
      history.push('/login')
    }
  }

  return (
    <main className='flex flex-col items-start w-full h-screen'>
      <NavBar />
      <section className='flex flex-col self-center justify-start w-full h-full gap-10 mt-8 md:max-w-screen-md'>
        <FlightSearch />
        {/* <FlightFilter /> */}
        {/* <div className='grid justify-center grid-flow-row sm:justify-end auto-cols-min md:px-0'>
          <section className='flex flex-col items-center justify-center gap-5 sm:items-end sm:px-3 md:px-0'>
            {flights.map((flight) => (
              <FlightCard flight={flight} />
            ))}
          </section>
        </div> */}
        <button type='button' onClick={submitHandler}>
          Submit
        </button>
      </section>
    </main>
  )
}

export default FlightListings
