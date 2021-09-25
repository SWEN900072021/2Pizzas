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
      },
      onError: (err) => {
        console.log('Error getting all users:', err.response)
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
      },
      onError: (err) => {
        console.log('Error creating admin:', err.response)
      }
    })
  }
}
