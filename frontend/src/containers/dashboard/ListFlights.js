import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router'
import Spinner from '../../components/Spinner'
import { useSessionStore } from '../../hooks/Store'

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

  return <main>{!validUser ? <Spinner size={6} /> : <div />}</main>
}

export default ListFlights
