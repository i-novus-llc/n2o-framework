import map from 'lodash/map';
import values from 'lodash/values';
import intersection from 'lodash/intersection';
import isEmpty from 'lodash/isEmpty';
import indexOf from 'lodash/indexOf';
import {
  SECURITY_LOGIN,
  SECURITY_LOGOUT,
  SECURITY_ERROR,
  SECURITY_CHECK
} from './authTypes';

export function checkPermission(cfg = {}, user = {}) {
  if (cfg.denied) return false;
  if (cfg.permitAll) return true;
  if (cfg.anonymous) return isEmpty(user.username);
  if (!isEmpty(cfg.permissions) && isEmpty(user.permissions)) return false;
  if (!isEmpty(user.username)) {
    if (cfg.authenticated) return true;
    return !isEmpty(intersection(cfg.roles, user.roles)) ||
      !isEmpty(intersection(cfg.permissions, user.permissions)) ||
      !isEmpty(intersection(cfg.usernames, [user.username]));
  }
  return false;
}

export default (type, params) => {
  switch (type) {
    case SECURITY_LOGIN:
      return Promise.resolve(params);
    case SECURITY_LOGOUT:
      return Promise.resolve(params);
    case SECURITY_ERROR:
      const { status } = params;
      return status === 401
        ? Promise.reject(params)
        : Promise.resolve(params);
    case SECURITY_CHECK:
      const { config, user } = params;
      const res = indexOf(map(values(config), cfg => checkPermission(cfg, user)), false);
      return res === -1 ? Promise.resolve(config) : Promise.reject('Нет доступа.');
      break;
    default:
      return Promise.reject('Неверно задан тип для authProvider!');
  }
};
