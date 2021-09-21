import axios from 'axios'

const API_URL = process.env.REACT_APP_API_URL

// React Query + Axios: https://alexstreza.hashnode.dev/data-fetching-with-react-query-and-axios
const client = (() =>
  axios.create({
    baseURL: API_URL
  }))()

// options format: https://axios-http.com/docs/req_config
const request = async ({ options, onSuccess, onError }) => {
  const onSuccessFn =
    onSuccess ||
    ((response) => {
      const { data } = response
      return data
    })

  const onErrorFn =
    onError || ((error) => Promise.reject(error.response))

  return client(options).then(onSuccessFn).catch(onErrorFn)
}

export default request
