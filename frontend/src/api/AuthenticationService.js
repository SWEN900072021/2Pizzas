import request from './request'

export default class AuthenticationService {
  static login({ data: { username, password }, onSuccess, onError }) {
    return request({
      options: {
        url: '/login',
        method: 'POST',
        data: {
          username,
          password
        }
      },
      onSuccess,
      onError
    })
  }

  static signup({
    data: { username, password, givenName, surname, email },
    onSuccess,
    onError
  }) {
    return request({
      options: {
        url: '/signup',
        method: 'POST',
        data: {
          username,
          password,
          givenName,
          surname,
          email
        }
      },
      onSuccess,
      onError
    })
  }
}
