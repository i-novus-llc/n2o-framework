import {
  USER_CHECK,
  USER_CHECK_ERROR,
  USER_CHECK_SUCCESS,
  USER_LOGIN,
  USER_LOGIN_ERROR,
  USER_LOGIN_SUCCESS,
  USER_LOGOUT,
  USER_LOGOUT_ERROR,
  USER_LOGOUT_SUCCESS,
} from '../constants/auth';
import {
  userCheck,
  userCheckError,
  userCheckSuccess,
  userLogin,
  userLoginError,
  userLoginSuccess,
  userLogout,
  userLogoutError,
  userLogoutSuccess,
} from './auth';

const payload = {
  login: 'test',
  password: 'test',
};

const meta = {
  auth: true,
};

describe('Тесты для экшенов auth', () => {
  describe('Проверяет экшен userCheck', () => {
    it('Генирирует правильное событие', () => {
      const action = userCheck(payload);
      expect(action.type).toEqual(USER_CHECK);
    });
    it('Возвращает правильный payload', () => {
      const action = userCheck(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userCheck(payload);
      expect(action.meta).toEqual(meta);
    });
  });

  describe('Проверяет экшен userCheckError', () => {
    it('Генирирует правильное событие', () => {
      const action = userCheckError(payload);
      expect(action.type).toEqual(USER_CHECK_ERROR);
    });
    it('Возвращает правильный payload', () => {
      const action = userCheckError(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userCheckError(payload);
      expect(action.meta).toEqual(meta);
    });
  });

  describe('Проверяет экшен userCheckSuccess', () => {
    it('Генирирует правильное событие', () => {
      const action = userCheckSuccess(payload);
      expect(action.type).toEqual(USER_CHECK_SUCCESS);
    });
    it('Возвращает правильный payload', () => {
      const action = userCheckSuccess(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userCheckSuccess(payload);
      expect(action.meta).toEqual(meta);
    });
  });

  describe('Проверяет экшен userLogin', () => {
    it('Генирирует правильное событие', () => {
      const action = userLogin(payload);
      expect(action.type).toEqual(USER_LOGIN);
    });
    it('Возвращает правильный payload', () => {
      const action = userLogin(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userLogin(payload);
      expect(action.meta).toEqual(meta);
    });
  });

  describe('Проверяет экшен userLoginError', () => {
    it('Генирирует правильное событие', () => {
      const action = userLoginError(payload);
      expect(action.type).toEqual(USER_LOGIN_ERROR);
    });
    it('Возвращает правильный payload', () => {
      const action = userLoginError(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userLoginError(payload);
      expect(action.meta).toEqual(meta);
    });
  });

  describe('Проверяет экшен userLoginSuccess', () => {
    it('Генирирует правильное событие', () => {
      const action = userLoginSuccess(payload);
      expect(action.type).toEqual(USER_LOGIN_SUCCESS);
    });
    it('Возвращает правильный payload', () => {
      const action = userLoginSuccess(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userLoginSuccess(payload);
      expect(action.meta).toEqual(meta);
    });
  });

  describe('Проверяет экшен userLogout', () => {
    it('Генирирует правильное событие', () => {
      const action = userLogout(payload);
      expect(action.type).toEqual(USER_LOGOUT);
    });
    it('Возвращает правильный payload', () => {
      const action = userLogout(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userLogout(payload);
      expect(action.meta).toEqual(meta);
    });
  });

  describe('Проверяет экшен userLogoutError', () => {
    it('Генирирует правильное событие', () => {
      const action = userLogoutError(payload);
      expect(action.type).toEqual(USER_LOGOUT_ERROR);
    });
    it('Возвращает правильный payload', () => {
      const action = userLogoutError(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userLogoutError(payload);
      expect(action.meta).toEqual(meta);
    });
  });

  describe('Проверяет экшен userLogoutSuccess', () => {
    it('Генирирует правильное событие', () => {
      const action = userLogoutSuccess(payload);
      expect(action.type).toEqual(USER_LOGOUT_SUCCESS);
    });
    it('Возвращает правильный payload', () => {
      const action = userLogoutError(payload);
      expect(action.payload).toEqual(payload);
    });
    it('Возвращаем правильную meta', () => {
      const action = userLogoutSuccess(payload);
      expect(action.meta).toEqual(meta);
    });
  });
});
