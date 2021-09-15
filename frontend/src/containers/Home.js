import { React, useState } from 'react'
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
import OriginSearch from './OriginSearch'
import DestinationSearch from './DestinationSearch'
import Button from '../components/Button'
import Search from '../components/Search'
import NavBar from '../components/NavBar'
import { useStore } from '../hooks/Store'
import landscapePicture from '../assets/home-landscape.png'

const { RangePicker } = DatePicker

const airports = [
  {
    code: 'LHR',
    name: 'London Heathrow',
    location: 'London'
  },
  {
    code: 'CDG',
    name: 'Charles De Gaulle',
    location: 'Paris'
  },
  {
    code: 'ORY',
    name: 'Paris Orly',
    location: 'Paris'
  },
  {
    code: 'JFK',
    name: 'John F Kennedy',
    location: 'New York'
  },
  {
    code: 'CGK',
    name: 'Soekarno-Hatta',
    location: 'Jakarta'
  },
  {
    code: 'SIN',
    name: 'Singapore Changi',
    location: 'Singapore'
  },
  {
    code: 'BKK',
    name: 'Suvarnabhumi',
    location: 'Bangkok'
  },
  {
    code: 'HKG',
    name: 'Hong Kong',
    location: 'Hong Kong'
  },
  {
    code: 'MNL',
    name: 'Minato',
    location: 'Tokyo'
  },
  {
    code: 'ICN',
    name: 'Incheon',
    location: 'Seoul'
  },
  {
    code: 'PUS',
    name: 'Busan',
    location: 'Busan'
  },
  {
    code: 'DPS',
    name: 'Denpasar',
    location: 'Bali'
  },
  {
    code: 'HKT',
    name: 'Phuket',
    location: 'Phuket'
  },
  {
    code: 'KUL',
    name: 'Kuala Lumpur',
    location: 'Kuala Lumpur'
  },
  {
    code: 'SGN',
    name: 'Tan Son Nhat',
    location: 'Ho Chi Minh'
  },
  {
    code: 'TPE',
    name: 'Taipei',
    location: 'Taipei'
  },
  {
    code: 'HND',
    name: 'Haneda',
    location: 'Tokyo'
  }
]

const Home = () => {
  const [dates, setDates] = useState([
    moment(),
    moment().add(1, 'days')
  ])

  const [isReturn, setReturn] = useState(true)
  const [visible, setVisible] = useState(false)

  const isMobile = useMediaQuery({ query: '(max-width: 640px)' })
  const isTablet = useMediaQuery({ query: '(min-width: 768px)' })

  const handleVisibleChange = (visibleState) => {
    setVisible(visibleState)
  }

  const disabledDepartureDates = (current) =>
    current < moment().endOf('day')
  const disabledReturnDates = (current) =>
    current < dates[0].endOf('day')

  const cabinClass = useStore((state) => state.cabinClass)
  const ECONOMY = useStore((state) => state.economyClass)
  const BUSINESS = useStore((state) => state.businessClass)
  const FIRST = useStore((state) => state.firstClass)
  const setEconomyClass = useStore((state) => state.setEconomyClass)
  const setBusinessClass = useStore((state) => state.setBusinessClass)
  const setFirstClass = useStore((state) => state.setFirstClass)

  const passengerCount = useStore((state) => state.passengerCount)
  const addPassenger = useStore((state) => state.addPassenger)
  const removePassenger = useStore((state) => state.removePassenger)

  const cabinClassPassengersPopover = (
    <section className='flex flex-col justify-center items-start gap-3 font-semibold'>
      {/* Cabin Class Buttons */}
      <span>
        <h4>Cabin Class</h4>
      </span>
      <span className='grid grid-cols-3 gap-0.5'>
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
      </span>
      {/* Passenger Count Buttons */}
      <span>
        <h4>Number of Passengers</h4>
      </span>
      <span className='flex justify-center items-center gap-4'>
        <button
          type='button'
          onClick={removePassenger}
          className='rounded-xl text-yellow-500 focus:outline-none focus:ring-2 focus:ring-yellow-400'
        >
          <FiMinusCircle />
        </button>
        <span>{passengerCount}</span>
        <button
          type='button'
          onClick={addPassenger}
          className='rounded-xl text-yellow-500 focus:outline-none focus:ring-2 focus:ring-yellow-400'
        >
          <FiPlusCircle />
        </button>
      </span>
    </section>
  )

  return (
    <main className='h-screen'>
      <NavBar />
      <section className='h-full flex flex-col justify-start items-center'>
        <img
          draggable={false}
          src={landscapePicture}
          alt='Landscape with hot air balloons'
          className='
            relative 
            h-5/6 md:h-3/4 w-full
            object-cover object-left
            filter contrast-75'
        />
        <div
          className='
          absolute
          transform translate-y-36 sm:translate-y-32
          self-center mx-6'
        >
          <h2 className='text-white text-center sm:text-left text-3xl sm:text-5xl font-bold select-none'>
            Search hundreds of flights
          </h2>
        </div>
        {/* Main Flight Search Form */}
        <section
          className='
            absolute 
            flex flex-wrap flex-col justify-center items-center gap-3 
            max-w-max bg-yellow-50 mt-52 p-5'
        >
          {/* Section 1: Origin/Destination Airport Search and Date Pickers */}
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
                disabledDate={disabledDepartureDates}
                allowClear={false}
                value={dates && dates[0] ? dates[0] : moment()}
                bordered={false}
                suffixIcon={<FiArrowRight />}
                placeholder='Departure date'
                onChange={(date) => {
                  setDates((oldDates) =>
                    date > oldDates[1].startOf('day')
                      ? [date, date.add(1, 'days')]
                      : [date, oldDates[1]]
                  )
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
                  setDates((oldDates) => [oldDates[0], date])
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
                inputReadOnly
                separator={<FiArrowRight className='text-gray-400' />}
                bordered={false}
                disabled={[false, !isReturn]}
                placeholder={['Departure date', 'Return date']}
                onChange={(newDates) => {
                  setDates(newDates)
                }}
              />
            </span>
          </section>

          {/* Section 2: Return/One-Way Buttons, Cabin Class and Passenger Select, and Find Flights Button */}
          <section className='grid grid-rows-2 w-full gap-4 md:flex md:flex-row md:flex-wrap md:items-center md:justify-between '>
            <section className='flex flex-row justify-center items-center gap-2 md:gap-5'>
              {/* Return and One-Way Buttons */}
              <span className='inline'>
                <button
                  type='button'
                  onClick={() => {
                    setReturn(true)
                  }}
                  className={`${
                    isReturn
                      ? 'w-20 bg-yellow-600 text-xs font-semibold text-white p-2 ring-1 ring-yellow-400'
                      : 'w-20 bg-white text-xs font-semibold p-2 border-2'
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
                      ? 'w-20 bg-yellow-600 text-xs font-semibold text-white p-2 ring-1 ring-yellow-400'
                      : 'w-20 bg-white text-xs font-semibold p-2 border-2'
                  }
                    focus:outline-none focus:ring-2 focus:ring-yellow-400`}
                >
                  One-way
                </button>
              </span>

              {/* Cabin Class and Passenger Select */}
              <span>
                <Popover
                  // Will show the popover defined above when the user clicks on the Passenger Search field
                  content={cabinClassPassengersPopover}
                  trigger='click'
                  visible={visible}
                  onVisibleChange={handleVisibleChange}
                  placement='bottom'
                >
                  {/* We need this div for the Popover to anchor onto Searchbar */}
                  <div>
                    <Search
                      readOnly
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
            <Button label='Find Flights' submit onClick={() => {}} />
          </section>
        </section>
      </section>
    </main>
  )
}

export default Home
