/* eslint-disable no-unused-vars */
/* eslint-disable import/no-unresolved */
import React, { useEffect, useState } from 'react'
import { Input, Select, DatePicker, Space, Collapse } from 'antd'
import moment from 'moment-timezone'
import {
  useSessionStore,
  useFlightStore,
  useBookingStore
} from '../hooks/Store'
import NavBar from '../components/NavBar'

const { Panel } = Collapse
const { RangePicker } = DatePicker
const { Option } = Select

const CreateBooking = () => {
  const numberOfPassengers = 3
  const economyCost = 300
  const businessCost = 400
  const firstClassCost = 500
  const totalCost = 1000

  const token = useSessionStore((state) => state.token)
  //   const [state, setState] = useState({

  //   })
  const passengerCount = useFlightStore(
    (state) => state.passengerCount
  )
  const selectedOutboundFlight = useBookingStore(
    (state) => state.selectedOutboundFlight
  )
  const selectedReturnFlight = useBookingStore(
    (state) => state.selectedReturnFlight
  )

  const generatePassengerState = (numPassengers) => {
    const passengerState = []
    const singlePassenger = {
      givenName: '',
      surname: '',
      passportNumber: '',
      nationality: '',
      dateOfBirth: moment()
    }
    for (let i = 0; i < numPassengers; i += 1) {
      passengerState.push(singlePassenger)
    }
  }

  const [state, setState] = useState([])

  useEffect(() => {
    if (state === []) generatePassengerState(numberOfPassengers)
  }, [])

  const invalidDOB = (current) => current >= moment()

  const handleSubmit = (e) => {
    e.preventDefault()
    // console.log(state)
  }

  const handleChange = (e, index) =>
    setState((oldState) => {
      const newState = oldState
      const passengerState = {
        ...oldState[index],
        [e.target.id]: e.target.value
      }
      newState.splice(index, 1, passengerState)
      return newState
    })

  //   const handleTotalCost = (e) => {
  //     setState(())
  //   }

  // eslint-disable-next-line consistent-return
  const passengerForm =
    state &&
    [...Array(numberOfPassengers).keys()].map((passenger) => {
      const passengerNumber = passenger + 1
      return (
        <Panel
          header={`Passenger ${passengerNumber}`}
          key={passengerNumber}
        >
          {/* <section className='grid items-center w-full grid-cols-5 gap-2 p-3 bg-gray-50'> */}
          <h1 className='text-1xl font-bold'>
            Passenger {passenger + 1}
          </h1>
          <p>First name</p>
          <Input
            required
            id='givenName'
            value={state[passenger].givenName}
            className='col-span-3'
            placeholder={`Enter passenger${passengerNumber} first name`}
            onChange={handleChange}
          />
          <p>Surname</p>
          <Input
            required
            id='surname'
            value={state[passenger].surname}
            className='col-span-3'
            placeholder={`Enter passenger${passengerNumber} surname`}
            onChange={handleChange}
          />
          <p>Passport number</p>
          <Input
            required
            id='passportNumber'
            value={state[passenger].passportNumber}
            className='col-span-3'
            placeholder={`Enter passenger${passengerNumber} passport number`}
            onChange={handleChange}
          />
          <p>Nationality</p>
          <Input
            required
            id='nationality'
            value={state[passenger].nationality}
            className='col-span-3'
            placeholder={`Enter passenger${passengerNumber} nationality`}
            onChange={handleChange}
          />
          <p>Date of birth</p>
          {/* <Space direction='vertical' size={12}> */}
          <DatePicker
            disabledDate={invalidDOB}
            value={state[passenger].dateOfBirth}
            // onChange={(date) =>
            //   setState((oldState) => ({
            //     ...oldState,
            //     dateOfBirth: date
            //   }))
            // }
            dateRender={(current) => {
              const style = {}
              if (current.date() === 1) {
                style.border = '1px solid #1890ff'
                style.borderRadius = '50%'
              }
              return (
                <div className='ant-picker-cell-inner' style={style}>
                  {current.date()}
                </div>
              )
            }}
          />
          {/* </Space> */}

          <Select
            required
            defaultValue={`Economy Class ($${economyCost})`}
            id='seatClass'
            value={state[passenger].givenName}
            className='col-span-3'
            onChange={handleChange}
          >
            <Option id='economy' value={economyCost}>
              Economy Class (${economyCost})
            </Option>
            <Option id='business' value={businessCost}>
              Business Class (${businessCost})
            </Option>
            <Option id='first' value={firstClassCost}>
              First Class (${firstClassCost})
            </Option>
          </Select>
        </Panel>
      )
    })

  return (
    <main
      style={{ maxHeight: '80vh' }}
      //   className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'
    >
      <NavBar />
      <section className='flex flex-col w-full h-full max-w-lg gap-4'>
        <h1 className='text-3xl font-bold'>Finalise Booking</h1>
        <h2>Departing : AVV Avalon Airport 16:00 23/05/2021</h2>
        <h2>Arriving : JFK New York Airport 08:00 24/05/2021</h2>
        <h2>Number of passengers : {numberOfPassengers}</h2>
        <hr />
        <form
          className='flex flex-col items-start w-full h-full max-h-full gap-4 overflow-y-auto'
          //   onSubmit={handleSubmit}
        >
          <Collapse defaultActiveKey={['1']}>
            {passengerForm}
          </Collapse>

          <p>Total cost : ${totalCost}</p>
          <button
            // type='submit'
            type='button'
            onClick={handleSubmit}
            className='flex items-center self-end justify-center w-20 h-10 p-2 font-semibold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
          >
            Confirm Booking
            {/* {loading ? <Spinner size={5} /> : 'Submit'} */}
          </button>
        </form>
      </section>
    </main>
  )
}

export default CreateBooking
