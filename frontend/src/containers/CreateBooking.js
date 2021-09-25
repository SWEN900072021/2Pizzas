/* eslint-disable no-unused-expressions */
/* eslint-disable no-unused-vars */
/* eslint-disable import/no-unresolved */
import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router'
import {
  Input,
  Select,
  DatePicker,
  Space,
  Collapse,
  Button
} from 'antd'
import moment from 'moment-timezone'
import {
  useSessionStore,
  useFlightStore,
  useBookingStore
} from '../hooks/Store'
import NavBar from '../components/common/NavBar'
import { BookingService } from '../api'

const { Panel } = Collapse
const { RangePicker } = DatePicker
const { Option } = Select

const CreateBooking = () => {
  const token = useSessionStore((state) => state.token)
  const history = useHistory()

  const isReturn = useFlightStore((state) => state.isReturn)

  const numberOfPassengers = useFlightStore(
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
      dateOfBirth: moment(),
      outboundClass: 'economy',
      returnClass: 'economy',
      outboundSeat: '',
      returnSeat: ''
    }
    for (let i = 0; i < numPassengers; i += 1) {
      passengerState.push(singlePassenger)
    }
    return passengerState
  }

  const [state, setState] = useState(null)

  useEffect(() => {
    if (!state) {
      const passengerState = generatePassengerState(
        numberOfPassengers
      )
      setState(passengerState)
      console.log(passengerState)
      console.log(selectedOutboundFlight)
    }
  }, [numberOfPassengers, selectedOutboundFlight, state])

  /* -------------------------------------------------------------------------- */

  const invalidDOB = (current) => current > moment()

  const handleSubmit = (e) => {
    e.preventDefault()
    const seatAllocations = []
    state.forEach((passenger) => {
      if (passenger.outboundSeat) {
        seatAllocations.push({
          seatName: passenger.outboundSeat,
          flightId: selectedOutboundFlight.id
        })
      }

      if (selectedReturnFlight && passenger.returnSeat) {
        seatAllocations.push({
          seatName: passenger.returnSeat,
          flightId: selectedReturnFlight.id
        })
      }
    })
    const booking = {
      flightId: selectedOutboundFlight.id,
      returnFlightId: selectedReturnFlight
        ? selectedReturnFlight.id
        : null,
      passengerBookings: state.map((passenger) => ({
        givenName: passenger.givenName,
        surname: passenger.surname,
        passportNumber: passenger.passportNumber,
        dateOfBirth: passenger.dateOfBirth.format('YYYY-MM-DD'),
        nationality: passenger.nationality,
        seatAllocations
      }))
    }
    console.log('payload')
    console.log(selectedOutboundFlight)
    console.log(booking)
    BookingService.createBooking(
      token,
      booking,
      (res) => history.push('/dashboard/current-booking'),
      (err) => console.log(err)
    )
    console.log(state)
  }

  const handleChange = (e, index) => {
    let i = 0
    const updatedState = state.map((passengerObj) => {
      if (i === index) {
        i += 1
        return { ...passengerObj, [e.target.id]: e.target.value }
      }
      i += 1
      return passengerObj
    })
    setState(updatedState)
  }

  const handleDateChange = (date, index) => {
    let i = 0
    const updatedState = state.map((passengerObj) => {
      if (i === index) {
        i += 1
        return { ...passengerObj, dateOfBirth: date }
      }
      i += 1
      return passengerObj
    })
    setState(updatedState)
  }

  const [finalCost, setFinalCost] = useState(0)

  const handleClassSeatChange = (value, index, type) => {
    let i = 0
    const updatedState = state.map((passengerObj) => {
      if (i === index) {
        i += 1
        return { ...passengerObj, [type]: value }
      }
      i += 1
      return passengerObj
    })
    setState(updatedState)
    if (type === 'outboundClass' || type === 'returnClass') {
      const prevClassCost = state[index][type]
      let currClassCost = 0
      value === 'ECONOMY'
        ? (currClassCost = selectedOutboundFlight.economyClassCost)
        : null
      value === 'ECONOMY'
        ? (currClassCost += selectedReturnFlight.economyClassCost)
        : null
      value === 'BUSINESS'
        ? (currClassCost += selectedOutboundFlight.businessClassCost)
        : null
      value === 'BUSINESS'
        ? (currClassCost += selectedReturnFlight.businessClassCost)
        : null
      value === 'FIRST'
        ? (currClassCost += selectedOutboundFlight.firstClassCost)
        : null
      value === 'FIRST'
        ? (currClassCost += selectedReturnFlight.firstClassCost)
        : null
      setFinalCost(
        (prevCost) => prevCost - prevClassCost + currClassCost
      )
    }
  }

  // const seatListHandler = (passenger) => {
  //   for (let i = 0; i < numberOfPassengers; i += 1) {
  //     const seatClass = selectedReturnFlight.seatAvailabilities[i][0]
  //     const seat = selectedReturnFlight.seatAvailabilities[i][1]
  //     if (
  //       seatClass === 'ECONOMY' &&
  //       state[passenger].returnClass === 'ECONOMY'
  //     )
  //       return seat
  //     if (
  //       seatClass === 'BUSINESS' &&
  //       state[passenger].returnClass === 'BUSINESS'
  //     )
  //       return seat
  //     if (
  //       seatClass === 'FIRST' &&
  //       state[passenger].returnClass === 'FIRST'
  //     )
  //       return seat
  //   }
  //   return null
  // }

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
          <h1 className='font-bold text-1xl'>
            Passenger {passenger + 1}
          </h1>
          <p>First name</p>
          <Input
            required
            id='givenName'
            value={state[passenger].givenName}
            className='col-span-3'
            placeholder={`Enter passenger${passengerNumber} first name`}
            onChange={(e) => handleChange(e, passenger)}
          />
          <p>Surname</p>
          <Input
            required
            id='surname'
            value={state[passenger].surname}
            className='col-span-3'
            placeholder={`Enter passenger${passengerNumber} surname`}
            onChange={(e) => handleChange(e, passenger)}
          />
          <p>Passport number</p>
          <Input
            required
            id='passportNumber'
            value={state[passenger].passportNumber}
            className='col-span-3'
            placeholder={`Enter passenger${passengerNumber} passport number`}
            onChange={(e) => handleChange(e, passenger)}
          />
          <p>Nationality</p>
          <Input
            required
            id='nationality'
            value={state[passenger].nationality}
            className='col-span-3'
            placeholder={`Enter passenger${passengerNumber} nationality`}
            onChange={(e) => handleChange(e, passenger)}
          />
          <p>Date of birth</p>
          {/* <Space direction='vertical' size={12}> */}
          <DatePicker
            disabledDate={invalidDOB}
            value={state[passenger].dateOfBirth}
            onChange={(date) => handleDateChange(date, passenger)}
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

          <p>Outbound flight class</p>
          <Select
            required
            defaultValue='ECONOMY'
            id='outboundClass'
            className='col-span-3'
            onChange={(value) =>
              handleClassSeatChange(value, passenger, 'outboundClass')
            }
          >
            <Option id='economy' value='ECONOMY'>
              Economy Class ($
              {selectedOutboundFlight.economyClassCost})
            </Option>
            <Option id='business' value='BUSINESS'>
              Business Class ($
              {selectedOutboundFlight.businessClassCost})
            </Option>
            <Option id='first' value='FIRST'>
              First Class (${selectedOutboundFlight.firstClassCost})
            </Option>
          </Select>
          <p>Outbound flight seat</p>
          <Select
            required
            // defaultValue='economy'
            id='outboundSeat'
            className='col-span-3'
            onChange={(value) =>
              handleClassSeatChange(value, passenger, 'outboundSeat')
            }
          >
            {selectedOutboundFlight.seatAvailabilities &&
              selectedOutboundFlight.seatAvailabilities.map(
                (seatsPerClass) => {
                  if (
                    seatsPerClass.seatClass ===
                    state[passenger].outboundClass
                  )
                    return seatsPerClass.seats.map((seat) => (
                      <Option id={seat} value={seat}>
                        {seat}
                      </Option>
                    ))
                  return null
                }
              )}
          </Select>

          {isReturn && selectedReturnFlight ? (
            <>
              <p>Return flight class</p>
              <Select
                required
                defaultValue='ECONOMY'
                id='returnClass'
                className='col-span-3'
                onChange={(value) =>
                  handleClassSeatChange(
                    value,
                    passenger,
                    'returnClass'
                  )
                }
              >
                <Option id='economy' value='ECONOMY'>
                  Economy Class ($
                  {selectedReturnFlight.economyClassCost})
                </Option>
                <Option id='business' value='BUSINESS'>
                  Business Class ($
                  {selectedReturnFlight.businessClassCost})
                </Option>
                <Option id='first' value='FIRST'>
                  First Class (${selectedReturnFlight.firstClassCost})
                </Option>
              </Select>
              <p>Return flight seat</p>
              <Select
                required
                // defaultValue={() => seatListHandler(passenger)}
                id='returnSeat'
                className='col-span-3'
                onChange={(value) =>
                  handleClassSeatChange(
                    value,
                    passenger,
                    'returnSeat'
                  )
                }
              >
                {selectedReturnFlight.seatAvailabilities &&
                  selectedReturnFlight.seatAvailabilities.map(
                    (seatsPerClass) => {
                      if (
                        seatsPerClass.seatClass ===
                        state[passenger].outboundClass
                      )
                        return seatsPerClass.seats.map((seat) => (
                          <Option id={seat} value={seat}>
                            {seat}
                          </Option>
                        ))
                      return null
                    }
                  )}
              </Select>
            </>
          ) : null}
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
        <hr />
        <h2 className='text-xl font-bold'>Your outbound flight</h2>
        <p>
          Departing : {selectedOutboundFlight.origin.code}{' '}
          {selectedOutboundFlight.origin.location}{' '}
          {selectedOutboundFlight.origin.name}
          at {selectedOutboundFlight.departureLocal}
        </p>
        <p>
          Arriving : {selectedOutboundFlight.destination.code}{' '}
          {selectedOutboundFlight.destination.location}{' '}
          {selectedOutboundFlight.destination.name}
          at {selectedOutboundFlight.arrivalLocal}
        </p>
        {isReturn && selectedReturnFlight ? (
          <>
            <h2 className='text-xl font-bold'>Your return flight</h2>
            <p>
              Departing : {selectedReturnFlight.origin.code}{' '}
              {selectedReturnFlight.origin.location}{' '}
              {selectedReturnFlight.origin.name}
              at {selectedReturnFlight.departureLocal}
            </p>
            <p>
              Arriving : {selectedReturnFlight.destination.code}{' '}
              {selectedReturnFlight.destination.location}{' '}
              {selectedReturnFlight.destination.name}
              at {selectedReturnFlight.arrivalLocal}
            </p>
          </>
        ) : null}
        <hr />
        <form className='flex flex-col items-start w-full h-full max-h-full gap-4 overflow-y-auto'>
          <Collapse defaultActiveKey={['1']}>
            {passengerForm}
          </Collapse>

          <p>Total cost : ${finalCost}</p>
          <Button
            onClick={handleSubmit}
            className='flex items-center self-end justify-center w-20 h-10 p-2 font-semibold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
          >
            Confirm Booking
            {/* {loading ? <Spinner size={5} /> : 'Submit'} */}
          </Button>
        </form>
      </section>
    </main>
  )
}

export default CreateBooking
