import axios from 'axios'
import { API_URL } from './index'

// React Query + Axios: https://alexstreza.hashnode.dev/data-fetching-with-react-query-and-axios
const client = (() =>
  axios.create({
    baseURL: API_URL
  }))()

// options format: https://axios-http.com/docs/req_config
const request = async (options) => {
  const onSuccess = (response) => {
    const { data } = response
    return data
  }

  const onError = (error) => Promise.reject(error.response)

  return client(options).then(onSuccess).catch(onError)
}

export default request
