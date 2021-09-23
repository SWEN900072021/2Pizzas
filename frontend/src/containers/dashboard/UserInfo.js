import React from 'react'
// import { useHistory } from 'react-router'
// import { Link } from 'react-router-dom'
// import { NavLink } from 'react-router-dom'

// Hooks
// import Spinner from '../components/Spinner'
import { useSessionStore } from '../../hooks/Store'
// import useUsers from '../hooks/useUsers'

// Containers and Components
// import NavBar from '../components/NavBar'
// import Spinner from '../components/Spinner'

const UserInfo = () => {
  const username = useSessionStore((state) => state.username)

  const userDetails = <div>Your username is: {username}</div>

  return <main>{userDetails}</main>
}

export default UserInfo
