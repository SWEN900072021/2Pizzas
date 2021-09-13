import { React, useState } from 'react'
import { FiArrowRight } from 'react-icons/fi'
import { DatePicker, Radio } from 'antd'
import { useMediaQuery } from 'react-responsive'
import moment from 'moment'
import OriginSearch from './OriginSearch'
import DestinationSearch from './DestinationSearch'
import Button from '../components/Button'

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
  const [dates, setDates] = useState([moment(), moment()])
  const [isReturn, setReturn] = useState(false)
  const isMobile = useMediaQuery({ query: '(max-width: 640px)' })

  return (
    <main className='h-screen'>
      <section className=' flex flex-wrap flex-col justify-center items-center gap-3 bg-purple-200 p-3'>
        <section className='flex flex-wrap justify-center items-center gap-3'>
          <OriginSearch airports={airports} />

          <DestinationSearch airports={airports} />
          {isMobile || !isReturn ? (
            <span
              className='inline-block font-light tracking-wide text-gray-800 
                  placeholder-gray-500 focus:placeholder-gray-400
                  focus:outline-none focus:ring-2 focus:ring-purple-400 
                  bg-white rounded-lg py-1 md:py-2'
            >
              <DatePicker
                value={dates[0]}
                bordered={false}
                suffixIcon={<FiArrowRight />}
                placeholder='Departure date'
                allowClear
                onChange={(date) => {
                  setDates((oldDates) => [date, oldDates[1]])
                }}
              />
              <DatePicker
                value={dates.at(1)}
                bordered={false}
                separator={<FiArrowRight />}
                placeholder='Return date'
                disabled={!isReturn}
                allowClear
                className='-ml-4'
                onChange={(date) => {
                  setDates((oldDates) => [oldDates[0], date])
                }}
              />
            </span>
          ) : (
            <span
              className='
                font-light tracking-wide text-gray-800 
                placeholder-gray-500 focus:placeholder-gray-400
                focus:outline-none focus:ring-2 focus:ring-purple-400 
                bg-white rounded-lg py-1 md:py-2'
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
          )}
        </section>
        <section className='flex flex-row justify-around items-center gap-8'>
          <Radio.Group
            defaultValue='return'
            value={isReturn ? 'return' : 'oneWay'}
            onChange={() => {
              setDates((oldDates) => [oldDates[0], moment()])
              setReturn(!isReturn)
            }}
            buttonStyle='solid'
          >
            <Radio.Button value='oneWay'>One-way</Radio.Button>
            <Radio.Button value='return'>Return</Radio.Button>
          </Radio.Group>
          <Button label='Find Flights' submit />
        </section>
      </section>
    </main>
  )
}

export default Home
