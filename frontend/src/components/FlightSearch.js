import React, { useEffect, useState } from 'react'
import { DatePicker } from 'antd'
import {
  FaSearch,
  FaChevronLeft,
  FaChevronRight,
  FaPlaneArrival,
  FaPlaneDeparture
} from 'react-icons/fa'
import moment from 'moment'

// import DatePicker from './DatePicker'
import { useStore } from '../hooks/Store'
import FlightForm from '../containers/FlightForm'

const FlightSearch = () => {
  const originAirport = useStore((state) => state.originAirport)
  const destinationAirport = useStore(
    (state) => state.destinationAirport
  )
  const passengerCount = useStore((state) => state.passengerCount)
  const cabinClass = useStore((state) => state.cabinClass)

  const departDate = useStore((state) => state.departDate)
  const returnDate = useStore((state) => state.returnDate)
  const setDepartDate = useStore((state) => state.setDepartDate)
  const setReturnDate = useStore((state) => state.setReturnDate)
  const [departureOpen, setDepartureOpen] = useState(false)
  const [returnOpen, setReturnOpen] = useState(false)

  const [searchOpen, setSearchOpen] = useState(false)

  useEffect(() => {
    // update departure and return dates
    // requery flights
  }, [])

  return (
    <section className='flex flex-col items-center justify-center flex-grow p-2 py-3 text-white bg-yellow-600 rounded-none sm:p-3 md:p-4 md:rounded-md'>
      {/* -------------------------------------------------------------------------- */
      /*                 Flight Description with Simple Date Pickers                */
      /* -------------------------------------------------------------------------- */}
      <section className='flex items-center w-full gap-3 md:gap-5 justify-evenly md:justify-stretch'>
        {/* Search Button for Devices Larger than Tablets */}
        <button
          type='button'
          onClick={() => setSearchOpen(!searchOpen)}
          className='hidden md:block bg-yellow-500 rounded-3xl p-3.5 hover:bg-yellow-400 transition-colors focus:outline-none focus:ring-2 focus:ring-yellow-500'
        >
          <FaSearch />
        </button>

        {/* Depart and Return Locations and Dates */}
        <div className='flex flex-col justify-between flex-grow-0 gap-1 md:flex-row md:flex-grow'>
          <div className='flex items-center gap-5'>
            <section className='flex flex-col justify-center'>
              <div className='flex items-center self-center justify-center gap-2 text-lg font-bold'>
                <FaPlaneDeparture />
                {originAirport.location} ({originAirport.code})
              </div>
              <div className='flex items-center self-center justify-center'>
                <button type='button'>
                  <FaChevronLeft />
                </button>

                <span className='flex justify-center pr-5 transition-colors w-36 hover:bg-yellow-500 rounded-2xl'>
                  <DatePicker
                    allowClear={false}
                    open={departureOpen}
                    onOpenChange={(open) => {
                      setDepartureOpen(open)
                    }}
                    style={{ visibility: 'hidden', width: 0 }}
                    onChange={(date) => {
                      setDepartDate(date)
                    }}
                  />
                  <button
                    type='button'
                    onClick={() => {
                      setDepartureOpen(!departureOpen)
                    }}
                    className='font-bold'
                  >
                    {moment(departDate).format('ddd, DD MMM')}
                  </button>
                </span>

                <button type='button'>
                  <FaChevronRight />
                </button>
              </div>
            </section>

            <section className='flex flex-col justify-center'>
              <div className='flex items-center self-center justify-center gap-2 text-lg font-bold'>
                <FaPlaneArrival />
                {destinationAirport.location} (
                {destinationAirport.code})
              </div>
              <div className='flex items-center self-center justify-center'>
                <button type='button'>
                  <FaChevronLeft />
                </button>

                <span className='flex justify-center pr-5 transition-colors w-36 hover:bg-yellow-500 rounded-2xl'>
                  <DatePicker
                    allowClear={false}
                    open={returnOpen}
                    onOpenChange={(open) => {
                      setReturnOpen(open)
                    }}
                    style={{ visibility: 'hidden', width: 0 }}
                    onChange={(date) => {
                      setReturnDate(date)
                    }}
                  />
                  <button
                    type='button'
                    onClick={() => {
                      setReturnOpen(!returnOpen)
                    }}
                    className='font-bold'
                  >
                    {moment(returnDate).format('ddd, DD MMM')}
                  </button>
                </span>

                <button type='button'>
                  <FaChevronRight />
                </button>
              </div>
            </section>
          </div>
          <div className='flex gap-2 font-semibold'>
            <div>{passengerCount} passenger(s)</div>
            <div>•</div>
            <div>
              {cabinClass.charAt(0).toUpperCase() +
                cabinClass.slice(1)}
            </div>
          </div>
        </div>

        {/* Search Button for Mobile/Tablet Devices */}
        <button
          type='button'
          onClick={() => setSearchOpen(!searchOpen)}
          className='block md:hidden bg-yellow-500 rounded-3xl p-3.5 hover:bg-yellow-400 transition-colors focus:outline-none focus:ring-2 focus:ring-yellow-500'
        >
          <FaSearch />
        </button>
      </section>

      {/* -------------------------------------------------------------------------- */
      /*                       Open/Closed Flight Search Form                       */
      /* -------------------------------------------------------------------------- */}
      <section
        className={`${
          searchOpen ? 'block' : 'hidden'
        } my-3 md:my-0 md:mt-3`}
      >
        <FlightForm />
      </section>
    </section>
  )
}

export default FlightSearch
