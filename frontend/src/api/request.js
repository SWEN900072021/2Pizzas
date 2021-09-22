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
    onError ||
    ((error) => {
      if (error.status === 401) {
        const session = {
          ...JSON.parse(sessionStorage.getItem('session-store')),
          token: null
        }
        sessionStorage.setItem(
          'session-store',
          JSON.stringify(session)
        )

        window.location.reload()
      } else {
        Promise.reject(error.response)
      }
    })

  return client(options).then(onSuccessFn).catch(onErrorFn)
}

export default request
