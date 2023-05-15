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

const createFieldPath = (prefix, datasourceId, fieldName) => ([datasourceId, prefix, 'registeredFields', fieldName])

/* eslint-disable consistent-return */
const formSlice = createSlice({
    name: 'n2o/formPlugin',
    initialState,
    reducers: {
        REGISTER_FIELD_EXTRA: {
            prepare(prefix, key, name, initialState) {
                return ({
                    payload: {
                        name,
                        key,
                        initialState,
                        prefix,
                    },
                    meta: { key, name, prefix },
                })
            },

            reducer(state, action) {
                const { name, key, initialState = {}, prefix } = action.payload
                const formState = merge(FormPlugin.defaultState, initialState)
                const fieldPath = createFieldPath(prefix, key, name)
                const registeredInfo = get(state, fieldPath, {})

                set(state, fieldPath, Object.assign(registeredInfo, formState))
            },
        },

        unRegisterExtraField: {
            prepare(prefix, key, name) {
                return ({
                    payload: { name, key, prefix },
                    meta: { key, name, prefix },
                })
            },

            reducer(state, action) {
                const { name, key, prefix } = action.payload

                delete state?.[key]?.[prefix].registeredFields?.[name]
            },
        },

        DISABLE_FIELD: {
            prepare(prefix, key, name) {
                return ({
                    payload: { key, name, prefix },
                    meta: { key, name, prefix },
                })
            },

            reducer(state, action) {
                const { key, name, prefix } = action.payload
                const field = get(state, createFieldPath(prefix, key, name))

                if (!field) { return warnNonExistent(name, 'disabled') }

                field.disabled_field = true
                field.disabled = field.disabled_field || field.disabled_set
            },
        },

        ENABLE_FIELD: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(prefix, key, name) {
                return ({
                    payload: { key, name, prefix },
                    meta: { key, name, prefix },
                })
            },

            reducer(state, action) {
                const { name, key, prefix } = action.payload
                const field = get(state, createFieldPath(prefix, key, name))

                if (!field) { return warnNonExistent(name, 'disabled') }

                field.disabled_field = false
                field.disabled = field.disabled_field || field.disabled_set
            },
        },

        SHOW_FIELD: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(prefix, key, name) {
                return ({
                    payload: { key, name, prefix },
                    meta: { key, name, prefix },
                })
            },

            reducer(state, action) {
                const { name, key, prefix } = action.payload
                const field = get(state, createFieldPath(prefix, key, name))

                if (!field) { return warnNonExistent(name, 'visible') }

                field.visible_field = true
                field.visible = field.visible_field && field.visible_set
            },
        },

        HIDE_FIELD: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(prefix, key, name) {
                return ({
                    payload: { prefix, key, name },
                    meta: { prefix, key, name },
                })
            },

            reducer(state, action) {
                const { name, key, prefix } = action.payload
                const field = get(state, createFieldPath(prefix, key, name))

                if (!field) { return warnNonExistent(name, 'visible') }

                field.visible_field = false
                field.visible = field.visible_field && field.visible_set
            },
        },

        REGISTER_DEPENDENCY: {
            prepare(prefix, key, name, dependency) {
                return ({
                    payload: { prefix, key, name, dependency },
                    meta: { prefix, key, name },
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
                const { prefix, name, dependency, key } = action.payload
                // const field = get(state, createFieldPath(prefix, key, name))

                // if (!field) { return warnNonExistent(name, 'dependency') }
                //
                // field.dependency = dependency
                // TODO: Если все работает, раскоментировать верхний варриант и попробовать еще раз
                set(state, [key, prefix, 'registeredFields', name, 'dependency'], dependency)
            },
        },

        SET_REQUIRED: {
            prepare(prefix, key, field) {
                return ({
                    payload: { key, field, prefix },
                    meta: { key, field, prefix },
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
                const { field, key, prefix } = action.payload

                set(state, [key, prefix, 'registeredFields', field, 'required'], true)
            },
        },

        UNSET_REQUIRED: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(prefix, key, field) {
                return ({
                    payload: { key, field, prefix },
                    meta: { key, field, prefix },
                })
            },

            /**
             * Снять флаг обазяательного поля
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.unsetRequiredPayload} action.payload
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action) {
                const { field, key, prefix } = action.payload

                set(state, [key, prefix, 'registeredFields', field, 'required'], false)
            },
        },

        SET_LOADING: {
            prepare(prefix, key, name, loading) {
                return ({
                    payload: { key, name, prefix, loading },
                    meta: { key, name, prefix },
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
                const { prefix, name, loading, key } = action.payload

                set(state, [key, prefix, 'registeredFields', name, 'loading'], loading)
            },
        },

        SHOW_FIELDS: {
            prepare(prefix, key, names) {
                return ({
                    payload: { key, prefix, names },
                    meta: { key, prefix, names },
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
                const { names, key: datasourceId, prefix } = action.payload

                names.forEach((key) => {
                    const field = get(state, createFieldPath(prefix, datasourceId, key))

                    if (!field) { return warnNonExistent(key, 'visible') }

                    field.visible_set = true
                    field.visible = field.visible_field && field.visible_set
                })
            },
        },

        HIDE_FIELDS: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(prefix, key, names) {
                return ({
                    payload: { prefix, key, names },
                    meta: { prefix, key, names },
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
                const { names, key: datasourceId, prefix } = action.payload

                names.forEach((key) => {
                    const field = get(state, createFieldPath(prefix, datasourceId, key))

                    if (!field) { return warnNonExistent(key, 'visible') }

                    field.visible_set = false
                    field.visible = field.visible_field && field.visible_set
                })
            },
        },

        DISABLE_FIELDS: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(prefix, key, names) {
                return ({
                    payload: { prefix, key, names },
                    meta: { prefix, key },
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
                const { names, key, prefix } = action.payload

                names.forEach((name) => {
                    const field = get(state, createFieldPath(prefix, key, name))

                    if (!field) { return warnNonExistent(name, 'disabled') }

                    field.disabled_set = true
                    field.disabled = field.disabled_field || field.disabled_set
                })
            },
        },

        ENABLE_FIELDS: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(prefix, key, names) {
                return ({
                    payload: { prefix, key, names },
                    meta: { prefix, key },
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
                const { names, key: datasourceId, prefix } = action.payload

                names.forEach((key) => {
                    const field = get(state, createFieldPath(prefix, datasourceId, key))

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
                const { field, key, prefix } = action.payload

                set(state, [key, prefix, 'registeredFields', field, 'touched'], true)
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
                const { fields, key, prefix } = action.payload

                if (Array.isArray(fields)) {
                    fields.forEach((field) => {
                        set(state, [key, prefix, 'registeredFields', field, 'touched'], true)
                    })
                } else {
                    set(state, [key, prefix, 'registeredFields', fields, 'touched'], true)
                }
            },
        },

        initializeDependencies: {
            prepare(prefix, key) {
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
            const { field, start, end, key, prefix } = action.payload
            const deleteAll = end !== undefined

            // Чистим мапу form[dsName][prefix].registeredFields[fieldsetName[index].fieldName]
            const registredKeys = Object
                .keys(state[key][prefix].registeredFields)
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

                    delete state[key][prefix].registeredFields[sourceKey]
                })
            }

            for (; i < groupedFields.length; i += 1) {
                // eslint-disable-next-line no-loop-func
                groupedFields[i].forEach((fieldName) => {
                    const newIndex = i - deleteCount
                    const sourceKey = `${field}[${i}].${fieldName}`
                    const destKey = `${field}[${newIndex}].${fieldName}`

                    state[key][prefix].registeredFields[destKey] = state[key][prefix].registeredFields[sourceKey]
                    state[key][prefix].registeredFields[destKey].parentIndex = newIndex
                    delete state[key][prefix].registeredFields[sourceKey]
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
