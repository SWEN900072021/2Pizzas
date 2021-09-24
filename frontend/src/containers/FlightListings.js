import React, { Fragment, useEffect, useState } from 'react'
import moment from 'moment-timezone'
import { useHistory } from 'react-router'
import shallow from 'zustand/shallow'
import { v4 as uuid } from 'uuid'
import { Listbox, Transition } from '@headlessui/react'
import { BiCheck, BiChevronDown } from 'react-icons/bi'

import Button from '../components/Button'
import FlightCard from '../components/FlightCard'
// import FlightFilter from '../components/FlightFilter'
import FlightSearch from '../components/FlightSearch'
import NavBar from '../components/NavBar'

import FlightSearchService from '../api/FlightSearchService'
import {
  useFlightStore,
  useBookingStore,
  useSessionStore
} from '../hooks/Store'

const flightSearchDetails = (state) => [
  state.originAirport,
  state.destinationAirport,
  state.departDate,
  state.returnDate
]

const FlightListings = () => {
  const isReturn = useFlightStore((state) => state.isReturn)

  const history = useHistory()
  const token = useSessionStore((state) => state.token)
  const setCreatingBooking = useBookingStore(
    (state) => state.setCreatingBooking
  )

  const outboundFlight = useBookingStore(
    (state) => state.selectedOutboundFlight
  )
  const returnFlight = useBookingStore(
    (state) => state.selectedReturnFlight
  )
  const setOutboundFlight = useBookingStore(
    (state) => state.setSelectedOutboundFlight
  )
  const setReturnFlight = useBookingStore(
    (state) => state.setSelectedReturnFlight
  )

  const flightType = [{ name: 'Outbound' }, { name: 'Return' }]
  const [flightToggle, setFlightToggle] = useState(flightType[0])

  const handleSelectOutboundFlight = (flight) => {
    setOutboundFlight(flight)
  }

  const handleSelectReturnFlight = (flight) => {
    setReturnFlight(flight)
  }

  /* -------------------------------------------------------------------------- */

  const [originAirport, destinationAirport, departDate, returnDate] =
    useFlightStore(flightSearchDetails, shallow)

  const outboundFlights = useFlightStore(
    (state) => state.outboundFlights
  )
  const returnFlights = useFlightStore((state) => state.returnFlights)

  const setOutboundFlights = useFlightStore(
    (state) => state.setOutboundFlights
  )
  const setReturnFlights = useFlightStore(
    (state) => state.setReturnFlights
  )

  useEffect(() => {
    console.log(moment(departDate).startOf('day').utc(true))

    const outboundFlightSearchCriteria = {
      origin: originAirport.id,
      destination: destinationAirport.id,
      departingAfter: moment(departDate)
        .startOf('day')
        // .tz(departDate, originAirport.utcOffset)
        .utc()
        .format(),
      departingBefore: moment(departDate)
        .endOf('day')
        // .tz(departDate, originAirport.utcOffset)
        .utc()
        .format(),
      airline: null
    }

    console.log('Outbound flight dates:', {
      departingAfter: outboundFlightSearchCriteria.departingAfter,
      departingBefore: outboundFlightSearchCriteria.departingBefore
    })

    FlightSearchService.searchFlights({
      data: outboundFlightSearchCriteria,
      onSuccess: (res) => {
        setOutboundFlights(res.data)
      },
      onError: (err) => {
        console.log(err)
      }
    })

    if (isReturn) {
      // fetch return flights
      const returnFlightSearchCriteria = {
        origin: destinationAirport.id,
        destination: originAirport.id,
        departingAfter: moment(returnDate)
          .startOf('day')
          // .tz(returnDate, destinationAirport.utcOffset)
          .utc()
          .format(),
        departingBefore: moment(returnDate)
          .endOf('day')
          // .tz(returnDate, destinationAirport.utcOffset)
          .utc()
          .format(),
        airline: null
      }

      FlightSearchService.searchFlights({
        data: returnFlightSearchCriteria,
        onSuccess: (res) => {
          setReturnFlights(res.data)
        },
        onError: (err) => {
          console.log(err)
        }
      })
    }
  }, [
    departDate,
    destinationAirport,
    isReturn,
    originAirport,
    returnDate,
    setOutboundFlight,
    setOutboundFlights,
    setReturnFlight,
    setReturnFlights
  ])

  /* -------------------------------------------------------------------------- */

  const flightToggleListbox = isReturn && (
    <div className='flex items-center justify-center gap-2 md:hidden'>
      <span>Showing </span>
      <div className='relative'>
        <Listbox value={flightToggle} onChange={setFlightToggle}>
          <Listbox.Button className='grid items-center w-full grid-flow-col py-2 pl-3 pr-10 font-medium text-left text-yellow-900 rounded-lg shadow-md cursor-default bg-yellow-50 focus:outline-none focus-visible:ring-2 focus-visible:ring-opacity-75 focus-visible:ring-white focus-visible:ring-offset-orange-300 focus-visible:ring-offset-2 focus-visible:border-indigo-500 sm:text-sm'>
            {flightToggle.name}
            <BiChevronDown className='w-5 h-5' />
          </Listbox.Button>
          <Transition
            as={Fragment}
            leave='transition ease-in duration-100'
            leaveFrom='opacity-100'
            leaveTo='opacity-0'
          >
            <Listbox.Options className='absolute py-1 mt-1 overflow-auto text-base bg-white rounded-md shadow-lg min-w-max max-h-60 ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm'>
              {flightType.map((type) => (
                <Listbox.Option
                  key={uuid()}
                  value={type}
                  className='relative grid items-center justify-start grid-cols-3 gap-2 px-4 py-2 text-gray-900 cursor-default select-none min-w-max hover:text-yellow-900 hover:bg-yellow-50'
                >
                  <span className='col-span-1'>
                    <BiCheck
                      className={`${
                        type.name !== flightToggle.name && 'hidden'
                      } w-5 h-5`}
                    />
                  </span>
                  <span
                    className={`${
                      type.name === flightToggle.name
                        ? 'font-medium'
                        : 'font-normal'
                    } col-span-2`}
                  >
                    {type.name}
                  </span>
                </Listbox.Option>
              ))}
            </Listbox.Options>
          </Transition>
        </Listbox>
      </div>
      <span>flights</span>
    </div>
  )

  /* -------------------------------------------------------------------------- */

  const handleSubmit = (e) => {
    e.preventDefault()

    if (token) history.push('/booking/create')
    else {
      setCreatingBooking(true)
      history.push('/login')
    }
  }

  return (
    <main className='flex flex-col items-start w-full h-screen'>
      <NavBar />
      <section className='flex flex-col self-center justify-start w-full h-full gap-10 mt-8 md:max-w-screen-md'>
        <FlightSearch />
        {/* <FlightFilter /> */}
        <div className='flex flex-col items-center w-full gap-3'>
          {/* <FlightSidebar /> */}
          {flightToggleListbox}
          <div className='grid items-start self-center justify-center grid-flow-col gap-4 md:justify-end auto-cols-min'>
            <section
              className={`${
                flightToggle.name === flightType[0].name
                  ? 'flex'
                  : 'hidden'
              } md:flex flex-col items-center justify-center gap-5 md:items-end`}
            >
              {outboundFlights &&
                outboundFlights.map((flight) => (
                  <div className='flex items-center justify-center gap-5 md:items-end'>
                    <FlightCard
                      flight={flight}
                      selected={
                        outboundFlight
                          ? flight.id === outboundFlight.id
                          : false
                      }
                      selectFlight={handleSelectOutboundFlight}
                    />
                  </div>
                ))}
            </section>
            {isReturn && (
              <span className='hidden w-px h-full bg-black bg-opacity-60 md:block' />
            )}
            {isReturn && (
              <section
                className={`${
                  flightToggle.name === flightType[1].name
                    ? 'flex'
                    : 'hidden'
                } md:flex flex-col items-center justify-center gap-5 md:items-end`}
              >
                {returnFlights &&
                  returnFlights.map((flight) => (
                    <div className='flex items-center justify-center gap-5 md:items-end'>
                      <FlightCard
                        flight={flight}
                        selected={
                          returnFlight
                            ? flight.id === returnFlight.id
                            : false
                        }
                        selectFlight={handleSelectReturnFlight}
                      />
                    </div>
                  ))}
              </section>
            )}
          </div>
          <Button
            label='Submit'
            onClick={handleSubmit}
            disabled={
              isReturn
                ? !(outboundFlight && returnFlight)
                : !outboundFlight
            }
          />
        </div>
      </section>
    </main>
  )
}

export default FlightListings
