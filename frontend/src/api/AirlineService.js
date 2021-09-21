import request from './request'

export default class AirlineService {
  static getAllAirlines() {
    return request({
      options: {
        method: 'GET',
        url: '/airline'
      }
    })
  }

  static createAirline(token, airline) {
    return request({
      options: {
        method: 'POST',
        url: '/airline',
        data: airline,
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    })
  }
}
