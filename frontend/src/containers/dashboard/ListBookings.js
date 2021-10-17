import React, { useEffect, useState } from 'react'
import moment from 'moment'
import { string } from 'prop-types'
import { Link } from 'react-router-dom'
import { useHistory } from 'react-router'
import { FaSadTear } from 'react-icons/fa'
import { Table, Tag } from 'antd'
import { useQueryClient } from 'react-query'

// Hooks
import { useSessionStore } from '../../hooks/Store'

// Containers and Components
import useBookings from '../../hooks/useBookings'

const { Column, ColumnGroup } = Table

const ListBookings = ({ bookingsStatus }) => {
  const token = useSessionStore((state) => state.token)

  const user = useSessionStore((state) => state.user)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  const resetSession = useSessionStore((state) => state.resetSession)
  const queryClient = useQueryClient()

  useEffect(() => {
    if (!token || !user || user.userType !== 'customer') {
      setValidUser(false)
      resetSession()
      queryClient.clear()
      history.push('/')
    } else {
      setValidUser(true)
    }
  }, [token, user, history, resetSession, queryClient])

  /* -------------------------------------------------------------------------- */

  const {
    data: customerBookings,
    error,
    isLoading,
    isSuccess
  } = useBookings(token)

  useEffect(() => {
    if (error && error.response && error.response.status === 401) {
      resetSession()
      queryClient.clear()
      history.push('/login')
    }
  }, [error, history, queryClient, resetSession])

  const heading = (
    <header>
      {bookingsStatus === 'previous' ? (
        <h2 className='text-3xl font-bold'>Your previous bookings</h2>
      ) : (
        <h2 className='text-3xl font-bold'>Your current bookings</h2>
      )}
    </header>
  )

  const sort = (a, b) => {
    if (a < b) return -1
    if (a > b) return 1
    return 0
  }

  const renderBookings = () => {
    const bookings =
      bookingsStatus === 'previous'
        ? customerBookings.filter((pb) =>
            pb.returnFlight
              ? moment() >= moment(pb.returnFlight.arrival)
              : moment() >= moment(pb.flight.arrival)
          )
        : customerBookings.filter((cb) =>
            cb.returnFlight
              ? moment() < moment(cb.returnFlight.arrival)
              : moment() < moment(cb.flight.arrival)
          )

    return (
      <section className='flex flex-col max-w-lg gap-4'>
        {!bookings ? (
          <div>
            No bookings yet <FaSadTear />
          </div>
        ) : (
          <Table
            bordered
            size='middle'
            scroll={{ x: 1200, y: 400 }}
            pagination={false}
            dataSource={bookings.map((booking) => ({
              key: booking.id,
              id: booking.id.slice(0, 8),

              flightType: booking.returnFlight ? 'RETURN' : 'ONE WAY',

              flightCode: booking.flight.code,
              flightOrigin: booking.flight.origin.code,
              flightDestination: booking.flight.destination.code,
              flightDeparture: moment(
                booking.flight.departure
              ).format('YYYY-MM-DD HH:mm Z'),
              flightArrival: moment(booking.flight.arrival).format(
                'YYYY-MM-DD HH:mm Z'
              ),
              returnFlightCode: booking.returnFlight
                ? booking.returnFlight.code
                : 'N/A',
              returnFlightOrigin: booking.returnFlight
                ? booking.returnFlight.origin.code
                : 'N/A',
              returnFlightDestination: booking.returnFlight
                ? booking.returnFlight.destination.code
                : 'N/A',
              returnFlightDeparture: booking.returnFlight
                ? moment(booking.returnFlight.departure).format(
                    'YYYY-MM-DD HH:mm Z'
                  )
                : 'N/A',
              returnFlightArrival: booking.returnFlight
                ? moment(booking.returnFlight.arrival).format(
                    'YYYY-MM-DD HH:mm Z'
                  )
                : 'N/A'
            }))}
          >
            <Column
              title='ID'
              dataIndex='id'
              width={100}
              sorter={{
                compare: (a, b) => sort(a.id, b.id)
              }}
            />
            <Column
              title='Type'
              width={80}
              sorter={{
                compare: (a, b) => sort(a.id, b.id)
              }}
              render={(text, record) => {
                let colour = 'geekblue'
                if (record.flightType === 'RETURN') {
                  colour = 'green'
                }
                return (
                  <Tag color={colour} key={record.flightType}>
                    {record.flightType}
                  </Tag>
                )
              }}
            />
            <Column
              title='Origin'
              dataIndex='flightOrigin'
              width={80}
              sorter={{
                compare: (a, b) =>
                  sort(a.flightOrigin, b.flightOrigin)
              }}
            />
            <Column
              title='Destination'
              dataIndex='flightDestination'
              width={100}
              sorter={{
                compare: (a, b) =>
                  sort(a.flightDestination, b.flightDestination)
              }}
            />
            <ColumnGroup title='Outbound Flight'>
              <Column
                title='Code'
                dataIndex='flightCode'
                width={80}
                sorter={{
                  compare: (a, b) => sort(a.flightCode, b.flightCode)
                }}
              />
              <Column
                title='Departure'
                dataIndex='flightDeparture'
                width={125}
              />
              <Column
                title='Arrival'
                dataIndex='flightArrival'
                width={125}
              />
            </ColumnGroup>
            <ColumnGroup title='Return Flight'>
              <Column
                title='Code'
                dataIndex='returnFlightCode'
                width={80}
                sorter={{
                  compare: (a, b) =>
                    sort(a.returnFlightCode, b.returnFlightCode)
                }}
              />
              <Column
                title='Departure'
                dataIndex='returnFlightDeparture'
                width={125}
              />
              <Column
                title='Arrival'
                dataIndex='returnFlightArrival'
                width={125}
              />
            </ColumnGroup>
            <Column
              title='Details'
              fixed='right'
              width={80}
              render={(text, record) => (
                <Link
                  className='underline hover:underline'
                  to={`/dashboard/view/bookings/${record.key}`}
                >
                  View
                </Link>
              )}
            />
          </Table>
        )}
      </section>
    )
  }

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser ? (
        <p>Loading...</p>
      ) : (
        <section className='flex flex-col w-full h-full max-w-lg gap-4'>
          {heading}
          <hr />
          {isLoading && <p>Loading...</p>}
          {isSuccess && renderBookings()}
        </section>
      )}
    </main>
  )
}

ListBookings.propTypes = {
  bookingsStatus: string.isRequired
}

export default ListBookings
