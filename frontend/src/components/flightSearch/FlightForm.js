import React, { useEffect, useState } from 'react'
import {
  FiArrowRight,
  FiChevronDown,
  FiMinusCircle,
  FiPlusCircle
} from 'react-icons/fi'
import { IoIosPerson } from 'react-icons/io'
import { DatePicker, Popover } from 'antd'
import { useMediaQuery } from 'react-responsive'
import { useHistory } from 'react-router-dom'
import { bool } from 'prop-types'

import OriginSearch from './OriginSearch'
import DestinationSearch from './DestinationSearch'
import Button from '../common/Button'
import Search from '../common/Search'
import { useFlightStore } from '../../hooks/Store'
import useAirports from '../../hooks/useAirports'

const moment = require('moment-timezone')

const { RangePicker } = DatePicker

const FlightForm = ({ showButton }) => {
  const history = useHistory()
  const { refetch: refetchAirports } = useAirports()

  const [airports, setAirports] = useState(null)

  useEffect(() => {
    if (!airports) {
      refetchAirports().then((res) => {
        const validAirports = res.data.filter(
          (airport) => airport.status === 'ACTIVE'
        )
        setAirports(validAirports)
      })
    }
  }, [airports, refetchAirports])

  /* -------------------------------------------------------------------------- */

  const originAirport = useFlightStore((state) => state.originAirport)
  const destinationAirport = useFlightStore(
    (state) => state.destinationAirport
  )

  const departDate = useFlightStore((state) => state.departDate)
  const returnDate = useFlightStore((state) => state.returnDate)
  const setDepartDate = useFlightStore((state) => state.setDepartDate)
  const setReturnDate = useFlightStore((state) => state.setReturnDate)

  const [dates, setDates] = useState([
    moment(departDate),
    moment(returnDate)
  ])

  const [errorMessage, setErrorMessage] = useState('')

  const isReturn = useFlightStore((state) => state.isReturn)
  const setReturn = useFlightStore((state) => state.setReturn)

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

  const passengerCount = useFlightStore(
    (state) => state.passengerCount
  )
  const addPassenger = useFlightStore((state) => state.addPassenger)
  const removePassenger = useFlightStore(
    (state) => state.removePassenger
  )

  const passengersPopover = (
    <section className='flex flex-col items-start justify-center gap-3 font-semibold'>
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
    // Search for flights here, requery flight data
    if (originAirport.code && destinationAirport.code) {
      if (originAirport.code === destinationAirport.code) {
        setErrorMessage(
          'Must choose different origin and destination airports.'
        )
        return
      }

      history.push('/flight/results')
    } else {
      setErrorMessage('Airport fields are required.')
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
        <OriginSearch airports={airports || []} />
        <DestinationSearch airports={airports || []} />

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
              setDepartDate(newDates[0])
              setReturnDate(newDates[1])
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
      <section className='flex flex-col flex-wrap items-center justify-between w-full gap-5 md:flex-row'>
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
                  ? 'bg-yellow-500 text-xs font-semibold text-white p-2 ring-1 ring-yellow-400 ring-opacity-50'
                  : 'text-xs font-semibold text-yellow-400 p-2 ring-1 ring-yellow-500 ring-opacity-50'
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
                  ? 'bg-yellow-500 text-xs font-semibold text-white p-2 ring-1 ring-yellow-400 ring-opacity-50'
                  : 'text-xs font-semibold text-yellow-400 p-2 ring-1 ring-yellow-500 ring-opacity-50'
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
              content={passengersPopover}
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
                  value={`${passengerCount} passenger(s)`}
                />
              </div>
            </Popover>
          </span>
        </section>

        {/* Find Flight Button */}
        {showButton && (
          <section className='flex flex-col flex-wrap items-center justify-center gap-4 md:flex-row'>
            <span
              className={`${!errorMessage && 'hidden'} text-red-500`}
            >
              {errorMessage}
            </span>
            <Button label='Find Flights' onClick={handleSubmit} />
          </section>
        )}
      </section>
    </section>
  )
}

FlightForm.propTypes = {
  showButton: bool.isRequired
}

export default FlightForm
