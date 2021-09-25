import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router'
import { Table, Tag, Space } from 'antd'
import moment from 'moment'
import { Link } from 'react-router-dom'
import { FaPlus } from 'react-icons/fa'

import Spinner from '../../components/common/Spinner'
import { useSessionStore } from '../../hooks/Store'
import useFlights from '../../hooks/useFlights'

const { Column, ColumnGroup } = Table

const ListFlights = () => {
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

  const {
    data: flights,
    isLoading,
    isSuccess,
    isError,
    refetch: refetchFlights
  } = useFlights(token)

  useEffect(() => {
    refetchFlights()
  }, [refetchFlights])

  const heading = (
    <header className='flex items-center justify-between'>
      <h2 className='text-3xl font-bold'>Your flights</h2>
      <Link to='/dashboard/create/flights'>
        <button
          type='button'
          className='flex items-center justify-center gap-2 p-2 font-bold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
        >
          <FaPlus />
          <span>Add New Flight</span>
        </button>
      </Link>
    </header>
  )

  const sort = (a, b) => {
    if (a < b) return -1
    if (a > b) return 1
    return 0
  }

  const renderFlights = () => {
    if (isLoading) {
      return <Spinner size={6} />
    }

    if (isSuccess && flights) {
      const dataSource = flights.map((flight, idx) => ({
        key: `${idx}`,
        id: flight.id,
        flightCode: flight.code,
        originName: flight.origin.name,
        destinationName: flight.destination.name,
        originCode: flight.origin.code,
        destinationCode: flight.destination.code,
        departure: flight.departureLocal
          ? moment(flight.departureLocal)
              .utc()
              .format('YYYY-MM-DD HH:mm')
          : moment(flight.departure).utc().format('YYYY-MM-DD HH:mm'),
        arrival: flight.arrivalLocal
          ? moment(flight.arrivalLocal)
              .utc()
              .format('YYYY-MM-DD HH:mm')
          : moment(flight.arrival).utc().format('YYYY-MM-DD HH:mm'),
        departureMoment: flight.departureLocal
          ? moment(flight.departureLocal).utc()
          : moment(flight.departure).utc(),
        arrivalMoment: flight.arrivalLocal
          ? moment(flight.arrivalLocal).utc()
          : moment(flight.arrival).utc(),
        status: flight.status
      }))

      return (
        <Table
          dataSource={dataSource}
          bordered
          size='middle'
          scroll={{ x: 1200, y: 400 }}
          pagination={false}
        >
          <Column
            title='Code'
            dataIndex='flightCode'
            width={80}
            sorter={{
              compare: (a, b) => sort(a.flightCode, b.flightCode)
            }}
          />
          <ColumnGroup title='Origin Airport'>
            <Column
              title='Name'
              dataIndex='originName'
              key='originName'
              sorter={{
                compare: (a, b) => sort(a.originName, b.originName)
              }}
            />
            <Column
              title='Code'
              dataIndex='originCode'
              key='originCode'
              width={80}
              sorter={{
                compare: (a, b) => sort(a.originCode, b.originCode)
              }}
            />
          </ColumnGroup>
          <ColumnGroup title='Destination Airport'>
            <Column
              title='Name'
              dataIndex='destinationName'
              key='destinationName'
              sorter={{
                compare: (a, b) =>
                  sort(a.destinationName, b.destinationName)
              }}
            />

            <Column
              title='Code'
              dataIndex='destinationCode'
              key='destinationCode'
              width={80}
              sorter={{
                compare: (a, b) =>
                  sort(a.destinationCode, b.destinationCode)
              }}
            />
          </ColumnGroup>
          <Column
            title='Departure (UTC)'
            dataIndex='departure'
            key='departure'
            sorter={{
              compare: (a, b) =>
                sort(a.departureMoment, b.departureMoment)
            }}
          />
          <Column
            title='Arrival (UTC)'
            dataIndex='arrival'
            key='arrival'
            sorter={{
              compare: (a, b) =>
                sort(a.arrivalMoment, b.arrivalMoment)
            }}
          />
          <Column
            title='Status'
            dataIndex='status'
            key='status'
            width={150}
            sorter={{
              compare: (a, b) => sort(a.status, b.status)
            }}
            render={(status) => {
              let colour = 'geekblue'

              if (status === 'TO_SCHEDULE') {
                colour = 'green'
              }
              if (status === 'CANCELLED') {
                colour = 'volcano'
              }

              return (
                <Tag color={colour} key={status}>
                  {status}
                </Tag>
              )
            }}
          />
          <Column
            title='Details'
            key='details'
            fixed='right'
            render={(text, record) => (
              <Space size='middle'>
                <Link
                  to={`/dashboard/view/flights/${record.id}`}
                  className='underline hover:underline'
                >
                  View
                </Link>
              </Space>
            )}
            width={80}
          />
        </Table>
      )
    }

    if (isError) {
      return <p>Something went wrong</p>
    }

    return <Spinner size={6} />
  }

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser ? (
        <Spinner size={6} />
      ) : (
        <section className='flex flex-col w-full h-full max-w-3xl gap-4'>
          {heading}
          {renderFlights()}
        </section>
      )}
    </main>
  )
}

export default ListFlights
