import axios from 'axios'

const URL = process.env.REACT_APP_API_URL

const login = ({ username, password }) => {
  const user = { username, password }

  return axios.post(`${URL}/login`, user)
}

const signup = ({
  username,
  password,
  email,
  givenName,
  surname
}) => {
  const user = { username, password, email, givenName, surname }

  return axios.post(`${URL}/signup`, user)
}

export { login, signup }
