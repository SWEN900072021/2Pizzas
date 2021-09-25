import request from './request'

export default class FlightSearchService {
  static searchFlights({
    destination,
    origin,
    departingAfter,
    departingBefore,
    airline
  }) {
    return request({
      options: {
        url: '/search/flight',
        method: 'GET',
        params: {
          destination,
          origin,
          departingAfter,
          departingBefore,
          airline
        }
      },
      onError: (err) => {
        console.log('Error searching flights:', err.response)
      }
    })
  }
}
