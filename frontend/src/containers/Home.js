import { React, useEffect } from 'react'

// Containers and Components

import NavBar from '../components/common/NavBar'

// Hooks

// Assets
import landscapePicture from '../assets/home-landscape.png'
import FlightSearchForm from '../components/flightSearch/FlightSearchForm'
import { useFlightStore } from '../hooks/Store'

const Home = () => {
  const setOriginAirportSearchValue = useFlightStore(
    (state) => state.setOriginAirportSearchValue
  )
  const setDestinationAirportSearchValue = useFlightStore(
    (state) => state.setDestinationAirportSearchValue
  )
  const setOriginAirport = useFlightStore(
    (state) => state.setOriginAirport
  )
  const setDestinationAirport = useFlightStore(
    (state) => state.setDestinationAirport
  )

  useEffect(() => {
    setOriginAirportSearchValue('')
    setDestinationAirportSearchValue('')
    setOriginAirport({})
    setDestinationAirport({})
  }, [
    setOriginAirportSearchValue,
    setDestinationAirportSearchValue,
    setOriginAirport,
    setDestinationAirport
  ])

  return (
    <main className='flex flex-col h-screen'>
      <NavBar />

      {/* -------------------------------------------------------------------------- */
      /*                                  Jumbotron                                 */
      /* -------------------------------------------------------------------------- */}
      <section className='flex flex-col items-center justify-start flex-grow h-full gap-5'>
        <img
          draggable={false}
          src={landscapePicture}
          alt='Landscape with hot air balloons'
          className='relative object-cover object-left w-full h-5/6 md:h-3/4 filter contrast-75'
        />
        <div className='absolute self-center mx-6 transform translate-y-36 md:translate-y-32 '>
          <h2 className='text-3xl font-bold text-center text-white select-none sm:text-left md:text-5xl'>
            Search hundreds of flights
          </h2>
        </div>
        {/* ------------------------------------------------------------------------- */
        /*                           Main Flight Search Form                          */
        /* -------------------------------------------------------------------------- */}
        <section className='absolute flex flex-col flex-wrap items-center justify-center flex-grow p-5 bg-yellow-50 mt-52 max-w-max'>
          <FlightSearchForm />
        </section>
      </section>
    </main>
  )
}

export default Home
