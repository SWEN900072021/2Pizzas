import request from './request'

export default class AirplaneProfileService {
  static getAllAirplaneProfiles() {
    return request({
      options: {
        url: '/airplane-profile',
        method: 'GET'
      },
      onError: (err) => {
        console.log(
          'Error getting all airplane profiles:',
          err.response
        )
      }
    })
  }

  static createAirplaneProfile(token, airplaneProfile) {
    return request({
      options: {
        url: '/airplane-profile',
        method: 'POST',
        data: airplaneProfile,
        headers: {
          Authorization: `Bearer ${token}`
        }
      },
      onError: (err) => {
        console.log('Error creating airplane profile:', err.response)
      }
    })
  }
}
