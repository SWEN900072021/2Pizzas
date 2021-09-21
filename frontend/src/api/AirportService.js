import request from './request'

export default class AirportService {
  static getAllAirports() {
    return request({
      options: {
        url: '/airport',
        method: 'GET'
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
      }
    })
  }
}
