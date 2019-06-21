import {
  USER_CHECK,
  USER_CHECK_SUCCESS,
  USER_CHECK_ERROR,
  USER_LOGIN,
  USER_LOGIN_SUCCESS,
  USER_LOGIN_ERROR,
  USER_LOGOUT,
  USER_LOGOUT_SUCCESS,
  USER_LOGOUT_ERROR,
} from '../constants/auth';
import auth from './auth';

const initialState = {
  id: null,
  name: null,
  roles: [],
  isLoggedIn: false,
  inProgress: false,
};

describe('Тесты auth reducer', () => {
  it('Должен вернуть initialState', () => {
    expect(
      auth(null, {
        type: USER_CHECK,
      })
    ).toEqual(null);
  });
  it('Должен вернуть данные пользователя и залогинить его', () => {
    expect(
      auth(null, {
        type: USER_LOGIN_SUCCESS,
        payload: {
          id: 'userId',
          name: 'username',
          roles: [],
          isLoggedIn: false,
          inProgress: false,
        },
      })
    ).toEqual({
      id: 'userId',
      name: 'username',
      roles: [],
      isLoggedIn: true,
      inProgress: false,
    });
  });

  it('Должен вернуть initialState при logout', () => {
    expect(
      auth(null, {
        type: USER_LOGOUT_SUCCESS,
      })
    ).toEqual(initialState);
  });
});
