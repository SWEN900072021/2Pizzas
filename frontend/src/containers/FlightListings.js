import React, { useState } from 'react'
import { useHistory } from 'react-router'
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

  /* -------------------------------------------------------------------------- */

  const isReturn = useFlightStore((state) => state.isReturn)

  const outboundFlight = useBookingStore(
    (state) => state.selectedOutboundFlight
  )
  const returnFlight = useBookingStore(
    (state) => state.selectedReturnFlight
  )
  const setOutboundFlight = useBookingStore(
    (state) => state.setSelectedOutboundFlight
  )
  const setReturnFlight = useBookingStore(
    (state) => state.setSelectedReturnFlight
  )

  const flightType = [{ name: 'Outbound' }, { name: 'Return' }]
  const [flightToggle, setFlightToggle] = useState(flightType[0])

  const handleSelectOutboundFlight = (flight) => {
    setOutboundFlight(flight)
  }

  const handleSelectReturnFlight = (flight) => {
    setReturnFlight(flight)
  }

  /* -------------------------------------------------------------------------- */

  const history = useHistory()

  const handleSubmit = (e) => {
    e.preventDefault()
    history.push('/booking/create')
  }

  return (
    <main className='flex flex-col items-start w-full h-screen'>
      <NavBar />
      <section className='flex flex-col self-center justify-start w-full h-full gap-10 mt-8 md:max-w-screen-md'>
        <FlightSearch />
        {/* <FlightFilter /> */}
        <div className='flex flex-col items-center w-full gap-3'>
          {/* <FlightSidebar /> */}
          {isReturn && (
            <div className='flex items-center justify-center gap-2 md:hidden'>
              <Button
                className={
                  flightToggle.name === flightType[0].name &&
                  'bg-yellow-600 border-2 border-yellow-900'
                }
                label={flightType[0].name}
                onClick={() => setFlightToggle(flightType[0])}
              />
              <Button
                className={
                  flightToggle.name === flightType[1].name &&
                  'bg-yellow-600 border-2 border-yellow-900'
                }
                label={flightType[1].name}
                onClick={() => setFlightToggle(flightType[1])}
              />
            </div>
          )}
          <div className='grid items-start self-center justify-center grid-flow-col gap-4 md:justify-end auto-cols-min'>
            <section
              className={`${
                flightToggle.name === flightType[0].name
                  ? 'flex'
                  : 'hidden'
              } md:flex flex-col items-center justify-center gap-5 md:items-end`}
            >
              {oneWayFlights.map((flight) => (
                <div className='flex items-center justify-center gap-5 md:items-end'>
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
            {isReturn && (
              <span className='hidden w-px h-full bg-black bg-opacity-60 md:block' />
            )}
            {isReturn && (
              <section
                className={`${
                  flightToggle.name === flightType[1].name
                    ? 'flex'
                    : 'hidden'
                } md:flex flex-col items-center justify-center gap-5 md:items-end`}
              >
                {returnFlights.map((flight) => (
                  <div className='flex items-center justify-center gap-5 md:items-end'>
                    <FlightCard
                      flight={flight}
                      selected={
                        returnFlight
                          ? flight.id === returnFlight.id
                          : false
                      }
                      selectFlight={handleSelectReturnFlight}
                    />
                  </div>
                ))}
              </section>
            )}
          </div>
          <Button
            label='Submit'
            onClick={handleSubmit}
            disabled={
              isReturn
                ? !(outboundFlight && returnFlight)
                : !outboundFlight
            }
          />
        </div>
      </section>
    </main>
  )
}

export default FlightListings
