import React, { useEffect, useState } from 'react'
import { Table } from 'antd'
import moment from 'moment-timezone'
import {
  FaChevronLeft
  // FaPlane,
  // FaPlaneArrival,
  // FaPlaneDeparture
} from 'react-icons/fa'
import { useHistory, useParams } from 'react-router'
import Spinner from '../../components/common/Spinner'
import { useSessionStore } from '../../hooks/Store'
import useFlights from '../../hooks/useFlights'
import useAirports from '../../hooks/useAirports'

const ViewFlight = () => {
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  useEffect(() => {
    if (!token || !user || user.userType !== 'airline') {
      setValidUser(false)
      history.push('/')
    } else {
      setValidUser(true)
    }
  }, [token, user, history])

  /* -------------------------------------------------------------------------- */

  const { id } = useParams()
  const resetSession = useSessionStore((state) => state.resetSession)
  const { data, isLoading, isSuccess, isError } = useFlights(token)
  const { data: airports } = useAirports()
  const [flight, setFlight] = useState(null)

  useEffect(() => {
    if (isError) {
      resetSession()
      history.push('/')
    }

    if (isSuccess && data && id && !flight) {
      const currentFlight = data.find((b) => b.id === id)
      console.log(currentFlight)
      setFlight(currentFlight)
    }
  }, [data, isSuccess, id, history, isError, resetSession])

  const goBack = () => {
    history.goBack()
  }

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser || !isSuccess || !flight || !airports ? (
        <div>{isLoading && <Spinner size={6} />}</div>
      ) : (
        <section className='flex flex-col w-full max-w-lg gap-4'>
          <header className='flex flex-col items-start self-start gap-2'>
            <h1 className='text-3xl font-bold'>Flight Details</h1>
            <button
              type='button'
              onClick={goBack}
              className='flex items-center justify-center gap-2 transition-colors hover:text-yellow-600'
            >
              <FaChevronLeft /> Back
            </button>
          </header>
          <hr />
          <section className='flex items-stretch self-start gap-2'>
            <button
              type='button'
              className='px-4 py-2 font-bold text-white transition-colors bg-red-500 rounded-lg hover:bg-red-600'
            >
              Cancel Flight
            </button>
            <button
              type='button'
              className='px-4 py-2 font-bold text-white transition-colors bg-blue-400 rounded-lg hover:bg-blue-600'
            >
              Mark as Delayed
            </button>
          </section>
          <section className='grid grid-cols-12 gap-3'>
            <>
              <p className='col-span-6'>Status</p>
              <p className='col-span-6'>{flight.status}</p>
            </>
            <>
              <p className='col-span-6'>Flight Code</p>
              <p className='col-span-6'>{flight.code}</p>
            </>
            <>
              <p className='col-span-6'>Origin Airport</p>
              <p className='grid-flow-row col-span-6'>
                <p>{flight.origin.name}</p>
                <p>{flight.origin.code}</p>
                <p>{flight.origin.location}</p>
              </p>
            </>
            <>
              <p className='col-span-6'>Departure Date/Time</p>
              <p className='col-span-6'>
                {moment(flight.departureLocal).format(
                  'YYYY/MM/DD HH:mm Z z'
                )}
              </p>
            </>
            <>
              <p className='col-span-6'>Destination Airport</p>
              <p className='grid-flow-row col-span-6'>
                <p>{flight.origin.name}</p>
                <p>{flight.origin.code}</p>
                <p>{flight.origin.location}</p>
              </p>
            </>
            <>
              <p className='col-span-6'>Arrival Date/Time</p>
              <p className='col-span-6'>
                {moment(flight.arrivalLocal).format(
                  'YYYY/MM/DD HH:mm Z z'
                )}
              </p>
            </>
            <>
              {flight.stopOvers ? (
                <>
                  <p className='col-span-3'>Stopovers</p>
                  <p className='grid-flow-row col-span-9'>
                    <Table
                      dataSource={flight.stopOvers.map(
                        (stopover) => ({
                          id: stopover.location,
                          location: stopover.location,
                          arrival: stopover.arrival,
                          departure: stopover.departure
                        })
                      )}
                    >
                      <Table.Column
                        title='Location'
                        dataIndex='location'
                      />
                      <Table.Column
                        title='Arrival'
                        dataIndex='arrival'
                      />
                      <Table.Column
                        title='Departure'
                        dataIndex='departure'
                      />
                    </Table>
                  </p>
                </>
              ) : (
                <>
                  <p className='col-span-6'>Stopovers</p>
                  <p className='col-span-6'>
                    <p>N/A</p>
                  </p>
                </>
              )}
            </>
          </section>
        </section>
      )}
    </main>
  )
}

export default ViewFlight
