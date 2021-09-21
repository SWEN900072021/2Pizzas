import axios from 'axios'

const API_URL = process.env.REACT_APP_API_URL

const login = ({ username, password }) => {
  const user = { username, password }

  return axios.post(`${API_URL}/login`, user)
}

const signup = ({
  username,
  password,
  email,
  givenName,
  surname
}) => {
  const user = { username, password, email, givenName, surname }

  return axios.post(`${API_URL}/signup`, user)
}

export { API_URL, login, signup }
