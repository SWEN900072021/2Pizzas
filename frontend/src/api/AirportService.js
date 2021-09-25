import request from './request'

export default class AirportService {
  static getAllAirports() {
    return request({
      options: {
        url: '/airport',
        method: 'GET'
      },
      onError: (err) => {
        console.log('Error getting all airports:', err.response)
      }
    })
  }

  static updateAirport({
    data: { token, id, status },
    onSuccess,
    onError
  }) {
    return request({
      options: {
        url: `/airport/${id}`,
        method: 'PATCH',
        data: { status },
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onSuccess,
      onError
    })
  }

  static createAirport({
    data: { token, airport },
    onSuccess,
    onError
  }) {
    return request({
      options: {
        url: '/airport',
        method: 'POST',
        data: airport,
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onSuccess,
      onError
    })
  }
}
