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

  static createAirport(token, airport) {
    return request({
      options: {
        url: '/airport',
        method: 'POST',
        data: airport,
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onError: (err) => {
        console.log('Error creating airport:', err.response)
      }
    })
  }
}
