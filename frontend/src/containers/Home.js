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
import useStore from '../hooks/Store'

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
  const setCabinClass = useStore((state) => state.setCabinClass)
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
          onClick={() => setCabinClass('economy')}
          className={`${
            cabinClass === 'economy'
              ? 'w-20 bg-yellow-600 text-xs font-medium text-white p-2 ring-1 ring-yellow-400'
              : 'w-20 bg-white text-xs font-medium p-2 border-2'
          } focus:outline-none focus:ring-2 focus:ring-yellow-400`}
        >
          Economy
        </button>
        <button
          type='button'
          onClick={() => setCabinClass('business')}
          className={`${
            cabinClass === 'business'
              ? 'w-20 bg-yellow-600 text-xs font-medium text-white p-2 ring-1 ring-yellow-400'
              : 'w-20 bg-white text-xs font-medium p-2 border-2'
          } focus:outline-none focus:ring-2 focus:ring-yellow-400`}
        >
          Business
        </button>
        <button
          type='button'
          onClick={() => setCabinClass('first')}
          className={`${
            cabinClass === 'first'
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
    <main className='h-screen my-8 flex flex-col justify-start items-center'>
      {/* Main Flight Search Form */}
      <section
        className='
            flex flex-wrap flex-col justify-center items-center gap-3 
            max-w-max bg-yellow-50 p-5'
      >
        {/* Origin/Destination Airport Search and Date Pickers */}
        <section
          className={`grid w-full justify-center items-center gap-2 ${
            !isTablet ? ' grid-rows-3' : 'grid-cols-3'
          }`}
        >
          {/* Airport Pickers */}
          <OriginSearch airports={airports} />
          <DestinationSearch airports={airports} />

          {/* Date Pickers, visible only for Mobile Devices or One-Way Flights */}
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

          {/* Date Range Picker, visible only for Return Flights or Non-Mobile Devices */}
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

        {/* Flight Type (Return/One-Way) Buttons, Cabin Class and Passenger Select, and Submit Button */}
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

            {/* Cabin Class and Passenger Popover */}
            <span>
              <Popover
                placement='bottom'
                content={cabinClassPassengersPopover}
                trigger='click'
                visible={visible}
                onVisibleChange={handleVisibleChange}
              >
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
    </main>
  )
}

export default Home
