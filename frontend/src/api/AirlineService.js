import request from './request'

export default class AirlineService {
  static getAllAirlines() {
    return request({
      options: {
        method: 'GET',
        url: '/airline'
      },
      onError: (err) => {
        console.log('Error getting all airlines:', err.response)
      }
    })
  }

  static createAirline({
    data: { token, airline },
    onSuccess,
    onError
  }) {
    return request({
      options: {
        method: 'POST',
        url: '/airline',
        data: airline,
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onSuccess,
      onError
    })
  }
}
