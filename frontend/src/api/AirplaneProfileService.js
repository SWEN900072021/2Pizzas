import request from './request'

export default class AirplaneProfileService {
  static getAllAirplaneProfiles() {
    return request({
      options: {
        url: '/airplane-profile',
        method: 'GET'
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
      }
    })
  }
}
