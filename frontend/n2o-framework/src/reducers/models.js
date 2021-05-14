import omit from 'lodash/omit'
import mapFn from 'lodash/map'
import isArray from 'lodash/isArray'
import isObject from 'lodash/isObject'
import merge from 'lodash/merge'
import pick from 'lodash/pick'
import each from 'lodash/each'
import isString from 'lodash/isString'

import {
    SET,
    REMOVE,
    REMOVE_ALL,
    SYNC,
    UPDATE,
    UPDATE_MAP,
    MERGE,
    CLEAR,
    PREFIXES,
} from '../constants/models'
import { omitDeep, setIn } from '../tools/helpers'

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
    edit: {},
    selectionType: {},
}

/**
 * Функция определяет тип модели и возвращает такой же тип
 * @param state
 * @param action
 */
function resolveUpdate(state, action) {
    const { key, field, value } = action.payload

    if (!field) { return state }

    if (isArray(state[key])) {
        return setIn(state[key], field, value)
    }
    if (isObject(state[key])) {
        return setIn(state[key], field, value)
    }

    return setIn(state, field, value)
}

function resolve(state, action) {
    switch (action.type) {
        case SET: {
            return { ...state, [action.payload.key]: action.payload.model }
        }
        case REMOVE: {
            return omit(state, action.payload.key)
        }
        case SYNC: {
            return {

                ...state,
                ...action.payload.keys.map(key => ({ [key]: action.payload.model })),
            }
        }
        case UPDATE: {
            return {
                ...state,
                [action.payload.key]: resolveUpdate(state, action),
            }
        }
        case UPDATE_MAP: {
            const { value, key, field, map } = action.payload
            const newValue = isString(value) ? [value] : value

            return setIn(state, [key, field], mapFn(newValue, v => ({ [map]: v })))
        }
        case CLEAR: {
            return {
                ...state,
                [action.payload.key]: {
                    ...pick(state[action.payload.key], [action.payload.exclude]),
                },
            }
        }
        default:
            return state
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
        case UPDATE_MAP: {
            return { ...state, [action.payload.prefix]: resolve(state[action.payload.prefix], action) }
        }
        case MERGE: {
            return { ...merge(state, action.payload.combine) }
        }
        case REMOVE_ALL: {
            return {
                ...state,
                ...omitDeep(omit(state, PREFIXES.filter), [action.payload.key]),
            }
        }
        case CLEAR: {
            const res = {}

            each(action.payload.prefixes, (prefix) => {
                res[prefix] = {
                    ...state[prefix],
                    [action.payload.key]: {
                        ...pick(state[prefix][action.payload.key], [
                            action.payload.exclude,
                        ]),
                    },
                }
            })

            return {
                ...state,
                ...res,
            }
        }
        default:
            return state
    }
}
