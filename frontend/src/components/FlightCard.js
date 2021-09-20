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
    <div className='flex flex-col p-4 border-2 border-gray-400 bg-green-50 rounded-xl gap-5'>
      <div className='flex items-center justify-center gap-5'>
        <div>{flight.flight.airlineCode}</div>
        <div className='flex flex-col items-center gap-5'>
          <div>{flight.flight.origin}</div>
          <div>{flight.flight.departure}</div>
        </div>
        <div className='flex flex-col self-start gap-5'>
          <div className='flex justify-center gap-5'>
            {flight.flight.stopovers.map((stopover) => (
              <div>{stopover}</div>
            ))}
          </div>
          <div className='self-center'>----------&gt;</div>
        </div>
        <div className='flex flex-col items-center gap-5'>
          <div>{flight.flight.destination}</div>
          <div>{flight.flight.arrival}</div>
        </div>
      </div>
      <hr className='border-gray-400' />
      <div className='flex items-center justify-center gap-5'>
        <div>{flight.returnFlight.airlineCode}</div>
        <div className='flex flex-col items-center gap-5'>
          <div>{flight.returnFlight.origin}</div>
          <div>{flight.returnFlight.departure}</div>
        </div>
        <div className='flex flex-col self-start gap-5'>
          <div className='flex justify-center gap-5'>
            {flight.returnFlight.stopovers.map((stopover) => (
              <div>{stopover}</div>
            ))}
          </div>
          <div className='self-center'>----------&gt;</div>
        </div>
        <div className='flex flex-col items-center gap-5'>
          <div>{flight.returnFlight.destination}</div>
          <div>{flight.returnFlight.arrival}</div>
        </div>
      </div>
    </div>
    <div className='flex items-center p-4 border-2 border-gray-400 bg-green-50 rounded-xl'>
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
