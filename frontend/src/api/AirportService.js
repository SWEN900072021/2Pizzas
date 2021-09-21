import request from './request'

export default class AirportService {
  static getAllAirports() {
    return request({
      url: '/airport',
      method: 'GET'
    })
  }
}
