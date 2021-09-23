import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useHistory } from 'react-router'
import { Table, Space } from 'antd'
import { FaPlus } from 'react-icons/fa'

import Spinner from '../../components/Spinner'
import { useSessionStore } from '../../hooks/Store'
import useAirlines from '../../hooks/useAirlines'

const { Column } = Table

const ListAirlines = () => {
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  const {
    data: airlines,
    isLoading,
    isError,
    isSuccess,
    refetch: refetchAirlines
  } = useAirlines()

  useEffect(() => {
    if (!token || !user || user.userType !== 'administrator') {
      setValidUser(false)
      history.push('/')
    } else {
      setValidUser(true)
    }

    // console.log('Is valid user:', validUser)
  }, [token, user, history])

  useEffect(() => {
    console.log(airlines)

    if (!airlines) {
      refetchAirlines()
    }
  }, [airlines, refetchAirlines])

  const heading = (
    <header className='flex items-center justify-between'>
      <h2 className='text-3xl font-bold'>Your airlines</h2>
      <Link to='/dashboard/create-airline'>
        <button
          type='button'
          className='flex items-center justify-center gap-2 p-2 font-bold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
        >
          <FaPlus />
          <span>Add New Airline</span>
        </button>
      </Link>
    </header>
  )

  const sort = (a, b) => {
    if (a < b) return -1
    if (a > b) return 1
    return 0
  }

  const renderAirlines = () => {
    if (isLoading) {
      return <Spinner size={6} />
    }

    if (isSuccess && airlines) {
      const dataSource = airlines.map((airline, idx) => ({
        key: `${idx}`,
        id: airline.id,
        name: airline.name,
        code: airline.code
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
            title='ID'
            dataIndex='id'
            width={80}
            sorter={{
              compare: (a, b) => sort(a.id, b.id)
            }}
          />
          <Column
            title='Name'
            dataIndex='name'
            width={80}
            sorter={{
              compare: (a, b) => sort(a.name, b.name)
            }}
          />
          <Column
            title='Code'
            dataIndex='code'
            width={80}
            sorter={{
              compare: (a, b) => sort(a.code, b.code)
            }}
          />

          <Column
            title='Actions'
            key='actions'
            fixed='right'
            render={() => (
              <Space size='middle'>
                {/* <Link
                  to={`/dashboard/airlines/${record.id}`}
                  className='underline'
                >
                  View
                </Link> */}
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
          {renderAirlines()}
        </section>
      )}
    </main>
  )
}

export default ListAirlines
