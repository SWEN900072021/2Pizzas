import request from './request'

export default class BookingService {
  static getCustomerBookings(token) {
    return request({
      options: {
        url: '/customer/booking',
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onError: (err) => {
        console.log('Error getting customer booking:', err.response)
      }
    })
  }

  static createBooking(token, booking) {
    return request({
      options: {
        url: '/booking',
        method: 'POST',
        data: booking,
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onError: (err) => {
        console.log('Error creating booking:', err.response)
      }
    })
  }
}
