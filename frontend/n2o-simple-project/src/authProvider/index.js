import { map, values, intersection, isEmpty, indexOf } from 'lodash';
import {
  SECURITY_LOGIN,
  SECURITY_LOGOUT,
  SECURITY_ERROR,
  SECURITY_CHECK,
} from 'n2o/lib/core/auth/authTypes';

function checkPermission(cfg = {}, user = {}) {
  if (cfg.denied) return false;
  if (cfg.permitAll) return true;
  if (isEmpty(user)) {
    return !!(cfg.anonimous);
  } else {
    if (cfg.authentificated) return true;
    // cfg: { roles: ['moder', 'user'], permissions: [], usernames: [] }
    // roles OR permissions OR usernames
    /*
     "user" : {
       "username" : "test",
       "roles" : ["user", "admin"],
       "permissions" : ["read", "edit"]
     }
     */
    return !isEmpty(intersection(cfg.roles, user.roles)) ||
      !isEmpty(intersection(cfg.permissions, user.permissions)) ||
      !isEmpty(intersection(cfg.usernames, [user.username]));
  }
}

export default (type, params) => {
  switch (type) {
    case SECURITY_LOGIN:
      return Promise.resolve(params);
    case SECURITY_LOGOUT:
      return Promise.resolve();
    case SECURITY_ERROR:
      return Promise.reject();
      break;
    case SECURITY_CHECK:
      // Promise.resolve() - true, т.е. есть доступ
      // Promise.reject() - false, нет доступа
      const {
        config,
        user
      } = params;
      // values(config);
      const res = indexOf(map(values(config), (cfg) => checkPermission(cfg, user)), false);
      return (res === -1) ? Promise.resolve(config) : Promise.reject();
      break;
    default:
      return Promise.reject('Неверно задан тип для authProvider!');
  }
};