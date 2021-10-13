import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useHistory } from 'react-router'
import { Table, Tag, Space } from 'antd'
import { FaPlus } from 'react-icons/fa'
import { useQueryClient } from 'react-query'

import Spinner from '../../components/common/Spinner'
import { useSessionStore } from '../../hooks/Store'
import useAirports from '../../hooks/useAirports'
import AirportService from '../../api/AirportService'

const { Column } = Table

const ListAirports = () => {
  const token = useSessionStore((state) => state.token)
  const user = useSessionStore((state) => state.user)
  const history = useHistory()
  const [validUser, setValidUser] = useState(false)

  const resetSession = useSessionStore((state) => state.resetSession)
  const queryClient = useQueryClient()

  useEffect(() => {
    if (!token || !user || user.userType !== 'administrator') {
      setValidUser(false)
      resetSession()
      queryClient.clear()
      history.push('/')
    } else {
      setValidUser(true)
    }

    // console.log('Is valid user:', validUser)
  }, [token, user, history, resetSession, queryClient])

  /* -------------------------------------------------------------------------- */

  const {
    data: airports,
    isLoading,
    isError,
    error,
    isSuccess,
    refetch: refetchAirports
  } = useAirports()

  useEffect(() => {
    refetchAirports()
  }, [refetchAirports])

  useEffect(() => {
    if (error && error.response && error.response.status === 401) {
      resetSession()
      queryClient.clear()
      history.push('/')
    }
  }, [error, history, queryClient, resetSession])

  /* -------------------------------------------------------------------------- */

  const heading = (
    <header className='flex items-center justify-between'>
      <h2 className='text-3xl font-bold'>Your airports</h2>
      <Link
        data-cy='add-new-airport-button'
        to='/dashboard/create/airports'
      >
        <button
          type='button'
          className='flex items-center justify-center gap-2 p-2 font-bold text-white transition-colors bg-yellow-600 hover:bg-yellow-500'
        >
          <FaPlus />
          <span>Add New Airport</span>
        </button>
      </Link>
    </header>
  )

  const sort = (a, b) => {
    if (a < b) return -1
    if (a > b) return 1
    return 0
  }

  const [isUpdating, setIsUpdating] = useState(null)

  const updateAirport = (id, status) => {
    setIsUpdating(id)

    AirportService.updateAirport({
      data: { token, id, status },
      onSuccess: () => {
        refetchAirports()
          .then(() => {
            setIsUpdating(null)
          })
          .catch(() => {
            // console.log(err)
            setIsUpdating(null)
          })
      },
      onError: (err) => {
        setIsUpdating(null)

        if (
          err.response &&
          err.response.status &&
          err.response.status === 401
        ) {
          resetSession()
          queryClient.clear()
          history.push('/login')
        }
        // console.log(err)
      }
    })
  }

  const renderAirports = () => {
    if (isLoading) {
      return <p>Loading...</p>
    }

    if (isSuccess && airports) {
      const dataSource = airports.map((airport, idx) => ({
        key: `${idx}`,
        id: airport.id,
        name: airport.name,
        code: airport.code,
        location: airport.location,
        zoneId: airport.zoneId,
        status: airport.status
      }))

      return (
        <Table
          dataSource={dataSource}
          bordered
          size='middle'
          scroll={{ x: 700, y: 400 }}
          pagination={false}
        >
          <Column
            title='Name'
            dataIndex='name'
            sorter={{
              compare: (a, b) => sort(a.name, b.name)
            }}
            defaultSortOrder='ascend'
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
            title='Location'
            dataIndex='location'
            sorter={{
              compare: (a, b) => sort(a.code, b.code)
            }}
          />
          <Column
            title='Zone ID'
            dataIndex='zoneId'
            width={180}
            sorter={{
              compare: (a, b) => sort(a.code, b.code)
            }}
          />
          <Column
            title='Status'
            dateIndex='status'
            render={(text, record) => {
              let colour = 'green'

              if (record.status === 'INACTIVE') {
                colour = 'volcano'
              }

              return (
                <Tag color={colour} key={record.status}>
                  {record.status}
                </Tag>
              )
            }}
          />
          <Column
            title='Actions'
            key='actions'
            fixed='right'
            render={(value, record) => (
              <Space size='middle'>
                {record.id !== isUpdating && (
                  <>
                    {record.status === 'ACTIVE' ? (
                      <button
                        type='button'
                        onClick={() =>
                          updateAirport(record.id, 'INACTIVE')
                        }
                        className='text-blue-400 underline transition-colors active:text-red-600 hover:text-red-400'
                      >
                        Disable
                      </button>
                    ) : (
                      <button
                        type='button'
                        onClick={() =>
                          updateAirport(record.id, 'ACTIVE')
                        }
                        className='text-blue-400 underline transition-colors active:text-green-600 hover:text-green-300'
                      >
                        Enable
                      </button>
                    )}
                  </>
                )}
                {record.id === isUpdating && <Spinner size={4} />}
              </Space>
            )}
          />
        </Table>
      )
    }

    if (isError) {
      return <p>Something went wrong</p>
    }

    return <p>Loading...</p>
  }

  return (
    <main className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'>
      {!validUser ? (
        <p>Loading...</p>
      ) : (
        <section className='flex flex-col w-full h-full max-w-3xl gap-4'>
          {heading}
          {renderAirports()}
        </section>
      )}
    </main>
  )
}

export default ListAirports
