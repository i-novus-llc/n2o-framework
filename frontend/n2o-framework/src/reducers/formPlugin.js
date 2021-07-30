import produce from 'immer'
import has from 'lodash/has'
import set from 'lodash/set'
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

const ACTION_TYPES = [
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
]

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

// eslint-disable-next-line no-console
const warnNonExistent = (field, property) => console.warn(`Attempt to change "${property}" a non-existent field "${field}"`)

/**
 * Редюсер удаления/добваления алертов
 * @ignore
 */
/* eslint-disable consistent-return */
// eslint-disable-next-line complexity
export const formPlugin = produce((state, { type, payload, meta }) => {
    if (ACTION_TYPES.includes(type)) {
        state.registeredFields = state.registeredFields || {}
        state.registeredFields[payload.name] =
      state.registeredFields[payload.name] || {}
    }
    switch (type) {
        case REGISTER_FIELD_EXTRA: {
            const initialState = merge(defaultState, payload.initialState || {})

            Object.assign(state.registeredFields[payload.name], initialState)

            break
        }

        case DISABLE_FIELD: {
            const { name } = payload
            const field = state.registeredFields[name]

            if (!field) { return warnNonExistent(name, 'disabled') }

            field.disabled_field = true
            field.disabled = field.disabled_field || field.disabled_set

            break
        }
        case ENABLE_FIELD: {
            const { name } = payload
            const field = state.registeredFields[name]

            if (!field) { return warnNonExistent(name, 'disabled') }

            field.disabled_field = false
            field.disabled = field.disabled_field || field.disabled_set

            break
        }
        case SHOW_FIELD: {
            const { name } = payload
            const field = state.registeredFields[name]

            if (!field) { return warnNonExistent(name, 'visible') }

            field.visible_field = true
            field.visible = field.visible_field && field.visible_set

            break
        }
        case HIDE_FIELD: {
            const { name } = payload
            const field = state.registeredFields[name]

            if (!field) { return warnNonExistent(name, 'visible') }

            field.visible_field = false
            field.visible = field.visible_field && field.visible_set

            break
        }
        case ADD_FIELD_MESSAGE: {
            state.registeredFields[payload.name].message = state.registeredFields[payload.name].message || {}
            Object.assign(state.registeredFields[payload.name].message, payload.message)

            break
        }
        case REMOVE_FIELD_MESSAGE: {
            state.registeredFields[payload.name].message = null

            break
        }
        case REGISTER_DEPENDENCY: {
            state.registeredFields[payload.name].dependency = payload.dependency

            break
        }
        case SET_FIELD_FILTER: {
            state.registeredFields[payload.name].filter =
                state.registeredFields[payload.name].filter
                    .filter(f => f.filterId !== payload.filter.filterId)
                    .concat(payload.filter)

            break
        }

        case SET_REQUIRED: {
            state.registeredFields[payload.name].required = true

            break
        }

        case UNSET_REQUIRED: {
            state.registeredFields[payload.name].required = false

            break
        }

        case SET_LOADING: {
            state.registeredFields[payload.name].loading = payload.loading

            break
        }

        case SHOW_FIELDS: {
            payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                if (!field) { return warnNonExistent(name, 'visible') }

                field.visible_set = true
                field.visible = field.visible_field && field.visible_set
            })

            break
        }

        case HIDE_FIELDS: {
            payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                if (!field) { return warnNonExistent(name, 'visible') }

                field.visible_set = false
                field.visible = field.visible_field && field.visible_set
            })

            break
        }

        case DISABLE_FIELDS: {
            payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                if (!field) { return warnNonExistent(name, 'disabled') }

                field.disabled_set = true
                field.disabled = field.disabled_field || field.disabled_set
            })

            break
        }

        case ENABLE_FIELDS: {
            payload.names.forEach((name) => {
                const field = state.registeredFields[name]

                if (!field) { return warnNonExistent(name, 'disabled') }

                field.disabled_set = false
                field.disabled = field.disabled_field || field.disabled_set
            })

            break
        }

        case actionTypes.CHANGE: {
            const { field } = meta

            if (!field) {
                break
            }
            const customFormAction = has(payload, 'keepDirty')
            const value = customFormAction ? payload.value : payload

            /*
             * TODO придумать как аккуратно отказаться от _.set
             *  сейчас он раскручивает поля ввида values[field[index].property]
             */
            set(state, `values[${field}]`, value)

            if (customFormAction && !payload.keepDirty) {
                set(state, `initial[${field}]`, payload.value)
            }

            break
        }

        default:
            break
    }
}, defaultState)

export default formPlugin
