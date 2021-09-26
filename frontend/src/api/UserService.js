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

  static updateUser({
    data: { token, id, status },
    onSuccess,
    onError
  }) {
    return request({
      options: {
        url: `/user/${id}`,
        method: 'PATCH',
        headers: {
          Authorization: `Bearer ${token}`
        },
        data: { status }
      },
      onSuccess,
      onError
    })
  }
}
