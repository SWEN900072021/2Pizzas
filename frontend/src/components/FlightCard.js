/* eslint-disable react/forbid-prop-types */
import {
  arrayOf,
  instanceOf,
  number,
  shape,
  string
} from 'prop-types'
import React from 'react'

const FlightCard = ({ flight }) => (
  <section className='flex'>
    <div className='bg-green-50 p-4 rounded-xl border-2 border-gray-400 flex flex-col gap-5'>
      <div className='flex gap-5 justify-center items-center'>
        <div>{flight.flight.airlineCode}</div>
        <div className='flex flex-col gap-5 items-center'>
          <div>{flight.flight.origin}</div>
          <div>{flight.flight.departure}</div>
        </div>
        <div className='flex flex-col self-start gap-5'>
          <div className='flex gap-5 justify-center'>
            {flight.flight.stopovers.map((stopover) => (
              <div>{stopover}</div>
            ))}
          </div>
          <div className='self-center'>----------&gt;</div>
        </div>
        <div className='flex flex-col gap-5 items-center'>
          <div>{flight.flight.destination}</div>
          <div>{flight.flight.arrival}</div>
        </div>
      </div>
      <hr className='border-gray-400' />
      <div className='flex gap-5 justify-center items-center'>
        <div>{flight.returnFlight.airlineCode}</div>
        <div className='flex flex-col gap-5 items-center'>
          <div>{flight.returnFlight.origin}</div>
          <div>{flight.returnFlight.departure}</div>
        </div>
        <div className='flex flex-col self-start gap-5'>
          <div className='flex gap-5 justify-center'>
            {flight.returnFlight.stopovers.map((stopover) => (
              <div>{stopover}</div>
            ))}
          </div>
          <div className='self-center'>----------&gt;</div>
        </div>
        <div className='flex flex-col gap-5 items-center'>
          <div>{flight.returnFlight.destination}</div>
          <div>{flight.returnFlight.arrival}</div>
        </div>
      </div>
    </div>
    <div className='bg-green-50 p-4 rounded-xl border-2 border-gray-400 flex items-center'>
      ${flight.cost}
    </div>
  </section>
)

FlightCard.propTypes = {
  flight: shape({
    flight: shape({
      airlineName: string,
      airlineCode: string,
      origin: string,
      destination: string,
      departure: instanceOf(Date),
      arrival: instanceOf(Date),
      stopovers: arrayOf(string)
    }),
    returnFlight: shape({
      airlineName: string,
      airlineCode: string,
      origin: string,
      destination: string,
      departure: instanceOf(Date),
      arrival: instanceOf(Date),
      stopovers: arrayOf(string)
    }),
    cost: number
  }).isRequired
}

export default FlightCard
