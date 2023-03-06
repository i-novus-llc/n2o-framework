import { createSlice } from '@reduxjs/toolkit'
import merge from 'deepmerge'
import set from 'lodash/set'
import get from 'lodash/get'

import { removeFieldFromArray, updateModel } from '../models/store'

import FormPlugin from './FormPlugin'

const initialState = FormPlugin.defaultState

// eslint-disable-next-line no-console
const warnNonExistent = (field, property) => console.warn(`Attempt to change "${property}" a non-existent field "${field}"`)

/*
 * TODO выделить filedset'ы нормально в редакс и убрать поля:
 *  disabled_field, disabled_set, visible_field, visible_set
 *  которые нужны для отключения и скрытия полей в зависимсти от филдсетов и нормального возвращения к предыдущему состоянию
 */

/* eslint-disable consistent-return */
const formSlice = createSlice({
    name: 'n2o/formPlugin',
    initialState,
    reducers: {
        REGISTER_FIELD_EXTRA: {
            /**
             * @param {string} key
             * @param {string} name
             * @param {FormPluginStore.item} initialState
             */
            prepare(key, name, initialState) {
                return ({
                    payload: {
                        name,
                        key,
                        initialState,
                    },
                    meta: { key, name },
                })
            },

            /**
             * Регистрирование дополнительных свойств у поля формы
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             */
            reducer(state, action) {
                const { name, key, initialState = {} } = action.payload
                const formState = merge(FormPlugin.defaultState, initialState)
                const registeredInfo = get(state, [key, 'registeredFields', name], {})

                set(state, [key, 'registeredFields', name], Object.assign(registeredInfo, formState))
            },
        },

        unRegisterExtraField: {
            /**
             * @param {string} key
             * @param {string} name
             */
            prepare(key, name) {
                return ({
                    payload: {
                        name,
                        key,
                    },
                    meta: { key, name },
                })
            },

            reducer(state, action) {
                const { name, key } = action.payload

                delete state?.[key].registeredFields?.[name]
            },
        },

        DISABLE_FIELD: {
            /**
             * @param {string} key
             * @param {string} name
             */
            prepare(key, name) {
                return ({
                    payload: { key, name },
                    meta: { key },
                })
            },

            /**
             * Диактивации поля
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.disableFieldPayload} action.payload
             */
            reducer(state, action) {
                const { key, name } = action.payload
                const field = get(state, [key, 'registeredFields', name])

                if (!field) { return warnNonExistent(name, 'disabled') }

                field.disabled_field = true
                field.disabled = field.disabled_field || field.disabled_set
            },
        },

        ENABLE_FIELD: {
            /**
             * @param {string} key
             * @param {string} name
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, name) {
                return ({
                    payload: { key, name },
                    meta: { key },
                })
            },

            /**
             * Активация поля
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.enableFieldPayload} action.payload
             */
            reducer(state, action) {
                const { name, key } = action.payload
                const field = get(state, [key, 'registeredFields', name])

                if (!field) { return warnNonExistent(name, 'disabled') }

                field.disabled_field = false
                field.disabled = field.disabled_field || field.disabled_set
            },
        },

        SHOW_FIELD: {
            /**
             * @param {string} key
             * @param {string} name
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, name) {
                return ({
                    payload: { key, name },
                    meta: { key },
                })
            },

            /**
             * Показать поле
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.showFieldPayload} action.payload
             */
            reducer(state, action) {
                const { name, key } = action.payload
                const field = get(state, [key, 'registeredFields', name])

                if (!field) { return warnNonExistent(name, 'visible') }

                field.visible_field = true
                field.visible = field.visible_field && field.visible_set
            },
        },

        HIDE_FIELD: {
            /**
             * @param {string} key
             * @param {string} name
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, name) {
                return ({
                    payload: { key, name },
                    meta: { key },
                })
            },

            /**
             * Скрыть поле
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.hideFieldPayload} action.payload
             */
            reducer(state, action) {
                const { name, key } = action.payload
                const field = get(state, [key, 'registeredFields', name])

                if (!field) { return warnNonExistent(name, 'visible') }

                field.visible_field = false
                field.visible = field.visible_field && field.visible_set
            },
        },

        REGISTER_DEPENDENCY: {
            /**
             * @param {string} key
             * @param {string} name
             * @param {object} dependency
             */
            prepare(key, name, dependency) {
                return ({
                    payload: { key, name, dependency },
                    meta: { key },
                })
            },

            /**
             * Зарегестрировать зависимости поля от других полей
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.registerFieldDependencyPayload} action.payload
             */
            reducer(state, action) {
                const { name, dependency, key } = action.payload

                set(state, [key, 'registeredFields', name, 'dependency'], dependency)
            },
        },

        SET_REQUIRED: {
            /**
             * @param {string} key
             * @param {string} field
             */
            prepare(key, field) {
                return ({
                    payload: { key, field },
                    meta: { key, field },
                })
            },

            /**
             * Установить флаг обазяательного поля
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.setRequiredPayload} action.payload
             */
            reducer(state, action) {
                const { field, key } = action.payload

                set(state, [key, 'registeredFields', field, 'required'], true)
            },
        },

        UNSET_REQUIRED: {
            /**
             * @param {string} key
             * @param {string} field
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, field) {
                return ({
                    payload: { key, field },
                    meta: { key, field },
                })
            },

            /**
             * Снять флаг обазяательного поля
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.unsetRequiredPayload} action.payload
             */
            reducer(state, action) {
                const { field, key } = action.payload

                set(state, [key, 'registeredFields', field, 'required'], false)
            },
        },

        SET_LOADING: {
            /**
             * @param {string} key
             * @param {string} name
             * @param {boolean} loading
             */
            prepare(key, name, loading) {
                return ({
                    payload: { key, name, loading },
                    meta: { key },
                })
            },

            /**
             * Поставить флаг загрузки
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.setLoadingPayload} action.payload
             */
            reducer(state, action) {
                const { name, loading, key } = action.payload

                set(state, [key, 'registeredFields', name, 'loading'], loading)
            },
        },

        SHOW_FIELDS: {
            /**
             * @param {string} key
             * @param {any[]} names
             */
            prepare(key, names) {
                return ({
                    payload: { key, names },
                    meta: { key },
                })
            },

            /**
             * Показать несколько полей
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.showMultiFieldsPayload} action.payload
             */
            reducer(state, action) {
                const { names, key: datasourceId } = action.payload

                names.forEach((key) => {
                    const field = get(state, [datasourceId, 'registeredFields', key])

                    if (!field) { return warnNonExistent(key, 'visible') }

                    field.visible_set = true
                    field.visible = field.visible_field && field.visible_set
                })
            },
        },

        HIDE_FIELDS: {
            /**
             * @param {string} key
             * @param {any[]} names
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, names) {
                return ({
                    payload: { key, names },
                    meta: { key },
                })
            },

            /**
             * Скрыть несколько полей
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.hideMultiFieldsPayload} action.payload
             */
            reducer(state, action) {
                const { names, key: datasourceId } = action.payload

                names.forEach((key) => {
                    const field = get(state, [datasourceId, 'registeredFields', key])

                    if (!field) { return warnNonExistent(key, 'visible') }

                    field.visible_set = false
                    field.visible = field.visible_field && field.visible_set
                })
            },
        },

        DISABLE_FIELDS: {
            /**
             * @param {string} key
             * @param {any[]} names
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, names) {
                return ({
                    payload: { key, names },
                    meta: { key },
                })
            },

            /**
             * Деактивация нескольких полей
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.disableMultiFieldsPayload} action.payload
             */
            reducer(state, action) {
                const { names, key } = action.payload

                names.forEach((name) => {
                    const field = get(state, [key, 'registeredFields', name])

                    if (!field) { return warnNonExistent(name, 'disabled') }

                    field.disabled_set = true
                    field.disabled = field.disabled_field || field.disabled_set
                })
            },
        },

        ENABLE_FIELDS: {
            /**
             * @param {string} key
             * @param {any[]} names
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, names) {
                return ({
                    payload: { key, names },
                    meta: { key },
                })
            },

            /**
             * Активация нескольких полей
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.enableMultiFieldsPayload} action.payload
             */
            reducer(state, action) {
                const { names, key: datasourceId } = action.payload

                names.forEach((key) => {
                    const field = get(state, [datasourceId, 'registeredFields', key])

                    if (!field) { return warnNonExistent(key, 'disabled') }

                    field.disabled_set = false
                    field.disabled = field.disabled_field || field.disabled_set
                })
            },
        },

        BLUR: {
            prepare(prefix, key, field) {
                return ({
                    payload: {},
                    meta: { prefix, key, field },
                })
            },

            reducer() {
                // empty reducer, action for saga
            },
        },

        FOCUS: {
            prepare(prefix, key, field) {
                return ({
                    payload: { prefix, key, field },
                    meta: { prefix, key, field },
                })
            },

            reducer(state, action) {
                const { field, key } = action.payload

                set(state, [key, 'registeredFields', field, 'touched'], true)
            },
        },

        TOUCH: {
            prepare(prefix, key, fields) {
                return ({
                    payload: { prefix, key, fields },
                    meta: { prefix, key, fields },
                })
            },

            reducer(state, action) {
                const { fields, key } = action.payload

                if (Array.isArray(fields)) {
                    fields.forEach((field) => {
                        set(state, [key, 'registeredFields', field, 'touched'], true)
                    })
                } else {
                    set(state, [key, 'registeredFields', fields, 'touched'], true)
                }
            },
        },

        initializeDependencies: {
            prepare(key) {
                return ({
                    meta: { key },
                    payload: {},
                })
            },
            reducer() {
                // empty reducer, action for saga
            },
        },

        setDirty: {
            prepare(key, value) {
                return ({
                    meta: { key, value },
                    payload: { key, value },
                })
            },
            reducer(state, action) {
                const { key, value } = action.payload

                set(state, [key, 'dirty'], value)
            },
        },
    },

    extraReducers: {
        [updateModel.type](state, action) {
            const { key } = action.payload
            const isDirty = get(state, 'dirty')

            if (!isDirty) {
                set(state, [key, 'dirty'], true)
            }
        },
        [removeFieldFromArray.type](state, action) {
            const { field, start, end, key } = action.payload
            const deleteAll = end !== undefined

            // Чистим мапу form[dsName].registeredFields[fieldsetName[index].fieldName]
            const registredKeys = Object
                .keys(state[key].registeredFields)
                .filter(fieldName => fieldName.startsWith(`${field}[`))
                // Разделяем индекс и имя поля в строке мультифилдсета
                .map(fieldName => fieldName.replace(field, '').match(/\[(\d+)]\.(.+)/))
                .filter(Boolean)
                .map(([, index, fieldName]) => ({
                    index: +index,
                    fieldName,
                }))
            const groupedFields = registredKeys.reduce((out, { index, fieldName }) => {
                out[index] = out[index] || []
                out[index].push(fieldName)

                return out
            }, [])

            const deleteCount = deleteAll ? groupedFields.length - start : 1
            let i = start

            for (; i < start + deleteCount; i += 1) {
                // eslint-disable-next-line no-loop-func
                groupedFields[i].forEach((fieldName) => {
                    const sourceKey = `${field}[${i}].${fieldName}`

                    delete state[key].registeredFields[sourceKey]
                })
            }

            for (; i < groupedFields.length; i += 1) {
                // eslint-disable-next-line no-loop-func
                groupedFields[i].forEach((fieldName) => {
                    const sourceKey = `${field}[${i}].${fieldName}`
                    const destKey = `${field}[${i - deleteCount}].${fieldName}`

                    state[key].registeredFields[destKey] = state[key].registeredFields[sourceKey]
                    delete state[key].registeredFields[sourceKey]
                })
            }
        },
    },
})

export default formSlice.reducer

export const {
    REGISTER_FIELD_EXTRA: registerFieldExtra,
    DISABLE_FIELD: disableField,
    ENABLE_FIELD: enableField,
    SHOW_FIELD: showField,
    HIDE_FIELD: hideField,
    REGISTER_DEPENDENCY: registerFieldDependency,
    SET_REQUIRED: setRequired,
    UNSET_REQUIRED: unsetRequired,
    SET_LOADING: setLoading,
    SHOW_FIELDS: showMultiFields,
    HIDE_FIELDS: hideMultiFields,
    DISABLE_FIELDS: disableMultiFields,
    ENABLE_FIELDS: enableMultiFields,
    BLUR: handleBlur,
    FOCUS: handleFocus,
    TOUCH: handleTouch,
    initializeDependencies,
    setDirty,
    unRegisterExtraField,
} = formSlice.actions
