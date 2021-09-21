/* eslint-disable react/forbid-prop-types */
import moment from 'moment'
import { arrayOf, number, shape, string } from 'prop-types'
import React from 'react'

const FlightCard = ({ flight }) => (
  <section className='flex flex-col w-full sm:flex-row'>
    {/* -------------------------------------------------------------------------- */
    /*                               Flight Details                               */
    /* -------------------------------------------------------------------------- */}
    <section className='flex flex-col justify-center flex-grow gap-3 p-4 shadow-md bg-yellow-50 rounded-t-xl sm:rounded-l-xl sm:rounded-r-none'>
      {/* Flight 1 */}
      <section className='flex items-center justify-between flex-grow w-full gap-5'>
        {/* Airline Code */}
        <div className='p-1 font-light text-white bg-yellow-500 rounded-3xl'>
          {flight.flight.airlineCode}
        </div>

        <section className='grid items-center justify-center grid-cols-3 gap-3 min-w-max'>
          {/* Origin Airport and Departure Time */}
          <section className='flex flex-col items-end'>
            <div className='text-xl font-medium'>
              {moment(flight.flight.departure).format('HH:mm')}
            </div>
            <div className='font-medium text-yellow-700'>
              {flight.flight.origin}
            </div>
          </section>

          {/* Stopovers */}
          <section className='flex flex-col'>
            <div className='flex justify-center gap-2 px-2'>
              {flight.flight.stopovers.map((stopover) => (
                <div className='relative'>
                  <header>{stopover}</header>
                  <span className='absolute w-2 h-2 bg-red-400 top-full left-1/2 rounded-xl' />
                </div>
              ))}
            </div>
            <div className='self-center w-5/6 h-px mt-1 bg-black bg-opacity-30' />
          </section>

          {/* Destination Airport and Arrival Time */}
          <section className='flex flex-col items-start'>
            <div className='text-xl font-medium'>
              {moment(flight.flight.arrival).format('HH:mm')}
            </div>
            <div className='font-medium text-yellow-700'>
              {flight.flight.destination}
            </div>
          </section>
        </section>
      </section>

      {flight.returnFlight && (
        <hr className='border-yellow-900 border-opacity-10' />
      )}

      {/* Flight 2 (if Return Flight) */}
      {flight.returnFlight && (
        <section className='flex items-center justify-between flex-grow w-full gap-5'>
          {/* Airline Code */}
          <div className='p-1 font-light text-white bg-yellow-500 rounded-3xl'>
            {flight.returnFlight.airlineCode}
          </div>

          <section className='grid items-center justify-center grid-cols-3 gap-3 min-w-max'>
            {/* Origin Airport and Departure Time */}
            <section className='flex flex-col items-end'>
              <div className='text-xl font-medium'>
                {moment(flight.returnFlight.departure).format(
                  'HH:mm'
                )}
              </div>
              <div className='font-medium text-yellow-700'>
                {flight.returnFlight.origin}
              </div>
            </section>

            {/* Stopovers */}
            <section className='flex flex-col'>
              <div className='flex justify-center gap-2 px-2'>
                {flight.returnFlight.stopovers.map((stopover) => (
                  <div className='relative'>
                    <header>{stopover}</header>
                    <span className='absolute w-2 h-2 bg-red-400 top-full left-1/2 rounded-xl' />
                  </div>
                ))}
              </div>
              <div className='self-center w-5/6 h-px mt-1 bg-black bg-opacity-30' />
            </section>

            {/* Destination Airport and Arrival Time */}
            <section className='flex flex-col items-start'>
              <div className='text-xl font-medium'>
                {moment(flight.returnFlight.arrival).format('HH:mm')}
              </div>
              <div className='font-medium text-yellow-700'>
                {flight.returnFlight.destination}
              </div>
            </section>
          </section>
        </section>
      )}
    </section>

    {/* Ticket Decoration */}
    <div className='relative w-full h-2 bg-yellow-50 sm:h-full sm:w-2'>
      <div className='absolute top-0 right-0 z-10 w-1 h-2 bg-white sm:w-2 sm:h-1 rounded-l-md sm:rounded-t-none sm:rounded-b-md' />
      <div className='absolute w-full h-px bg-yellow-700 bg-opacity-25 top-1/2 sm:w-px sm:h-full sm:top-0 sm:left-1/2' />
      <div className='absolute z-10 w-1 h-2 bg-white sm:w-2 sm:h-1 sm:bottom-0 rounded-r-md sm:rounded-b-none sm:rounded-t-md' />
    </div>

    {/* -------------------------------------------------------------------------- */
    /*                         Flight Cost and Book Button                        */
    /* -------------------------------------------------------------------------- */}
    <section className='flex items-center justify-between gap-4 p-4 shadow-md sm:flex-col bg-yellow-50 rounded-b-xl sm:rounded-r-xl sm:rounded-l-none'>
      <div className='text-lg font-medium'>${flight.cost}</div>
      <div>
        <button
          type='button'
          className='px-3 py-1 text-lg font-bold text-white transition-colors bg-yellow-600 hover:bg-yellow-500 rounded-xl'
        >
          Book
        </button>
      </div>
    </section>
  </section>
)

FlightCard.propTypes = {
  flight: shape({
    flight: shape({
      airline: string,
      origin: string,

      destination: string,
      departure: string,
      arrival: string,
      stopovers: arrayOf(string) || []
    }),
    returnFlight: shape({
      airline: string,
      origin: string,
      destination: string,
      departure: string,
      arrival: string,
      stopovers: arrayOf(string) || []
    }),
    cost: number
  }).isRequired
}

export default FlightCard
