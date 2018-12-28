import { isEmpty, isEqual } from 'lodash';
import {
  SECURITY_LOGIN,
  SECURITY_LOGOUT,
  SECURITY_ERROR,
  SECURITY_CHECK,
} from '../../src/core/auth/authTypes';

export default (type, params) => {
  switch (type) {
    case SECURITY_LOGIN:
      return Promise.resolve(params);
    case SECURITY_LOGOUT:
      return Promise.resolve();
    case SECURITY_ERROR:
      return Promise.reject();
      break
    case SECURITY_CHECK:
      if (!isEmpty(params.user && params.user.roles) && !isEmpty(params.config)) {
        if (params.user.username === 'admin') {
          return Promise.resolve(params.user.roles);
        } else {
          return Promise.reject();
        }
      } else if (!isEmpty(params.user && params.user.roles)) {
        return Promise.resolve(params.user.roles);
      } else {
        return Promise.reject();
      }
    default:
      return Promise.reject('WTF!?');
  }
};