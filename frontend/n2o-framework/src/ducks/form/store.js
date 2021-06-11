import { createSlice } from '@reduxjs/toolkit'
import merge from 'deepmerge'
import { actionTypes } from 'redux-form'
import has from 'lodash/has'
import set from 'lodash/set'

import FormPlugin from './FormPlugin'

const initialState = FormPlugin.defaultState

const formSlice = createSlice({
    name: 'n2o/formPlugin',
    initialState,
    reducers: {
        REGISTER_FIELD_EXTRA: {
            /**
             * @param {string} form
             * @param {string} name
             * @param {FormPluginStore.item} initialState
             * @return {{payload: FormPluginStore.registerFieldExtraPayload, meta: {form: string}}}
             */
            prepare(form, name, initialState) {
                return ({
                    payload: {
                        name,
                        form,
                        initialState,
                    },

                    meta: { form },
                })
            },

            /**
             * Регистрирование дополнительных свойств у поля формы
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.registerFieldExtraPayload} action.payload
             */
            reducer(state, action) {
                const { name, initialState = {} } = action.payload
                const formState = merge(FormPlugin.defaultState, initialState)

                if (!state.registeredFields) {
                    state.registeredFields = {}
                }

                if (!state.registeredFields[name]) {
                    state.registeredFields[name] = {}
                }

                state.registeredFields[name] = Object.assign(state.registeredFields[name], formState)
            },
        },

        DISABLE_FIELD: {
            /**
             * @param {string} form
             * @param {string} name
             * @return {{payload: FormPluginStore.disableFieldPayload, meta: {form: string}}}
             */
            prepare(form, name) {
                return ({
                    payload: { form, name },
                    meta: { form },
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
                const { name } = action.payload

                state.registeredFields[name].disabled = true
            },
        },

        ENABLE_FIELD: {
            /**
             * @param {string} form
             * @param {string} name
             * @return {{payload: FormPluginStore.enableFieldPayload, meta: {form: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(form, name) {
                return ({
                    payload: { form, name },
                    meta: { form },
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
                const { name } = action.payload

                state.registeredFields[name].disabled = false
            },
        },

        SHOW_FIELD: {
            /**
             * @param {string} form
             * @param {string} name
             * @return {{payload: FormPluginStore.showFieldPayload, meta: {form: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(form, name) {
                return ({
                    payload: { form, name },
                    meta: { form },
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
                const { name } = action.payload

                state.registeredFields[name].visible = true
            },
        },

        HIDE_FIELD: {
            /**
             * @param {string} form
             * @param {string} name
             * @return {{payload: FormPluginStore.hideFieldPayload, meta: {form: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(form, name) {
                return ({
                    payload: { form, name },
                    meta: { form },
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
                const { name } = action.payload

                state.registeredFields[name].visible = false
            },
        },

        ADD_MESSAGE: {
            /**
             * @param {string} form
             * @param {string} name
             * @param {Object.<string, any>} message
             * @param {boolean} isTouched
             * @return {{payload: FormPluginStore.addFieldMessagePayload, meta: {form: string, isTouched: boolean}}}
             */
            prepare(form, name, message, isTouched) {
                return ({
                    payload: { form, name, message },
                    meta: { form, isTouched },
                })
            },

            /**
             * Добавить сообщение (после валидации) к полю
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.addFieldMessagePayload} action.payload
             */
            reducer(state, action) {
                const { name, message } = action.payload

                if (!state.registeredFields[name].message) {
                    state.registeredFields[name].message = {}
                }

                state.registeredFields[name].message = Object.assign(state.registeredFields[name].message, message)
            },
        },

        REMOVE_MESSAGE: {
            /**
             * @param {string} form
             * @param {string} name
             * @return {{payload: FormPluginStore.removeFieldMessagePayload, meta: {form: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(form, name) {
                return ({
                    payload: { form, name },
                    meta: { form },
                })
            },

            /**
             * Удалить сообщение
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.removeFieldMessagePayload} action.payload
             */
            reducer(state, action) {
                const { name } = action.payload

                state.registeredFields[name].message = null
            },
        },

        REGISTER_DEPENDENCY: {
            /**
             * @param {string} form
             * @param {string} name
             * @param {object} dependency
             * @return {{payload: FormPluginStore.registerFieldDependencyPayload, meta: {form: string}}}
             */
            prepare(form, name, dependency) {
                return ({
                    payload: { form, name, dependency },
                    meta: { form },
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
                const { name, dependency } = action.payload

                state.registeredFields[name].dependency = dependency
            },
        },

        SET_FIELD_FILTER: {
            /**
             * @param {string} form
             * @param {string} name
             * @param {object} filter
             * @return {{payload: FormPluginStore.setFilterValuePayload, meta: {form: string}}}
             */
            prepare(form, name, filter) {
                return ({
                    payload: { form, name, filter },
                    meta: { form },
                })
            },

            /**
             * Установить значениек для поля filter
             * @param {FormPluginStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {FormPluginStore.setFilterValuePayload} action.payload
             */
            reducer(state, action) {
                const { name, filter } = action.payload

                state.registeredFields[name].filter =
                    state.registeredFields[name].filter
                        .filter(f => f.filterId !== filter.filterId)
                        .concat(filter)
            },
        },

        SET_REQUIRED: {
            /**
             * @param {string} form
             * @param {string} name
             * @return {{payload: FormPluginStore.setRequiredPayload, meta: {form: string, field: string}}}
             */
            prepare(form, name) {
                return ({
                    payload: { form, name },
                    meta: { form, field: name },
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
                const { name } = action.payload

                state.registeredFields[name].required = true
            },
        },

        UNSET_REQUIRED: {
            /**
             * @param {string} form
             * @param {string} name
             * @return {{payload: FormPluginStore.unsetRequiredPayload, meta: {form: string, field: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(form, name) {
                return ({
                    payload: { form, name },
                    meta: { form, field: name },
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
                const { name } = action.payload

                state.registeredFields[name].required = false
            },
        },

        SET_LOADING: {
            /**
             * @param {string} form
             * @param {string} name
             * @param {boolean} loading
             * @return {{payload: FormPluginStore.setLoadingPayload, meta: {form: string}}}
             */
            prepare(form, name, loading) {
                return ({
                    payload: { form, name, loading },
                    meta: { form },
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
                const { name, loading } = action.payload

                state.registeredFields[name].loading = loading
            },
        },

        SHOW_FIELDS: {
            /**
             * @param {string} form
             * @param {any[]} names
             * @return {{payload: FormPluginStore.showMultiFieldsPayload, meta: {form: string}}}
             */
            prepare(form, names) {
                return ({
                    payload: { form, names },
                    meta: { form },
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
                const { names } = action.payload

                names.forEach((key) => {
                    const field = state.registeredFields[key]

                    // показываем поля только если у них нет своего условия на видимость
                    if (
                        field.dependency &&
                        field.dependency.some(({ type }) => type === 'visible')
                    ) {
                        return
                    }
                    field.visible = true
                })
            },
        },

        HIDE_FIELDS: {
            /**
             * @param {string} form
             * @param {any[]} names
             * @return {{payload: FormPluginStore.hideMultiFieldsPayload, meta: {form: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(form, names) {
                return ({
                    payload: { form, names },
                    meta: { form },
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
                const { names } = action.payload

                names.forEach((key) => {
                    state.registeredFields[key].visible = false
                })
            },
        },

        DISABLE_FIELDS: {
            /**
             * @param {string} form
             * @param {any[]} names
             * @return {{payload: FormPluginStore.disableMultiFieldsPayload, meta: {form: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(form, names) {
                return ({
                    payload: { form, names },
                    meta: { form },
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
                const { names } = action.payload

                names.forEach((key) => {
                    state.registeredFields[key].disabled = true
                })
            },
        },

        ENABLE_FIELDS: {
            /**
             * @param {string} form
             * @param {any[]} names
             * @return {{payload: FormPluginStore.enableMultiFieldsPayload, meta: {form: string}}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(form, names) {
                return ({
                    payload: { form, names },
                    meta: { form },
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
                const { names } = action.payload

                names.forEach((key) => {
                    const field = state.registeredFields[key]

                    // поля доступны только если у них нет своего условия на доступность
                    if (
                        field.dependency &&
                        field.dependency.some(({ type }) => type === 'enabled')
                    ) {
                        return
                    }
                    field.disabled = false
                })
            },
        },
    },

    extraReducers: {
        [actionTypes.CHANGE](state, action) {
            const { field } = action.meta

            if (!field) {
                return
            }
            const customFormAction = has(action.payload, 'keepDirty')
            const value = customFormAction ? action.payload.value : action.payload

            /*
             * TODO придумать как аккуратно отказаться от _.set
             *  сейчас он раскручивает поля ввида values[field[index].property]
             */
            set(state, `values[${field}]`, value)

            if (customFormAction && !action.payload.keepDirty) {
                set(state, `initial[${field}]`, action.payload.value)
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
    ADD_MESSAGE: addFieldMessage,
    REMOVE_MESSAGE: removeFieldMessage,
    REGISTER_DEPENDENCY: registerFieldDependency,
    SET_FIELD_FILTER: setFilterValue,
    SET_REQUIRED: setRequired,
    UNSET_REQUIRED: unsetRequired,
    SET_LOADING: setLoading,
    SHOW_FIELDS: showMultiFields,
    HIDE_FIELDS: hideMultiFields,
    DISABLE_FIELDS: disableMultiFields,
    ENABLE_FIELDS: enableMultiFields,
} = formSlice.actions
