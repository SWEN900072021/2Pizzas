/* eslint-disable react/forbid-prop-types */
import moment from 'moment'
import {
  arrayOf,
  bool,
  func,
  number,
  shape,
  string
} from 'prop-types'
import React from 'react'

const FlightCard = ({ flight, selected, selectFlight }) => {
  const handleClick = () => {
    selectFlight(flight)
  }

  return (
    // eslint-disable-next-line jsx-a11y/click-events-have-key-events
    <section
      role='button'
      tabIndex={flight.id}
      onClick={handleClick}
      className={`${
        selected
          ? 'bg-yellow-100 border-2 border-yellow-600'
          : 'bg-yellow-50 '
      } flex flex-col w-full max-w-lg sm:flex-row rounded-xl`}
    >
      {/* -------------------------------------------------------------------------- */
      /*                               Flight Details                               */
      /* -------------------------------------------------------------------------- */}
      <section className='flex flex-row items-center justify-center flex-grow gap-3 p-4 shadow-md rounded-t-xl sm:rounded-l-xl sm:rounded-r-none'>
        {/* Flight Code */}
        <div className='p-1 px-2 font-semibold text-white bg-yellow-500 rounded-3xl'>
          {flight.code}
        </div>
        <section className='grid items-center justify-center grid-flow-col gap-3 min-w-max'>
          {/* Origin Airport and Departure Time */}
          <section className='flex flex-col items-end'>
            <div className='text-sm font-light'>
              {moment(flight.departureLocal).format('DD/MM/YYYY')}
            </div>
            <div className='text-xl font-medium'>
              {moment(flight.departureLocal).format('hh:mmA')}
            </div>
            <div className='font-medium text-yellow-700'>
              {flight.origin.code}
            </div>
          </section>

          {/* Stopovers */}
          <section className='flex flex-col'>
            <div className='flex justify-center gap-2 px-2'>
              {flight.stopovers &&
                flight.stopovers.map((stopover) => (
                  <div className='relative'>
                    <header>{stopover.name}</header>
                    <span className='absolute w-2 h-2 bg-red-400 top-full left-1/2 rounded-xl' />
                  </div>
                ))}
            </div>
            <div className='self-center w-5/6 h-px mt-1 bg-black bg-opacity-30' />
          </section>

          {/* Destination Airport and Arrival Time */}
          <section className='flex flex-col items-start'>
            <div className='text-sm font-light'>
              {moment(flight.arrivalLocal).format('DD/MM/YYYY')}
            </div>
            <div className='text-xl font-medium'>
              {moment(flight.arrivalLocal).format('hh:mmA')}
            </div>
            <div className='font-medium text-yellow-700'>
              {flight.destination.code}
            </div>
          </section>
        </section>
      </section>

      {/* Ticket Decoration */}
      {/* <div className='relative w-full h-2 bg-yellow-50 sm:h-full sm:w-2'>
        <div className='absolute top-0 right-0 z-10 w-1 h-2 bg-white sm:w-2 sm:h-1 rounded-l-md sm:rounded-t-none sm:rounded-b-md' />
        <div className='absolute w-full h-px bg-yellow-700 bg-opacity-25 top-1/2 sm:w-px sm:h-full sm:top-0 sm:left-1/2' />
        <div className='absolute z-10 w-1 h-2 bg-white sm:w-2 sm:h-1 sm:bottom-0 rounded-r-md sm:rounded-b-none sm:rounded-t-md' />
      </div> */}

      {/* -------------------------------------------------------------------------- */
      /*                         Flight Cost and Book Button                        */
      /* -------------------------------------------------------------------------- */}
      <section
        className={`${
          !selected && 'shadow-md'
        } flex items-center justify-between gap-4 p-4 sm:flex-col bg-yellow-50 rounded-b-xl sm:rounded-r-xl sm:rounded-l-none`}
      >
        <div className='font-medium text-med'>
          Starting from ${flight.economyClassCost} per passenger
        </div>
      </section>
    </section>
  )
}

FlightCard.propTypes = {
  flight: shape({
    id: string || number,
    airline: string,
    origin: string,
    destination: string,
    departure: string,
    arrival: string,
    stopovers: arrayOf(string) || [],
    firstClassCost: number,
    businessClassCost: number,
    economyClassCost: number
  }).isRequired,
  selected: bool.isRequired,
  selectFlight: func.isRequired
}

export default FlightCard
