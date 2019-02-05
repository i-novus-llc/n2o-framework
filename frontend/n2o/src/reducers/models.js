import { omit, map, isArray, isObject, merge, set, pick, each } from 'lodash';
import {
  SET,
  REMOVE,
  REMOVE_ALL,
  SYNC,
  UPDATE,
  UPDATE_MAP,
  MERGE,
  COPY,
  CLEAR
} from '../constants/models';
import { omitDeep } from '../tools/helpers';

/**
 * Префиксы моделей в N2O
 * @see https://n2o.i-novus.ru/react/docs/manual/#_%D0%9C%D0%BE%D0%B4%D0%B5%D0%BB%D0%B8_%D0%B2%D0%B8%D0%B4%D0%B6%D0%B5%D1%82%D0%B0
 * @type {Object}
 * @property {object} datasource
 * @property {object} select
 * @property {object} filter
 * @property {object} multi
 * @property {object} resolve
 * @property {object} edit
 */
const modelState = {
  /* Модели от сервера */
  datasource: {},
  /* Модели для клиента */
  select: {},
  filter: {},
  multi: {},
  resolve: {},
  edit: {}
};

/**
 * Функция определяет тип модели и возвращает такой же тип
 * @param state
 * @param action
 */
function resolveUpdate(state, action) {
  const copyState = { ...state };
  if (isArray(state[action.payload.key])) {
    set(copyState[action.payload.key], action.payload.field, action.payload.value);
    return [...copyState[action.payload.key]];
  }
  if (isObject(state[action.payload.key])) {
    set(copyState[action.payload.key], action.payload.field, action.payload.value);
    return {
      ...copyState[action.payload.key]
    };
  }

  return {
    ...copyState,
    ...set({}, action.payload.field, action.payload.value)
  };
}

function resolve(state, action) {
  switch (action.type) {
    case SET:
      return Object.assign({}, state, {
        [action.payload.key]: action.payload.model
      });
    case REMOVE:
      return omit(state, action.payload.key);
    case SYNC:
      return Object.assign(
        {},
        state,
        action.payload.keys.map(key => ({ [key]: action.payload.model }))
      );
    case UPDATE:
      return {
        ...state,
        [action.payload.key]: resolveUpdate(state, action)
      };
    case UPDATE_MAP:
      return {
        ...state,
        [action.payload.key]: {
          ...state[action.payload.key],
          [action.payload.field]:
            isArray(action.payload.value) &&
            map(action.payload.value, v => ({ [action.payload.map]: v }))
        }
      };
    case COPY:
      return {
        ...state[action.payload.target.prefix],
        [action.payload.target.key]: {
          ...state[action.payload.source.prefix][action.payload.source.key]
        }
      };
    case CLEAR:
      return {
        ...state,
        [action.payload.key]: {
          ...pick(state[action.payload.key], [action.payload.exclude])
        }
      };
    default:
      return state;
  }
}

/**
 * Редюсер для моделей
 * @ignore
 */
export default function models(state = modelState, action) {
  switch (action.type) {
    case SET:
    case REMOVE:
    case SYNC:
    case UPDATE:
    case UPDATE_MAP:
      return Object.assign({}, state, {
        [action.payload.prefix]: resolve(state[action.payload.prefix], action)
      });
    case COPY:
      return Object.assign({}, state, {
        [action.payload.target.prefix]: resolve(state, action)
      });
    case MERGE:
      return { ...merge(state, action.payload.combine) };
    case REMOVE_ALL:
      return {
        ...state,
        ...omitDeep(state, [action.payload.key])
      };
    case CLEAR:
      const res = {};
      each(action.payload.prefixes, prefix => {
        res[prefix] = {
          ...state[prefix],
          [action.payload.key]: {
            ...pick(state[prefix][action.payload.key], [action.payload.exclude])
          }
        };
      });
      return {
        ...state,
        ...res
      };
    default:
      return state;
  }
}
