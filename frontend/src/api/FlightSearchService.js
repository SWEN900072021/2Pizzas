import request from './request'

export default class FlightSearchService {
  static searchFlights({
    data: {
      destination,
      origin,
      departingAfter,
      departingBefore,
      airline
    },
    onSuccess,
    onError
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
      onSuccess,
      onError
    })
  }
}
