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
      }
    })
  }

  static createBooking(token, booking, onSuccess, onError) {
    return request({
      options: {
        url: '/booking',
        method: 'POST',
        data: booking,
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onSuccess,
      onError
    })
  }
}
