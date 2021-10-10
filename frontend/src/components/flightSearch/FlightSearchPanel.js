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

import { useFlightStore } from '../../hooks/Store'
import FlightForm from './FlightForm'

const FlightSearchPanel = () => {
  const originAirport = useFlightStore((state) => state.originAirport)
  const destinationAirport = useFlightStore(
    (state) => state.destinationAirport
  )
  const passengerCount = useFlightStore(
    (state) => state.passengerCount
  )

  const isReturn = useFlightStore((state) => state.isReturn)

  /* -------------------------------------------------------------------------- */

  const departDate = useFlightStore((state) => state.departDate)
  const returnDate = useFlightStore((state) => state.returnDate)
  const setDepartDate = useFlightStore((state) => state.setDepartDate)
  const setReturnDate = useFlightStore((state) => state.setReturnDate)
  const [departureOpen, setDepartureOpen] = useState(false)
  const [returnOpen, setReturnOpen] = useState(false)

  const disabledDates = (current) => current < moment().startOf('day')

  const handleDepartDateChange = (date) => {
    if (date < moment().startOf('day')) return

    if (date > moment(returnDate).startOf('day')) {
      setReturnDate(date)
    }
    setDepartDate(date)
  }

  const handleReturnDateChange = (date) => {
    if (date < moment().startOf('day')) return

    if (date < moment(departDate).startOf('day')) {
      setDepartDate(date)
    }
    setReturnDate(date)
  }

  /* -------------------------------------------------------------------------- */

  const [searchOpen, setSearchOpen] = useState(false)

  /* -------------------------------------------------------------------------- */

  useEffect(() => {
    // update departure and return dates
    // requery flights
  }, [])

  return (
    <section className='flex flex-col items-center justify-center p-2 py-3 text-white bg-yellow-600 rounded-none max-h-max sm:p-3 md:p-4 md:rounded-md'>
      {/* -------------------------------------------------------------------------- */
      /*                 Flight Description with Simple Date Pickers                */
      /* -------------------------------------------------------------------------- */}
      <section className='flex items-center w-full gap-3 md:gap-5 justify-evenly md:justify-stretch'>
        {/* Search Button for Devices Larger than Tablets */}
        <button
          type='button'
          data-cy='flight-form-toggle-button'
          onClick={() => setSearchOpen(!searchOpen)}
          className='hidden md:block bg-yellow-500 rounded-3xl p-3.5 hover:bg-yellow-400 transition-colors focus:outline-none focus:ring-2 focus:ring-yellow-500'
        >
          <FaSearch />
        </button>

        <section className='flex flex-col justify-between flex-grow-0 md:flex-row md:flex-grow'>
          {/* -------------- Depart and Return Locations and Date Pickers -------------- */}
          <div className='flex items-center gap-5'>
            {/* Depart Location and Date */}
            <section className='flex flex-col justify-center'>
              <div
                data-cy='origin-airport'
                className='flex items-center self-center justify-center gap-2 font-black sm:font-bold text-md sm:text-lg'
              >
                <FaPlaneDeparture />
                {originAirport.location} ({originAirport.code})
              </div>
              <div className='flex items-center self-center justify-center'>
                <button
                  data-cy='depart-date-prev-button'
                  type='button'
                  onClick={() =>
                    handleDepartDateChange(
                      moment(departDate).subtract(1, 'day')
                    )
                  }
                >
                  <FaChevronLeft />
                </button>

                <span className='flex justify-center pr-5 transition-colors w-36 hover:bg-yellow-500 rounded-2xl'>
                  <DatePicker
                    disabledDate={disabledDates}
                    value={moment(departDate)}
                    allowClear={false}
                    open={departureOpen}
                    onOpenChange={(open) => {
                      setDepartureOpen(open)
                    }}
                    style={{ visibility: 'hidden', width: 0 }}
                    onChange={(date) => {
                      handleDepartDateChange(date)
                    }}
                  />
                  <button
                    data-cy='depart-date-picker-button'
                    type='button'
                    onClick={() => {
                      setDepartureOpen(!departureOpen)
                    }}
                    className='font-bold'
                  >
                    {moment(departDate).format('ddd, DD MMM')}
                  </button>
                </span>

                <button
                  data-cy='depart-date-next-button'
                  type='button'
                  onClick={() =>
                    handleDepartDateChange(
                      moment(departDate).add(1, 'day')
                    )
                  }
                >
                  <FaChevronRight />
                </button>
              </div>
            </section>
            {/* Return Location and Date */}
            <section className='flex flex-col self-start min-h-full'>
              <div
                data-cy='destination-airport'
                className='flex items-center self-center justify-center gap-2 font-black sm:font-bold text-md sm:text-lg'
              >
                <FaPlaneArrival />
                {destinationAirport.location} (
                {destinationAirport.code})
              </div>
              <div
                className={`${
                  !isReturn && 'hidden'
                } flex items-center self-center justify-center`}
              >
                <button
                  data-cy='return-date-prev-button'
                  type='button'
                  onClick={() =>
                    handleReturnDateChange(
                      moment(returnDate).subtract(1, 'day')
                    )
                  }
                >
                  <FaChevronLeft />
                </button>

                <span className='flex justify-center pr-5 transition-colors w-36 hover:bg-yellow-500 rounded-2xl'>
                  <DatePicker
                    disabledDate={disabledDates}
                    value={moment(returnDate)}
                    allowClear={false}
                    open={returnOpen}
                    onOpenChange={(open) => {
                      setReturnOpen(open)
                    }}
                    style={{ visibility: 'hidden', width: 0 }}
                    onChange={(date) => {
                      handleReturnDateChange(date)
                    }}
                  />
                  <button
                    data-cy='return-date-picker-button'
                    type='button'
                    onClick={() => {
                      setReturnOpen(!returnOpen)
                    }}
                    className='font-bold'
                  >
                    {moment(returnDate).format('ddd, DD MMM')}
                  </button>
                </span>

                <button
                  data-cy='return-date-next-button'
                  type='button'
                  onClick={() =>
                    handleReturnDateChange(
                      moment(returnDate).add(1, 'day')
                    )
                  }
                >
                  <FaChevronRight />
                </button>
              </div>
            </section>
          </div>

          {/* ----------------------------- Passenger Count ---------------------------- */}
          <div className='flex self-center font-medium sm:self-start'>
            {passengerCount} passenger(s)
          </div>
        </section>

        {/* Search Button for Mobile/Tablet Devices */}
        <button
          type='button'
          data-cy='mobile-flight-form-toggle-button'
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
        } mt-5 mb-3 md:my-0 md:mt-3`}
      >
        <FlightForm />
      </section>
    </section>
  )
}

export default FlightSearchPanel
