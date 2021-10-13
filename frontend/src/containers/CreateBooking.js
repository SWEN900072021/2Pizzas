/* eslint-disable no-nested-ternary */
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
import { useQueryClient } from 'react-query'

import {
  useSessionStore,
  useFlightStore,
  useBookingStore
} from '../hooks/Store'
import NavBar from '../components/common/NavBar'
import { BookingService } from '../api'
import Spinner from '../components/common/Spinner'

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

  const [economyAvailableOutbound, setEconomyAvailableOutbound] =
    useState(false)
  const [businessAvailableOutbound, setBusinessAvailableOutbound] =
    useState(false)
  const [firstAvailableOutbound, setFirstAvailableOutbound] =
    useState(false)
  const [economyAvailableReturn, setEconomyAvailableReturn] =
    useState(false)
  const [businessAvailableReturn, setBusinessAvailableReturn] =
    useState(false)
  const [firstAvailableReturn, setFirstAvailableReturn] =
    useState(false)

  useEffect(() => {
    // console.log(
    //   'Outbound availabilities:',
    //   selectedOutboundFlight.seatAvailabilities
    // )

    selectedOutboundFlight.seatAvailabilities.forEach(
      (classAvailable) => {
        if (classAvailable.seatClass === 'ECONOMY') {
          setEconomyAvailableOutbound(true)
        } else if (classAvailable.seatClass === 'BUSINESS') {
          setBusinessAvailableOutbound(true)
        } else if (classAvailable.seatClass === 'FIRST') {
          setFirstAvailableOutbound(true)
        }

        // classAvailable.seatClass === 'ECONOMY'
        //   ? setEconomyAvailableOutbound(true)
        //   : setEconomyAvailableOutbound(false)
        // classAvailable.seatClass === 'BUSINESS'
        //   ? setBusinessAvailableOutbound(true)
        //   : setBusinessAvailableOutbound(false)
        // classAvailable.seatClass === 'FIRST'
        //   ? setFirstAvailableOutbound(true)
        //   : setFirstAvailableOutbound(false)
      }
    )

    if (isReturn) {
      // console.log(
      //   'Return availabilities:',
      //   selectedReturnFlight.seatAvailabilities
      // )

      selectedReturnFlight.seatAvailabilities.forEach(
        (classAvailable) => {
          if (classAvailable.seatClass === 'ECONOMY') {
            setEconomyAvailableReturn(true)
          } else if (classAvailable.seatClass === 'BUSINESS') {
            setBusinessAvailableReturn(true)
          } else if (classAvailable.seatClass === 'FIRST') {
            setFirstAvailableReturn(true)
          }

          // classAvailable.seatClass === 'ECONOMY'
          //   ? setEconomyAvailableReturn(true)
          //   : setEconomyAvailableReturn(false)
          // classAvailable.seatClass === 'FIRST'
          //   ? setFirstAvailableReturn(true)
          //   : setFirstAvailableReturn(false)
          // classAvailable.seatClass === 'BUSINESS'
          //   ? setBusinessAvailableReturn(true)
          //   : setBusinessAvailableReturn(false)
        }
      )
    }

    // console.log(
    //   economyAvailableOutbound,
    //   businessAvailableOutbound,
    //   firstAvailableOutbound,
    //   economyAvailableReturn,
    //   businessAvailableReturn,
    //   firstAvailableReturn
    // )
  }, [
    businessAvailableOutbound,
    businessAvailableReturn,
    economyAvailableOutbound,
    economyAvailableReturn,
    firstAvailableOutbound,
    firstAvailableReturn,
    isReturn,
    selectedOutboundFlight,
    selectedReturnFlight
  ])

  const [state, setState] = useState(null)

  useEffect(() => {
    if (!state) {
      const passengerState = []
      const singlePassenger = {
        givenName: '',
        surname: '',
        passportNumber: '',
        nationality: '',
        dateOfBirth: moment(),
        outboundClass: economyAvailableOutbound
          ? 'ECONOMY'
          : businessAvailableOutbound
          ? 'BUSINESS'
          : firstAvailableOutbound
          ? 'FIRST'
          : null,
        returnClass: economyAvailableReturn
          ? 'ECONOMY'
          : businessAvailableReturn
          ? 'BUSINESS'
          : firstAvailableReturn
          ? 'FIRST'
          : null,
        outboundSeat: '',
        returnSeat: ''
      }
      for (let i = 0; i < numberOfPassengers; i += 1) {
        passengerState.push(singlePassenger)
      }

      setState(passengerState)
    }
  }, [
    businessAvailableOutbound,
    businessAvailableReturn,
    economyAvailableOutbound,
    economyAvailableReturn,
    firstAvailableOutbound,
    firstAvailableReturn,
    numberOfPassengers,
    selectedOutboundFlight,
    state
  ])

  /* -------------------------------------------------------------------------- */

  const invalidDOB = (current) => current > moment()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const validate = () => {
    state.forEach((passenger) => {
      if (passenger.givenName === '') {
        return 'Please enter a first name for all passengers.'
      }

      if (passenger.surname === '') {
        return 'Please enter a surname for all passengers.'
      }

      if (passenger.passportNumber === '') {
        return 'Please enter a passport number for all passengers.'
      }

      return null
    })
  }

  /* -------------------------------------------------------------------------- */

  const queryClient = useQueryClient()
  const resetSession = useSessionStore((st) => st.resetSession)

  const handleSubmit = (e) => {
    e.preventDefault()

    setError(null)

    const seatAllocations = []
    state.forEach((passenger) => {
      const individualSeatAllocation = []
      if (passenger.outboundSeat) {
        // seatAllocations.push({
        individualSeatAllocation.push({
          seatName: passenger.outboundSeat,
          flightId: selectedOutboundFlight.id
        })
      }

      if (isReturn && selectedReturnFlight && passenger.returnSeat) {
        // seatAllocations.push({
        individualSeatAllocation.push({
          seatName: passenger.returnSeat,
          flightId: selectedReturnFlight.id
        })
      }
      seatAllocations.push(individualSeatAllocation)
    })

    const passengerBookings = []
    for (let i = 0; i < numberOfPassengers; i += 1) {
      passengerBookings.push({
        givenName: state[i].givenName,
        surname: state[i].surname,
        passportNumber: state[i].passportNumber,
        dateOfBirth: state[i].dateOfBirth.format('YYYY-MM-DD'),
        nationality: state[i].nationality,
        seatAllocations: seatAllocations[i]
      })
    }

    const booking = {
      flightId: selectedOutboundFlight.id,
      returnFlightId:
        isReturn && selectedReturnFlight
          ? selectedReturnFlight.id
          : null,
      passengerBookings
    }

    // console.log('outboundFlight')
    // console.log(selectedOutboundFlight)
    // console.log('returnFlight')
    // console.log(selectedReturnFlight)
    // console.log('state')
    // console.log(state)
    // console.log('booking')
    // console.log(booking)

    const bookingError = validate()

    if (bookingError) {
      setError(bookingError)
      return
    }

    setLoading(true)

    BookingService.createBooking(
      token,
      booking,
      (res) => {
        setLoading(false)
        history.push('/dashboard/view/bookings/current')
      },
      (err) => {
        setLoading(false)

        if (
          err.response &&
          err.response.status &&
          err.response.status === 401
        ) {
          queryClient.clear()
          resetSession()
          history.push('/login')
        }

        if (
          err.response &&
          err.response.data &&
          err.response.data.message
        ) {
          setError(err.response.data.message)
        }
        // console.log(err)
      }
    )

    // console.log(state)
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

  const [selectedOutboundSeats, setSelectedOutboundSeats] = useState(
    new Set()
  )
  const [selectedReturnSeats, setSelectedReturnSeats] = useState(
    new Set()
  )

  const handleClassSeatChange = (value, index, type) => {
    let i = 0
    const updatedState = state.map((passengerObj) => {
      if (i === index) {
        i += 1

        if (type === 'outboundSeat') {
          let newSelectedOutboundSeats = new Set(
            selectedOutboundSeats
          )

          if (state[index].outboundSeat) {
            const previousSeat = state[index].outboundSeat
            newSelectedOutboundSeats.delete(previousSeat)
          }

          newSelectedOutboundSeats =
            newSelectedOutboundSeats.add(value)

          setSelectedOutboundSeats(newSelectedOutboundSeats)

          // console.log(
          //   'New selected outbound seats:',
          //   newSelectedOutboundSeats
          // )

          // console.log('Update passenger ', index, 'to:', {
          //   ...passengerObj,
          //   [type]: value
          // })
        }

        if (type === 'returnSeat') {
          let newSelectedReturnSeats = new Set(selectedReturnSeats)

          if (state[index].returnSeat) {
            const previousSeat = state[index].returnSeat
            newSelectedReturnSeats.delete(previousSeat)
          }

          newSelectedReturnSeats = newSelectedReturnSeats.add(value)

          setSelectedReturnSeats(newSelectedReturnSeats)

          // console.log(
          //   'New selected return seats:',
          //   newSelectedReturnSeats
          // )

          // console.log('Update passenger ', index, 'to:', {
          //   ...passengerObj,
          //   [type]: value
          // })
        }

        if (type === 'outboundClass') {
          if (state[index].outboundSeat) {
            const previousSeat = state[index].outboundSeat
            const newSelectedOutboundSeats = new Set(
              selectedOutboundSeats
            )
            newSelectedOutboundSeats.delete(previousSeat)

            setSelectedOutboundSeats(newSelectedOutboundSeats)
          }

          return { ...passengerObj, [type]: value, outboundSeat: '' }
        }

        if (type === 'returnClass') {
          if (state[index].returnSeat) {
            const previousSeat = state[index].returnSeat
            const newSelectedReturnSeats = new Set(
              selectedReturnSeats
            )
            newSelectedReturnSeats.delete(previousSeat)

            setSelectedReturnSeats(newSelectedReturnSeats)
          }

          return { ...passengerObj, [type]: value, returnSeat: '' }
        }

        // Set the state of the seat
        return { ...passengerObj, [type]: value }
      }
      i += 1
      return passengerObj
    })

    setState(updatedState)
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
          {/* <h1 className='font-bold text-1xl'>
            Passenger {passenger + 1}
          </h1> */}
          <section className='flex flex-col items-stretch justify-start gap-2'>
            <p className='font-medium'>Given name</p>
            <Input
              required
              id='givenName'
              value={state[passenger].givenName}
              placeholder={`Enter passenger ${passengerNumber} given name`}
              onChange={(e) => handleChange(e, passenger)}
            />
            <p className='font-medium'>Surname</p>
            <Input
              required
              id='surname'
              value={state[passenger].surname}
              placeholder={`Enter passenger ${passengerNumber} surname`}
              onChange={(e) => handleChange(e, passenger)}
            />
            <p className='font-medium'>Passport number</p>
            <Input
              required
              id='passportNumber'
              value={state[passenger].passportNumber}
              placeholder={`Enter passenger ${passengerNumber} passport number`}
              onChange={(e) => handleChange(e, passenger)}
            />
            <p className='font-medium'>Nationality</p>
            <Input
              required
              id='nationality'
              value={state[passenger].nationality}
              placeholder={`Enter passenger ${passengerNumber} nationality`}
              onChange={(e) => handleChange(e, passenger)}
            />
            <p className='font-medium'>Date of Birth</p>
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
                  <div
                    className='ant-picker-cell-inner'
                    style={style}
                  >
                    {current.date()}
                  </div>
                )
              }}
            />
            {/* </Space> */}

            {/* OUTBOUND FLIGHT SEAT AND CLASS */}
            <p className='font-medium'>Outbound Flight Class</p>
            <Select
              required
              defaultValue={
                economyAvailableOutbound
                  ? 'ECONOMY'
                  : businessAvailableOutbound
                  ? 'BUSINESS'
                  : firstAvailableOutbound
                  ? 'FIRST'
                  : null
              }
              placeholder='Select cabin class'
              value={state[passenger].outboundClass}
              id='outboundClass'
              style={{ width: '100%' }}
              onChange={(value) =>
                handleClassSeatChange(
                  value,
                  passenger,
                  'outboundClass'
                )
              }
            >
              {economyAvailableOutbound && (
                <Option id='economy' value='ECONOMY'>
                  Economy Class ($
                  {selectedOutboundFlight.economyClassCost})
                </Option>
              )}
              {businessAvailableOutbound && (
                <Option id='business' value='BUSINESS'>
                  Business Class ($
                  {selectedOutboundFlight.businessClassCost})
                </Option>
              )}
              {firstAvailableOutbound && (
                <Option id='first' value='FIRST'>
                  First Class ($
                  {selectedOutboundFlight.firstClassCost})
                </Option>
              )}
            </Select>

            <p className='font-medium'>Outbound Flight Seat</p>
            <Select
              required
              value={state[passenger].outboundSeat}
              id='outboundSeat'
              disabled={!state[passenger].outboundClass}
              style={{ width: '100%' }}
              onChange={(value) =>
                handleClassSeatChange(
                  value,
                  passenger,
                  'outboundSeat'
                )
              }
            >
              {selectedOutboundFlight.seatAvailabilities &&
                selectedOutboundFlight.seatAvailabilities.map(
                  (seatsPerClass) => {
                    if (
                      seatsPerClass.seatClass ===
                      state[passenger].outboundClass
                    ) {
                      return seatsPerClass.seats
                        .filter((seat) => {
                          const currentPassengerSeat =
                            state[passenger].outboundSeat === seat
                          const notAlreadySelected =
                            !selectedOutboundSeats.has(seat)

                          return (
                            currentPassengerSeat || notAlreadySelected
                          )
                        })
                        .map((seat) => (
                          <Option id={seat} value={seat}>
                            {seat}
                          </Option>
                        ))
                    }
                    return null
                  }
                )}
            </Select>

            {/* RETURN FLIGHT CLASS AND SEAT */}
            {isReturn && selectedReturnFlight ? (
              <>
                <p className='font-medium'>Return Flight Class</p>
                <Select
                  required
                  defaultValue={
                    economyAvailableReturn
                      ? 'ECONOMY'
                      : businessAvailableReturn
                      ? 'BUSINESS'
                      : firstAvailableReturn
                      ? 'FIRST'
                      : null
                  }
                  placeholder='Select cabin class'
                  value={state[passenger].returnClass}
                  id='returnClass'
                  style={{ width: '100%' }}
                  onChange={(value) =>
                    handleClassSeatChange(
                      value,
                      passenger,
                      'returnClass'
                    )
                  }
                >
                  {economyAvailableReturn && (
                    <Option id='economy' value='ECONOMY'>
                      Economy Class ($
                      {selectedReturnFlight.economyClassCost})
                    </Option>
                  )}
                  {businessAvailableReturn && (
                    <Option id='business' value='BUSINESS'>
                      Business Class ($
                      {selectedReturnFlight.businessClassCost})
                    </Option>
                  )}
                  {firstAvailableReturn && (
                    <Option id='first' value='FIRST'>
                      First Class ($
                      {selectedReturnFlight.firstClassCost})
                    </Option>
                  )}
                </Select>

                <p className='font-medium'>Return Flight Seat</p>
                <Select
                  required
                  value={state[passenger].returnSeat}
                  id='returnSeat'
                  disabled={!state[passenger].returnClass}
                  style={{ width: '100%' }}
                  onChange={(value) =>
                    handleClassSeatChange(
                      value,
                      passenger,
                      'returnSeat'
                    )
                  }
                >
                  {isReturn &&
                    selectedReturnFlight.seatAvailabilities &&
                    selectedReturnFlight.seatAvailabilities.map(
                      (seatsPerClass) => {
                        if (
                          seatsPerClass.seatClass ===
                          state[passenger].returnClass
                        ) {
                          return seatsPerClass.seats
                            .filter((seat) => {
                              const currentPassengerSeat =
                                state[passenger].returnSeat === seat
                              const notAlreadySelected =
                                !selectedReturnSeats.has(seat)

                              return (
                                currentPassengerSeat ||
                                notAlreadySelected
                              )
                            })
                            .map((seat) => (
                              <Option id={seat} value={seat}>
                                {seat}
                              </Option>
                            ))
                        }
                        return null
                      }
                    )}
                </Select>
              </>
            ) : null}
          </section>
        </Panel>
      )
    })

  return (
    <main
      // style={{ maxHeight: '80vh' }}
      className='flex flex-col w-full h-screen gap-8'
    >
      <NavBar />
      <section
        style={{ maxHeight: '80vh' }}
        className='flex flex-col items-start self-center justify-center w-screen h-full max-w-lg gap-4'
      >
        <h1 className='text-3xl font-bold'>Finalise Booking</h1>
        <h2 className='text-xl font-bold'>Your outbound flight</h2>
        <p>
          Departing from{' '}
          <span className='font-semibold'>
            ({selectedOutboundFlight.origin.code}){' '}
            {selectedOutboundFlight.origin.name}
          </span>{' '}
          at{' '}
          <span className='font-bold'>
            {moment(selectedOutboundFlight.departureLocal).format(
              'YYYY/MM/DD, HH:mm'
            )}
          </span>
        </p>
        <p>
          Arriving at{' '}
          <span className='font-semibold'>
            ({selectedOutboundFlight.destination.code}){' '}
            {selectedOutboundFlight.destination.name}
          </span>{' '}
          at{' '}
          <span className='font-bold'>
            {moment(selectedOutboundFlight.arrivalLocal).format(
              'YYYY/MM/DD, HH:mm'
            )}
          </span>
        </p>
        {isReturn && selectedReturnFlight ? (
          <>
            <h2 className='text-xl font-bold'>Your return flight</h2>
            <p>
              Departing from{' '}
              <span className='font-semibold'>
                ({selectedReturnFlight.origin.code}){' '}
                {selectedReturnFlight.origin.name}
              </span>{' '}
              at{' '}
              <span className='font-bold'>
                {moment(selectedReturnFlight.departureLocal).format(
                  'YYYY/MM/DD, HH:mm'
                )}
              </span>
            </p>
            <p>
              Arriving at{' '}
              <span className='font-semibold'>
                ({selectedReturnFlight.destination.code}){' '}
                {selectedReturnFlight.destination.name}
              </span>{' '}
              at{' '}
              <span className='font-bold'>
                {moment(selectedReturnFlight.arrivalLocal).format(
                  'YYYY/MM/DD, HH:mm'
                )}
              </span>
            </p>
          </>
        ) : null}
        <hr />
        <form className='flex flex-col items-start w-full h-full gap-4 overflow-y-auto'>
          <Collapse
            className='w-full'
            accordion
            defaultActiveKey={['1']}
          >
            {passengerForm}
          </Collapse>
        </form>
        <span className='flex items-center justify-end w-full gap-3'>
          <p className='text-red-500'>{error || ''}</p>
          <Button
            onClick={handleSubmit}
            className='flex items-center self-end justify-center w-20 h-10 p-2 font-semibold text-white transition-colors bg-yellow-600 hover:text-white hover:border-yellow-400 hover:bg-yellow-500'
          >
            {loading ? <Spinner size={5} /> : 'Submit'}
          </Button>
        </span>
      </section>
    </main>
  )
}

export default CreateBooking
