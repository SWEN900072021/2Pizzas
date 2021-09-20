import React, { useState } from 'react'
import {
  FiArrowRight,
  FiChevronDown,
  FiMinusCircle,
  FiPlusCircle
} from 'react-icons/fi'
import { IoIosPerson } from 'react-icons/io'
import { DatePicker, Popover } from 'antd'
import { useMediaQuery } from 'react-responsive'
import moment from 'moment'
import { useHistory } from 'react-router-dom'
import OriginSearch from './OriginSearch'
import DestinationSearch from './DestinationSearch'
import Button from '../components/Button'
import Search from '../components/Search'
import { useStore, useTestDataStore } from '../hooks/Store'

const { RangePicker } = DatePicker

const FlightForm = () => {
  const history = useHistory()
  const airports = useTestDataStore((state) => state.airports)

  const originAirport = useStore((state) => state.originAirport)
  const destinationAirport = useStore(
    (state) => state.destinationAirport
  )

  const departDate = useStore((state) => state.departDate)
  const returnDate = useStore((state) => state.returnDate)
  const setDepartDate = useStore((state) => state.setDepartDate)
  const setReturnDate = useStore((state) => state.setReturnDate)

  const [dates, setDates] = useState([
    moment(departDate),
    moment(returnDate)
  ])

  const isReturn = useStore((state) => state.return)
  const setReturn = useStore((state) => state.setReturn)

  /* -------------------------------------------------------------------------- */

  // Used to determine which elements to hide and show depending on the breakpoint
  const isMobile = useMediaQuery({ query: '(max-width: 640px)' })
  const isTablet = useMediaQuery({ query: '(min-width: 768px)' })

  /* -------------------------------------------------------------------------- */

  const [visible, setVisible] = useState(false)

  const handleVisibleChange = (visibleState) => {
    setVisible(visibleState)
  }

  /* -------------------------------------------------------------------------- */

  const disabledDepartDates = (current) =>
    current < moment().startOf('day')
  const disabledReturnDates = (current) =>
    current < moment(departDate).startOf('day')

  /* -------------------------------------------------------------------------- */

  const ECONOMY = useStore((state) => state.economyClass)
  const BUSINESS = useStore((state) => state.businessClass)
  const FIRST = useStore((state) => state.firstClass)

  const cabinClass = useStore((state) => state.cabinClass)
  const setEconomyClass = useStore((state) => state.setEconomyClass)
  const setBusinessClass = useStore((state) => state.setBusinessClass)
  const setFirstClass = useStore((state) => state.setFirstClass)

  const passengerCount = useStore((state) => state.passengerCount)
  const addPassenger = useStore((state) => state.addPassenger)
  const removePassenger = useStore((state) => state.removePassenger)

  const cabinClassPassengersPopover = (
    <section className='flex flex-col items-start justify-center gap-3 font-semibold'>
      {/* --------------------------- Cabin Class Buttons -------------------------- */}
      <span>
        <h4>Cabin Class</h4>
      </span>
      <section className='grid grid-cols-3 gap-0.5'>
        <button
          type='button'
          onClick={setEconomyClass}
          className={`${
            cabinClass === ECONOMY
              ? 'w-20 bg-yellow-600 text-xs font-medium text-white p-2 ring-1 ring-yellow-400'
              : 'w-20 bg-white text-xs font-medium p-2 border-2'
          } focus:outline-none focus:ring-2 focus:ring-yellow-400`}
        >
          Economy
        </button>
        <button
          type='button'
          onClick={setBusinessClass}
          className={`${
            cabinClass === BUSINESS
              ? 'w-20 bg-yellow-600 text-xs font-medium text-white p-2 ring-1 ring-yellow-400'
              : 'w-20 bg-white text-xs font-medium p-2 border-2'
          } focus:outline-none focus:ring-2 focus:ring-yellow-400`}
        >
          Business
        </button>
        <button
          type='button'
          onClick={setFirstClass}
          className={`${
            cabinClass === FIRST
              ? 'w-20 bg-yellow-600 text-xs font-medium text-white p-2 ring-1 ring-yellow-400'
              : 'w-20 bg-white text-xs font-medium p-2 border-2'
          } focus:outline-none focus:ring-2 focus:ring-yellow-400`}
        >
          First
        </button>
      </section>

      {/* ----------------------------- Passenger Count ---------------------------- */}
      <span>
        <h4>Number of Passengers</h4>
      </span>
      <span className='flex items-center justify-center gap-4'>
        <button
          type='button'
          onClick={removePassenger}
          className='text-yellow-500 rounded-xl focus:outline-none focus:ring-2 focus:ring-yellow-400'
        >
          <FiMinusCircle />
        </button>
        <span className='text-black'>{passengerCount}</span>
        <button
          type='button'
          onClick={addPassenger}
          className='text-yellow-500 rounded-xl focus:outline-none focus:ring-2 focus:ring-yellow-400'
        >
          <FiPlusCircle />
        </button>
      </span>
    </section>
  )

  /* -------------------------------------------------------------------------- */

  const handleSubmit = (e) => {
    e.preventDefault()
    // Search for flights here
    if (originAirport.code && destinationAirport.code) {
      history.push('/flight/results')
    }
  }

  return (
    <section className='flex flex-col flex-wrap items-center justify-center flex-grow gap-3 text-black max-w-max'>
      {/* 
            Section 1: 
              - Origin/Destination Airport Search fields
              - Date Pickers 
          */}
      <section
        className={`grid w-full items-center justify-stretch gap-2 ${
          !isTablet ? ' grid-rows-3' : 'grid-cols-3'
        }`}
      >
        {/* Airport Search */}
        <OriginSearch airports={airports} />
        <DestinationSearch airports={airports} />

        {/* (Date Pickers) Visible only for Mobile Devices or One-Way Flights */}
        <span
          className={`${
            isReturn && (!isMobile || !isReturn) && 'hidden'
          }
                  grid grid-cols-2
                  border border-bg-grey
                  font-light tracking-wide text-gray-800 
                  placeholder-gray-500 focus:placeholder-gray-400
                  focus:outline-none focus:ring-2 focus:ring-yellow-400 
                  bg-white rounded-lg py-1 md:py-2`}
        >
          <DatePicker
            disabledDate={disabledDepartDates}
            allowClear={false}
            value={dates && dates[0] ? dates[0] : moment()}
            bordered={false}
            suffixIcon={<FiArrowRight />}
            placeholder='Departure date'
            onChange={(date) => {
              setDates((oldDates) => {
                const oldReturnDate = oldDates[1]
                // If new departure date is beyond the return date,
                // set return date to same date as new departure date

                setDepartDate(date)
                if (date > oldReturnDate.startOf('day')) {
                  setReturnDate(date)
                } else {
                  setReturnDate(oldReturnDate)
                }

                return date > oldReturnDate.startOf('day')
                  ? [date, date]
                  : [date, oldReturnDate]
              })
            }}
          />
          <DatePicker
            disabledDate={disabledReturnDates}
            allowClear={false}
            value={dates && dates[1] ? dates[1] : moment()}
            bordered={false}
            separator={<FiArrowRight />}
            placeholder='Return date'
            disabled={!isReturn}
            className='-ml-4'
            onChange={(date) => {
              setDates((oldDates) => {
                const oldDepartDate = oldDates[0]
                setReturnDate(date)
                return [oldDepartDate, date]
              })
            }}
          />
        </span>

        {/* (Date Range Picker) visible only for Return Flights or Non-Mobile Devices */}
        <span
          className={`${(!isReturn || isMobile) && 'hidden'}
                border border-bg-grey
                font-light tracking-wide text-gray-800 
                placeholder-gray-500 focus:placeholder-gray-400
                focus:outline-none focus:ring-2 focus:ring-yellow-400 
                bg-white rounded-lg py-1 md:py-2`}
        >
          <RangePicker
            value={dates}
            allowClear={false}
            inputReadOnly
            separator={<FiArrowRight className='text-gray-400' />}
            bordered={false}
            disabledDate={disabledDepartDates}
            disabled={[false, !isReturn]}
            placeholder={['Departure date', 'Return date']}
            onChange={(newDates) => {
              setDates(newDates)
            }}
          />
        </span>
      </section>

      {/* 
            Section 2: 
              - Return/One-Way Buttons, 
              - Cabin Class and Passenger Select, 
              - Find Flights Button 
          */}
      <section className='grid w-full grid-rows-2 gap-4 md:flex md:flex-row md:flex-wrap md:items-center md:justify-between '>
        <section className='flex flex-row items-center justify-center gap-2 md:gap-5'>
          {/* Return and One-Way Buttons */}
          <span className='inline'>
            <button
              type='button'
              onClick={() => {
                setReturn(true)
              }}
              className={`${
                isReturn
                  ? 'bg-yellow-600 text-xs font-semibold text-white p-2 ring-1 ring-yellow-400'
                  : 'bg-white text-xs font-semibold p-2 border-2'
              } 
                    focus:outline-none focus:ring-2 focus:ring-yellow-400
                `}
            >
              Return
            </button>
            <button
              type='button'
              onClick={() => {
                setReturn(false)
              }}
              className={`${
                !isReturn
                  ? 'bg-yellow-600 text-xs font-semibold text-white p-2 ring-1 ring-yellow-400'
                  : 'bg-white text-xs font-semibold p-2 border-2'
              }
                    focus:outline-none focus:ring-2 focus:ring-yellow-400`}
            >
              One-way
            </button>
          </span>

          {/* Cabin Class and Passenger Select */}
          <span>
            <Popover
              // Will show the popover defined when user clicks on Passenger Input field
              content={cabinClassPassengersPopover}
              trigger='click'
              visible={visible}
              onVisibleChange={handleVisibleChange}
              placement='bottom'
            >
              {/* We need this div for Popover to anchor onto Passenger Input field */}
              <div>
                <Search
                  readOnly
                  placeholder='Add passenger(s)'
                  className='cursor-default'
                  StartIcon={<IoIosPerson />}
                  EndIcon={<FiChevronDown />}
                  value={`${
                    cabinClass.charAt(0).toUpperCase() +
                    cabinClass.slice(1)
                  }, ${passengerCount} passenger(s)`}
                />
              </div>
            </Popover>
          </span>
        </section>

        {/* Find Flight Button */}
        <Button label='Find Flights' onClick={handleSubmit} />
      </section>
    </section>
  )
}

export default FlightForm
