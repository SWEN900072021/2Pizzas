import request from './request'

export default class FlightService {
  static createFlight({
    data: { token, flight },
    onSuccess,
    onError
  }) {
    return request({
      options: {
        url: '/flight',
        method: 'POST',
        data: flight,
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onSuccess,
      onError
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
      },
      onError: (err) => {
        console.log('Error getting all flights:', err.response)
      }
    })
  }

  static getFlightById(token, id) {
    return request({
      options: {
        url: `/flight/${id}`,
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onError: (err) => {
        console.log('Error getting flight by Id:', err.response)
      }
    })
  }

  static updateFlight({
    data: { token, id, flight },
    onSuccess,
    onError
  }) {
    return request({
      options: {
        url: `/flight/${id}`,
        method: 'PATCH',
        data: flight,
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onSuccess,
      onError:
        onError ||
        ((err) => {
          console.log('Error updating flight:', err.response)
        })
    })
  }

  static getFlightPassengers(token, flightId) {
    return request({
      options: {
        url: `/flight/${flightId}/passenger`,
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onError: (err) => {
        console.log('Error getting flight passengers:', err.response)
      }
    })
  }
}
