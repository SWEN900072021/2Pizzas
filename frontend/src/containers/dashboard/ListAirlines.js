import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useHistory } from 'react-router'
import { Table, Space, Tag } from 'antd'
import { FaPlus } from 'react-icons/fa'
import { useQueryClient } from 'react-query'

import Spinner from '../../components/common/Spinner'
import { useSessionStore } from '../../hooks/Store'
import useAirlines from '../../hooks/useAirlines'
import UserService from '../../api/UserService'

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
    refetchAirlines()
  }, [refetchAirlines])

  const heading = (
    <header className='flex items-center justify-between'>
      <h2 className='text-3xl font-bold'>Your airlines</h2>
      <Link to='/dashboard/create/airlines'>
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

  const [isUpdating, setIsUpdating] = useState(null)

  const queryClient = useQueryClient()
  const resetSession = useSessionStore((st) => st.resetSession)

  const updateAirline = (id, status) => {
    setIsUpdating(id)

    UserService.updateUser({
      data: { token, id, status },
      onSuccess: () => {
        refetchAirlines()
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
          queryClient.clear()
          resetSession()
          history.push('/login')
        }

        // console.log(err)
      }
    })
  }

  const renderAirlines = () => {
    if (isLoading) {
      return <p>Loading...</p>
    }

    if (isSuccess && airlines) {
      const dataSource = airlines.map((airline, idx) => ({
        key: `${idx}`,
        id: airline.id,
        name: airline.name,
        code: airline.code,
        status: airline.status
      }))

      return (
        <Table
          dataSource={dataSource}
          bordered
          size='middle'
          scroll={{ x: 400, y: 400 }}
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
            width={50}
            render={(value, record) => (
              <Space size='middle'>
                {record.id !== isUpdating && (
                  <>
                    {record.status === 'ACTIVE' ? (
                      <button
                        type='button'
                        onClick={() =>
                          updateAirline(record.id, 'INACTIVE')
                        }
                        className='text-blue-400 underline transition-colors active:text-red-600 hover:text-red-400'
                      >
                        Disable
                      </button>
                    ) : (
                      <button
                        type='button'
                        onClick={() =>
                          updateAirline(record.id, 'ACTIVE')
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
    <main
      in
      className='flex items-start justify-center w-full h-full px-5 py-8 md:items-center'
    >
      {!validUser ? (
        <p>Loading...</p>
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
