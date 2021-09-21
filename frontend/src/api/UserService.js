import request from './request'

export default class UserService {
  static getAllUsers(token) {
    return request({
      options: {
        url: '/user',
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    })
  }

  static createAdmin(token, admin) {
    return request({
      options: {
        url: '/admin',
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`
        },
        data: admin
      }
    })
  }
}
