import React, { useEffect, useState } from 'react'
import Button from '../components/Button'

import FlightCard from '../components/FlightCard'
// import FlightFilter from '../components/FlightFilter'
import FlightSearch from '../components/FlightSearch'
import NavBar from '../components/NavBar'
import {
  useFlightStore,
  useTestDataStore,
  useBookingStore
} from '../hooks/Store'

const FlightListings = () => {
  const returnFlights = useTestDataStore(
    (state) => state.returnFlights
  )
  const oneWayFlights = useTestDataStore(
    (state) => state.oneWayFlights
  )

  const isReturn = useFlightStore((state) => state.isReturn)
  const setReturn = useFlightStore((state) => state.setReturn)
  const [flights, setFlights] = useState(
    isReturn ? returnFlights : oneWayFlights
  )

  const outboundFlight = useBookingStore(
    (state) => state.selectedOutboundFlight
  )
  // const returnFlight = useBookingStore(
  //   (state) => state.selectedReturnFlight
  // )
  const setOutboundFlight = useBookingStore(
    (state) => state.setSelectedOutboundFlight
  )
  // const setReturnFlight = useBookingStore(
  //   (state) => state.setSelectedReturnFlight
  // )

  const flightType = [{ name: 'Outbound' }, { name: 'Return' }]

  const handleSelectOutboundFlight = (flight) => {
    setOutboundFlight(flight)
  }

  useEffect(() => {
    if (isReturn) {
      setFlights(returnFlights)
    } else {
      setFlights(oneWayFlights)
    }
  }, [isReturn, returnFlights, oneWayFlights])

  return (
    <main className='flex flex-col items-start w-full h-screen'>
      <NavBar />
      <section className='flex flex-col self-center justify-start w-full h-full gap-10 mt-8 md:max-w-screen-md'>
        <FlightSearch />
        {/* <FlightFilter /> */}
        <div className='flex flex-col w-full items-center gap-3'>
          {/* <FlightSidebar /> */}
          <div className='flex justify-center items-center gap-2 sm:hidden'>
            <Button
              className={
                !isReturn &&
                'bg-yellow-600 border-2 border-yellow-900'
              }
              label={flightType[0].name}
              onClick={() => setReturn(false)}
            />
            <Button
              className={
                isReturn && 'bg-yellow-600 border-2 border-yellow-900'
              }
              label={flightType[1].name}
              onClick={() => setReturn(true)}
            />
          </div>
          <div className='grid self-center justify-center grid-flow-col sm:justify-end auto-cols-min '>
            <section
              className={`${
                !isReturn ? 'flex' : 'hidden'
              } sm:flex flex-col items-center justify-center gap-5 sm:items-end`}
            >
              {flights.map((flight) => (
                <div className='flex items-center justify-center gap-5 sm:items-end'>
                  <FlightCard
                    flight={flight}
                    selected={
                      outboundFlight
                        ? flight.id === outboundFlight.id
                        : false
                    }
                    selectFlight={handleSelectOutboundFlight}
                  />
                </div>
              ))}
            </section>
            <section
              className={`${
                isReturn ? 'flex' : 'hidden'
              } sm:flexflex flex-col items-center justify-center gap-5 sm:items-end`}
            >
              {flights.map((flight) => (
                <div className='flex items-center justify-center gap-5 sm:items-end'>
                  <FlightCard
                    flight={flight}
                    selected={
                      outboundFlight
                        ? flight.id === outboundFlight.id
                        : false
                    }
                    selectFlight={handleSelectOutboundFlight}
                  />
                </div>
              ))}
            </section>
          </div>
        </div>
      </section>
    </main>
  )
}

export default FlightListings
