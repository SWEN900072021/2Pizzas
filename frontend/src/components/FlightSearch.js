import React, { useEffect, useState } from 'react'
import { DatePicker } from 'antd'
import { FaSearch } from 'react-icons/fa'
import { BsChevronLeft, BsChevronRight } from 'react-icons/bs'

// import DatePicker from './DatePicker'
import { useStore } from '../hooks/Store'

const FlightSearch = () => {
  const origin = useStore((state) => state.origin)
  const destination = useStore((state) => state.destination)
  const passengerCount = 1
  const cabinClass = 'economy'

  const [open, setOpen] = useState(false)

  const dates = useStore((state) => state.dates)
  const [departureDate, setDepartureDate] = useState(dates[0])
  const [arrivalDate, setArrivalDate] = useState(dates[1])
  const setDepartureDateInStore = useStore(
    (state) => state.setDepartureDate
  )
  const setArrivalDateInStore = useStore(
    (state) => state.setArrivalDate
  )

  useEffect(() => {
    // update departure and arrival dates
    setDepartureDateInStore(departureDate)
    setArrivalDateInStore(arrivalDate)
    // requery flights
  }, [
    departureDate,
    arrivalDate,
    setDepartureDateInStore,
    setArrivalDateInStore
  ])

  return (
    <section className='flex p-4 rounded-xl text-white bg-yellow-500 gap-5 justify-center items-center'>
      <button
        type='button'
        className='bg-yellow-700 rounded-3xl p-3.5'
      >
        <FaSearch />
      </button>
      <div className='flex flex-col gap-2'>
        <div className='flex gap-5 items-center'>
          <div className='flex flex-col gap-2 justify-center'>
            <div className='self-center font-bold'>
              Origin: {origin}
            </div>
            <div className='self-center font-bold'>
              {/* <DatePicker
                date={departureDate}
                setDate={setDepartureDate}
              /> */}
              <button type='button'>
                <BsChevronLeft />
              </button>
              <button
                type='button'
                onClick={() => setOpen((openState) => !openState)}
              >
                {departureDate.toString()}
              </button>
              <DatePicker
                open={open}
                allowClear={false}
                value={departureDate}
                bordered={false}
                placeholder='Return date'
                onChange={(newDate) => {
                  setDepartureDate(newDate)
                }}
              />
              <button type='button'>
                <BsChevronRight />
              </button>
            </div>
          </div>
          <div className='flex flex-col gap-2 justify-center'>
            <div className='self-center font-bold'>
              Destination: {destination}
            </div>
            <div className='self-center font-bold'>
              <button type='button'>
                <BsChevronLeft />
              </button>
              <DatePicker
                allowClear={false}
                value={arrivalDate}
                bordered={false}
                placeholder='Return date'
                onChange={(newDate) => {
                  setArrivalDate(newDate)
                }}
              />
              <button type='button'>
                <BsChevronRight />
              </button>
            </div>
          </div>
        </div>
        <div className='flex gap-2'>
          <div>{passengerCount} passenger(s)</div>
          <div>|</div>
          <div>
            {cabinClass.charAt(0).toUpperCase() + cabinClass.slice(1)}
          </div>
        </div>
      </div>
    </section>
  )
}

export default FlightSearch
