import request from './request'

export default class FlightService {
  static createFlight(token, flight) {
    return request({
      options: {
        url: '/flight',
        method: 'POST',
        data: flight,
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    })
  }

  static getAllFlights(token) {
    return request({
      options: {
        url: '/flight',
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    })
  }

  static getFlightById(token, id) {
    return request({
      url: `/flight/${id}`,
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
  }

  static updateFlight(token, id, flight) {
    return request({
      url: `/flight/${id}`,
      method: 'PUT',
      data: flight,
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
  }

  static getFlightPassengers(token, id) {
    return request({
      url: `/flight/${id}/passenger`,
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
  }
}
