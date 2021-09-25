import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useHistory } from 'react-router'
import { Table, Space } from 'antd'
import { FaPlus } from 'react-icons/fa'

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

  const {
    data: airports,
    isLoading,
    isError,
    isSuccess,
    refetch: refetchAirports
  } = useAirports()

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
    if (!airports) {
      refetchAirports()
    }
  }, [airports, refetchAirports])

  const heading = (
    <header className='flex items-center justify-between'>
      <h2 className='text-3xl font-bold'>Your airports</h2>
      <Link to='/dashboard/manage/airports/create'>
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
      onError: () => {
        setIsUpdating(null)
        // console.log(err)
      }
    })
  }

  const renderAirports = () => {
    if (isLoading) {
      return <Spinner size={6} />
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
            sorter={{
              compare: (a, b) => sort(a.code, b.code)
            }}
          />
          <Column
            title='Actions'
            key='actions'
            fixed='right'
            width={80}
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
                {record.id === isUpdating && <Spinner size={6} />}
              </Space>
            )}
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
          {renderAirports()}
        </section>
      )}
    </main>
  )
}

export default ListAirports
