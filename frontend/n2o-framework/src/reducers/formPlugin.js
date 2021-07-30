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

export const defaultState = {
    isInit: true,
    visible: true,
    visible_field: true,
    visible_set: true,
    disabled: false,
    disabled_field: false,
    disabled_set: false,
    message: null,
    filter: [],
    dependency: null,
    required: false,
    loading: false,
}

function resolve(state = defaultState, action) {
    switch (action.type) {
        case REGISTER_FIELD_EXTRA:
            return {

                ...state,
                ...merge(defaultState, action.payload.initialState || {}),
            }
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

// eslint-disable-next-line no-console
const warnNonExistent = (field, property) => console.warn(`Attempt to change "${property}" a non-existent field "${field}"`)

/**
 * Редюсер удаления/добваления алертов
 * @ignore
 */
/* eslint-disable consistent-return */
// eslint-disable-next-line consistent-return
export default function formPlugin(state = {}, action) {
    // ToDo: Переписать
    switch (action.type) {
        case REGISTER_FIELD_EXTRA:
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
        case actionTypes.CHANGE:
            return resolveChange(state, action)
        case DISABLE_FIELD: {
            const { name } = action.payload
            const field = state.registeredFields[name]

            if (!field) { return warnNonExistent(name, 'disabled') }

            field.disabled_field = true
            field.disabled = field.disabled_field || field.disabled_set

            break
        }
        case ENABLE_FIELD: {
            const { name } = action.payload
            const field = state.registeredFields[name]

            if (!field) { return warnNonExistent(name, 'disabled') }

            field.disabled_field = false
            field.disabled = field.disabled_field || field.disabled_set

            break
        }
        case SHOW_FIELD: {
            const { name } = action.payload
            const field = state.registeredFields[name]

            if (!field) { return warnNonExistent(name, 'visible') }

            field.visible_field = true
            field.visible = field.visible_field && field.visible_set

            break
        }
        case HIDE_FIELD: {
            const { name } = action.payload
            const field = state.registeredFields[name]

            if (!field) { return warnNonExistent(name, 'visible') }

            field.visible_field = false
            field.visible = field.visible_field && field.visible_set

            break
        }
        case SHOW_FIELDS: {
            action.payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                if (!field) { return warnNonExistent(name, 'visible') }

                field.visible_set = true
                field.visible = field.visible_field && field.visible_set
            })

            break
        }
        case HIDE_FIELDS: {
            action.payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                if (!field) { return warnNonExistent(name, 'visible') }

                field.visible_set = false
                field.visible = field.visible_field && field.visible_set
            })

            break
        }

        case DISABLE_FIELDS: {
            action.payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                if (!field) { return warnNonExistent(name, 'disabled') }

                field.disabled_set = true
                field.disabled = field.disabled_field || field.disabled_set
            })

            break
        }

        case ENABLE_FIELDS: {
            action.payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                if (!field) { return warnNonExistent(name, 'disabled') }

                field.disabled_set = false
                field.disabled = field.disabled_field || field.disabled_set
            })

            break
        }
    }

    return state
}
