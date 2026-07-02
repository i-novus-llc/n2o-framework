import { createSlice } from '@reduxjs/toolkit'
import set from 'lodash/set'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'

import { appendToArray, removeFromArray, updateModel } from '../models/store'
import { AppendToArrayAction, RemoveFromArrayAction } from '../models/Actions'
import { submitSuccess } from '../datasource/store'
import { successInvoke } from '../../actions/actionImpl'
import { ValidationResult } from '../../core/validation/types'
import { getCtxFromField } from '../../core/validation/utils'
import { mapMultiFields } from '../../core/models/mapMultiFields'
import { logger } from '../../utils/logger'
import { ModelLink } from '../../core/models/types'

import { getDefaultField, getDefaultState } from './FormPlugin'
import { Field, FormsState } from './types'
import {
    BlurFieldAction,
    DangerouslySetFieldValue,
    FocusFieldAction,
    RegisterAction,
    RegisterFieldAction,
    UnregisterFieldAction,
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
} from './Actions'

const warnNonExistent = (field: string, property: string) => logger.warn(`Attempt to change "${property}" a non-existent field "${field}"`)

/*
 * TODO выделить filedset'ы нормально в редакс и убрать поля:
 *  disabled_field, disabled_set, visible_field, visible_set
 *  которые нужны для отключения и скрытия полей в зависимсти от филдсетов и нормального возвращения к предыдущему состоянию
 */

const createFieldPath = (formName: string, fieldName: string) => ([formName, 'fields', fieldName])

const updateDirty = (
    modelLink: ModelLink,
    dirty: boolean,
    state: FormsState,
) => {
    Object.values(state).forEach((form) => {
        if (isEqual(form.modelLink, modelLink)) {
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
            prepare(formName, initState) {
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
                const fieldPath = createFieldPath(formName, fieldName)
                const field = {
                    ...getDefaultField(),
                    ...initialState,
                    ...get(state, fieldPath, {}),
                    isInit: true,
                    ctx: meta.evalContext,
                }

                set(state, fieldPath, field)
            },
        },

        unRegisterExtraFields: {
            prepare(formName: string, rowId: string | null) {
                return ({
                    payload: { formName, rowId },
                })
            },

            reducer(state, action: UnregisterFieldAction) {
                const { formName, rowId } = action.payload

                if (!rowId) { return }

                Object.entries(state[formName]?.fields)
                    .filter(([_, field]) => (field.rowId === rowId || field.rowId?.startsWith(`${rowId}/`)))
                    .forEach(([fieldName]) => {
                        delete state[formName]?.fields[fieldName]
                    })
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
                    const fieldPath = createFieldPath(formName, fieldName)
                    const field = get(state, fieldPath)

                    if (!field) {
                        const fieldInfo: Partial<Pick<Field, 'visible_set' | 'visible'>> = {
                            visible_set: visible,
                        }

                        if (!visible) { fieldInfo.visible = false }

                        set(state, fieldPath, fieldInfo)

                        return
                    }

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
                    const fieldPath = createFieldPath(formName, fieldName)
                    const field = get(state, fieldPath)

                    if (!field) {
                        const fieldInfo: Partial<Pick<Field, 'disabled_set' | 'disabled'>> = {
                            disabled_set: disabled,
                        }

                        if (disabled) { fieldInfo.disabled = true }

                        set(state, fieldPath, fieldInfo)

                        return
                    }

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

            updateDirty({ prefix: model, id }, false, state)
        },
        [successInvoke.type](state, action) {
            const { datasource, model } = action.payload

            if (datasource) { updateDirty({ prefix: model, id: datasource }, false, state) }
        },
        [updateModel.type](state, action) {
            const { modelLink } = action.payload

            updateDirty(modelLink, true, state)
        },
        [removeFromArray.type](state, action: RemoveFromArrayAction) {
            const { modelLink, fieldName: listName, start, count = 1 } = action.payload
            const forms = Object.values(state)

            if (!listName) {
                forms.forEach((form) => {
                    const { index, id, prefix } = form.modelLink

                    if (
                        (id === modelLink.id) && (prefix === modelLink.prefix) &&
                        (typeof index === 'number') && (index > start + count)
                    ) { form.modelLink.index = index - count }
                })

                return
            }

            for (const form of forms) {
                if (!isEqual(form.modelLink, modelLink)) { continue }

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
            const { modelLink, fieldName: listName, position } = action.payload
            const forms = Object.values(state)

            if (typeof position === 'undefined') { return }
            if (!listName) {
                forms.forEach((form) => {
                    const { index, id, prefix } = form.modelLink

                    if (
                        (id === modelLink.id) && (prefix === modelLink.prefix) &&
                        (typeof index === 'number') && (index >= position)
                    ) { form.modelLink.index = index + 1 }
                })

                return
            }

            for (const form of forms) {
                if (!isEqual(form.modelLink, modelLink)) { continue }

                form.fields = mapMultiFields(form.fields, listName, ({ item: field, fullName, subName, index }) => {
                    // index before new element
                    if (index < position) { return { name: fullName, value: field } }

                    const newName = `${listName}[${index + 1}].${subName}`

                    return {
                        name: newName,
                        value: {
                            ...field,
                            dependency: field.dependency?.map(dep => ({
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
    unRegisterExtraFields,
    setDirty,
} = formSlice.actions
