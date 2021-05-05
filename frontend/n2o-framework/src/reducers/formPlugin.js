import reduce from 'lodash/reduce'
import has from 'lodash/has'
import set from 'lodash/set'
import get from 'lodash/get'
import merge from 'deepmerge'
import { actionTypes } from 'redux-form'

import {
    DISABLE_FIELD,
    ENABLE_FIELD,
    SHOW_FIELD,
    HIDE_FIELD,
    REGISTER_FIELD_EXTRA,
    ADD_FIELD_MESSAGE,
    REMOVE_FIELD_MESSAGE,
    REGISTER_DEPENDENCY,
    SET_FIELD_FILTER,
    SHOW_FIELDS,
    HIDE_FIELDS,
    ENABLE_FIELDS,
    DISABLE_FIELDS,
    SET_REQUIRED,
    UNSET_REQUIRED,
    SET_LOADING,
} from '../constants/formPlugin'

const defaultState = {
    isInit: true,
    visible: true,
    disabled: false,
    message: null,
    filter: [],
    dependency: null,
    required: false,
    loading: false,
}

const setValueByNames = (state, names, props) => ({

    ...state,
    ...reduce(
        names,
        (result, name) => ({
            ...result,
            [name]: { ...state[name], ...props },
        }),
        {},
    ),
})

function resolve(state = defaultState, action) {
    switch (action.type) {
        case REGISTER_FIELD_EXTRA:
            return {

                ...state,
                ...merge(defaultState, action.payload.initialState || {}),
            }
        case DISABLE_FIELD:
            return { ...state, disabled: true }
        case ENABLE_FIELD:
            return { ...state, disabled: false }
        case SHOW_FIELD:
            return { ...state, visible: true }
        case HIDE_FIELD:
            return { ...state, visible: false }
        case ADD_FIELD_MESSAGE:
            return { ...state, message: { ...action.payload.message } }
        case REMOVE_FIELD_MESSAGE:
            return { ...state, message: null }
        case REGISTER_DEPENDENCY:
            return { ...state, dependency: action.payload.dependency }
        case SET_FIELD_FILTER:
            return { ...state,
                filter: state.filter
                    .filter(f => f.filterId !== action.payload.filter.filterId)
                    .concat(action.payload.filter) }
        case SHOW_FIELDS:
            return setValueByNames(state, action.payload.names, { visible: true })
        case DISABLE_FIELDS:
            return setValueByNames(state, action.payload.names, { disabled: true })
        case ENABLE_FIELDS:
            return setValueByNames(state, action.payload.names, { disabled: false })
        case HIDE_FIELDS:
            return setValueByNames(state, action.payload.names, { visible: false })
        case SET_REQUIRED:
            return { ...state, required: true }
        case UNSET_REQUIRED:
            return { ...state, required: false }
        case SET_LOADING:
            return { ...state, loading: action.payload.loading }
        default:
            return state
    }
}

/**
 * Резолв изменения поля в redux form с учетом keepDirty
 * @param state
 * @param payload
 * @param meta
 * @returns {any}
 */
export function resolveChange(state, { payload, meta }) {
    const { field } = meta
    const newState = { ...state }

    if (has(payload, 'keepDirty')) {
        set(newState, `values[${field}]`, payload.value)
        if (!payload.keepDirty) {
            set(newState, `initial[${field}]`, payload.value)
        }
    } else {
        set(newState, `values[${field}]`, payload)
    }

    return newState
}

/**
 * Редюсер удаления/добваления алертов
 * @ignore
 */
// eslint-disable-next-line consistent-return
export default function formPlugin(state = {}, action) {
    // ToDo: Переписать
    switch (action.type) {
        case REGISTER_FIELD_EXTRA:
        case DISABLE_FIELD:
        case ENABLE_FIELD:
        case SHOW_FIELD:
        case HIDE_FIELD:
        case ADD_FIELD_MESSAGE:
        case REMOVE_FIELD_MESSAGE:
        case REGISTER_DEPENDENCY:
        case SET_FIELD_FILTER:
        case SET_REQUIRED:
        case UNSET_REQUIRED:
        case SET_LOADING:
            return set(
                state,
                ['registeredFields', action.payload.name],
                resolve(get(state, ['registeredFields', action.payload.name]), action),
            )
        case DISABLE_FIELDS:
        case HIDE_FIELDS:
            return set(
                state,
                'registeredFields',
                resolve(get(state, 'registeredFields'), action),
            )
        case actionTypes.CHANGE:
            return resolveChange(state, action)
        case ENABLE_FIELDS: {
            action.payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                // поля доступны только если у них нет своего условия на доступность
                if (
                    field.dependency &&
                    field.dependency.some(({ type }) => type === 'enabled')
                ) {
                    return
                }

                field.disabled = false
            })

            break
        }
        case SHOW_FIELDS: {
            action.payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                // показываем поля только если у них нет своего условия на видимость
                if (
                    field.dependency &&
                    field.dependency.some(({ type }) => type === 'visible')
                ) {
                    return
                }

                field.visible = true
            })

            break
        }
        default:
            return state
    }
}
