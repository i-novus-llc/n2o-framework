import { createSlice } from '@reduxjs/toolkit'
import set from 'lodash/set'
import get from 'lodash/get'

import { appendToArray, removeFromArray, updateModel } from '../models/store'
import { AppendToArrayAction, RemoveFromArrayAction } from '../models/Actions'
import { submitSuccess } from '../datasource/store'
import { ModelPrefix } from '../../core/datasource/const'
import { successInvoke } from '../../actions/actionImpl'
import { ValidationResult } from '../../core/validation/types'
import { getCtxFromField } from '../../core/validation/utils'
import { mapMultiFields } from '../../core/models/mapMultiFields'
import { logger } from '../../utils/logger'

import { getDefaultField, getDefaultState } from './FormPlugin'
import { Form, FormsState } from './types'
import {
    BlurFieldAction,
    DangerouslySetFieldValue,
    FocusFieldAction,
    RegisterAction,
    RegisterFieldAction,
    RemoveAction,
    SetDirtyPayload,
    SetFieldDisabledAction,
    SetFieldLoadingAction,
    SetFieldRequiredAction,
    SetFieldTooltipAction,
    SetFieldVisibleAction,
    SetMessagePayload,
    SetMultiFieldDisabledAction,
    SetMultiFieldVisibleAction,
    TouchFieldsAction,
    UnregisterFieldAction,
} from './Actions'

const warnNonExistent = (field: string, property: string) => logger.warn(`Attempt to change "${property}" a non-existent field "${field}"`)

/*
 * TODO выделить filedset'ы нормально в редакс и убрать поля:
 *  disabled_field, disabled_set, visible_field, visible_set
 *  которые нужны для отключения и скрытия полей в зависимсти от филдсетов и нормального возвращения к предыдущему состоянию
 */

const createFieldPath = (formName: string, fieldName: string) => ([formName, 'fields', fieldName])

const updateDirty = (
    id: string,
    prefix: ModelPrefix,
    dirty: boolean,
    state: FormsState,
) => {
    Object.values(state).forEach((form) => {
        if (form.datasource === id && form.modelPrefix === prefix) {
            form.dirty = dirty
        }
    })
}

export const initialState: FormsState = {}

/* eslint-disable consistent-return */
export const formSlice = createSlice({
    name: 'n2o/form',
    initialState,
    reducers: {
        register: {
            prepare(formName: string, initState: Partial<Form>) {
                return ({
                    payload: {
                        formName,
                        initState,
                    },
                })
            },
            reducer(state, { payload }: RegisterAction) {
                const { formName, initState } = payload

                state[formName] = {
                    ...getDefaultState(),
                    ...initState,
                    isInit: true,
                    datasource: initState.datasource || formName,
                    formName,
                }
            },
        },
        remove: {
            prepare(formName: string) {
                return ({
                    payload: {
                        formName,
                    },
                })
            },
            reducer(state, { payload }: RemoveAction) {
                const { formName } = payload

                delete state[formName]
            },
        },
        REGISTER_FIELD_EXTRA: {
            prepare(formName, fieldName, initialState) {
                return ({
                    payload: {
                        formName,
                        fieldName,
                        initialState,
                    },
                })
            },

            reducer(state, { payload, meta = {} }: RegisterFieldAction) {
                const { formName, fieldName, initialState = {} } = payload
                const field = {
                    ...getDefaultField(),
                    ...initialState,
                    isInit: true,
                    ctx: meta.evalContext,
                }
                const fieldPath = createFieldPath(formName, fieldName)
                const registeredInfo = get(state, fieldPath, {})

                set(state, fieldPath, Object.assign(registeredInfo, field))
            },
        },

        setFieldDisabled: {
            prepare(formName: string, fieldName: string, disabled: boolean) {
                return ({
                    payload: { formName, fieldName, disabled },
                })
            },

            reducer(state, action: SetFieldDisabledAction) {
                const { formName, fieldName, disabled } = action.payload
                const field = get(state, createFieldPath(formName, fieldName))

                if (!field) { return warnNonExistent(fieldName, 'disabled') }

                field.disabled_field = disabled
                field.disabled = field.disabled_field || field.disabled_set
            },
        },

        setFieldVisible: {
            prepare(formName: string, fieldName: string, visible: boolean) {
                return ({
                    payload: { formName, fieldName, visible },
                })
            },

            reducer(state, action: SetFieldVisibleAction) {
                const { formName, fieldName, visible } = action.payload
                const field = get(state, createFieldPath(formName, fieldName))

                if (!field) { return warnNonExistent(fieldName, 'visible') }

                field.visible_field = visible
                field.visible = field.visible_field && field.visible_set
            },
        },

        setFieldTooltip: {
            prepare(formName: string, fieldName: string, tooltip: string | null) {
                return ({
                    payload: { formName, fieldName, tooltip },
                })
            },

            reducer(state, action: SetFieldTooltipAction) {
                const { formName, fieldName, tooltip } = action.payload

                const field = get(state, createFieldPath(formName, fieldName))

                if (!field) { return warnNonExistent(fieldName, 'tooltip') }

                field.tooltip = tooltip
            },
        },

        setFieldRequired: {
            prepare(
                formName: string,
                fieldName: string,
                required: boolean,
                validate?: boolean,
            ) {
                return ({
                    payload: { formName, fieldName, required },
                    meta: { validate },
                })
            },

            reducer(state, action: SetFieldRequiredAction) {
                const { formName, fieldName, required } = action.payload

                set(state, [formName, 'fields', fieldName, 'required'], required)
            },
        },

        /**
         * Установить кастомный параметр в поле
         * @deprecated
         */
        dangerouslySetFieldValue: {
            prepare(formName: string, fieldName: string, key: string, value: unknown) {
                return ({
                    payload: { formName, fieldName, key, value },
                })
            },

            reducer(state, action: DangerouslySetFieldValue) {
                const { formName, fieldName, key, value } = action.payload

                set(state, [formName, 'fields', fieldName, key], value)
            },
        },

        setFieldLoading: {
            prepare(formName: string, fieldName: string, loading: boolean) {
                return ({
                    payload: { formName, fieldName, loading },
                })
            },

            reducer(state, action: SetFieldLoadingAction) {
                const { formName, fieldName, loading } = action.payload

                set(state, [formName, 'fields', fieldName, 'loading'], loading)
            },
        },

        setMultiFieldVisible: {
            prepare(formName: string, fields: string[], visible: boolean) {
                return ({
                    payload: { formName, fields, visible },
                })
            },

            reducer(state, action: SetMultiFieldVisibleAction) {
                const { formName, fields, visible } = action.payload

                fields.forEach((fieldName) => {
                    const field = get(state, createFieldPath(formName, fieldName))

                    if (!field) { return warnNonExistent(fieldName, 'visible') }

                    field.visible_set = visible
                    field.visible = field.visible_field && field.visible_set
                })
            },
        },

        setMultiFieldDisabled: {
            prepare(formName: string, fields: string[], disabled: boolean) {
                return ({
                    payload: { formName, fields, disabled },
                })
            },

            reducer(state, action: SetMultiFieldDisabledAction) {
                const { formName, fields, disabled } = action.payload

                fields.forEach((fieldName) => {
                    const field = get(state, createFieldPath(formName, fieldName))

                    if (!field) { return warnNonExistent(fieldName, 'disabled') }

                    field.disabled_set = disabled
                    field.disabled = field.disabled_field || field.disabled_set
                })
            },
        },

        BLUR: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(formName: string, fieldName: string) {
                return ({
                    payload: { formName, fieldName },
                })
            },

            reducer(state, action: BlurFieldAction) {
                const { formName, fieldName } = action.payload
                const field = state[formName]?.fields[fieldName]

                if (field) {
                    field.touched = true
                    field.isActive = false
                }
            },
        },

        FOCUS: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(formName: string, fieldName: string) {
                return ({
                    payload: { formName, fieldName },
                })
            },

            reducer(state, action: FocusFieldAction) {
                const { formName, fieldName } = action.payload

                const field = state[formName]?.fields[fieldName]

                if (field) {
                    field.isActive = true
                }
            },
        },

        TOUCH: {
            prepare(formName: string, fields: string[]) {
                return ({
                    payload: { formName, fields },
                })
            },

            reducer(state, action: TouchFieldsAction) {
                const { fields, formName } = action.payload

                if (Array.isArray(fields)) {
                    fields.forEach((fieldName) => {
                        const field = state[formName]?.fields[fieldName]

                        if (field) {
                            field.touched = true
                        }
                    })
                } else {
                    const field = state[formName]?.fields[fields]

                    if (field) {
                        field.touched = true
                    }
                }
            },
        },

        setDirty: {
            prepare(formName: string, data: boolean) {
                return ({
                    payload: { formName, data },
                })
            },

            reducer(state, action: SetDirtyPayload) {
                const { data, formName } = action.payload

                state[formName].dirty = data
            },
        },

        setMessage: {
            prepare(formName: string, fieldName: string, message: ValidationResult | null) {
                return ({
                    payload: { formName, fieldName, message },
                })
            },

            reducer(state, action: SetMessagePayload) {
                const { formName, fieldName, message } = action.payload
                const field = state[formName]?.fields[fieldName]

                if (field) { field.message = message }
            },
        },
    },

    extraReducers: {
        [submitSuccess.type](state, action) {
            const { id, provider } = action.payload
            const { model } = provider

            updateDirty(id, model, false, state)
        },
        [successInvoke.type](state, action) {
            const { datasource, model } = action.payload

            if (datasource) { updateDirty(datasource, model, false, state) }
        },
        [updateModel.type](state, action) {
            const { key: id, prefix } = action.payload

            updateDirty(id, prefix, true, state)
        },
        [removeFromArray.type](state, action: RemoveFromArrayAction) {
            const { field: listName, start, count = 1, key: datasource, prefix } = action.payload

            if (!listName) { return }

            for (const form of Object.values(state)) {
                if (form.datasource !== datasource || form.modelPrefix !== prefix) { continue }

                form.fields = mapMultiFields(form.fields, listName, ({ item: field, fullName, subName, index }) => {
                    // index before removed elements
                    if (index < start) { return { name: fullName, value: field } }
                    // removed elements: ignore it
                    if ((index >= start) && (index < start + count)) { return }

                    // after removed: shift index
                    const newName = `${listName}[${index - count}].${subName}`

                    return {
                        name: newName,
                        value: {
                            ...field,
                            dependency: form.fields[newName]?.dependency || field.dependency,
                            ctx: form.fields[newName]?.ctx,
                        },
                    }
                })
            }
        },
        [appendToArray.type](state, action: AppendToArrayAction) {
            const { field: listName, position, key: datasource, prefix } = action.payload

            if (typeof position === 'undefined') { return }
            if (!listName) { return }

            for (const form of Object.values(state)) {
                if (form.datasource !== datasource || form.modelPrefix !== prefix) { continue }

                form.fields = mapMultiFields(form.fields, listName, ({ item: field, fullName, subName, index }) => {
                    // index before new element
                    if (index < position) { return { name: fullName, value: field } }

                    const newName = `${listName}[${index + 1}].${subName}`

                    return {
                        name: newName,
                        value: {
                            ...field,
                            dependency: field.dependency.map(dep => ({
                                ...dep,
                                on: dep.on?.map(on => on.replace(
                                    `${listName}[${index}]`,
                                    `${listName}[${index + 1}]`,
                                )),
                            })),
                            ctx: {
                                ...(field.ctx ?? {}),
                                ...(getCtxFromField(newName) ?? {}),
                            },
                        },
                    }
                })
            }
        },
    },
})

export default formSlice.reducer

export const {
    REGISTER_FIELD_EXTRA: registerFieldExtra,
    // form actions
    register,
    remove,
    // field actions
    setFieldDisabled,
    setFieldVisible,
    setFieldRequired,
    setFieldLoading,
    setFieldTooltip,
    setMessage,
    setMultiFieldVisible,
    setMultiFieldDisabled,
    dangerouslySetFieldValue,
    BLUR: handleBlur,
    FOCUS: handleFocus,
    // TODO @touched удалить
    TOUCH: handleTouch,
    setDirty,
} = formSlice.actions
