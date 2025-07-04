import { createSlice } from '@reduxjs/toolkit'
import set from 'lodash/set'
import get from 'lodash/get'

import { removeFieldFromArray, updateModel } from '../models/store'
import { RemoveFieldFromArrayAction } from '../models/Actions'
import { submitSuccess } from '../datasource/store'
import { ModelPrefix } from '../../core/datasource/const'
import { successInvoke } from '../../actions/actionImpl'

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
    SetMultiFieldDisabledAction,
    SetMultiFieldVisibleAction,
    TouchFieldsAction,
    UnregisterFieldAction,
} from './Actions'

// eslint-disable-next-line no-console
const warnNonExistent = (field: string, property: string) => console.warn(`Attempt to change "${property}" a non-existent field "${field}"`)

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
                const formState = {
                    ...getDefaultField(),
                    ...initialState,
                    isInit: true,
                    ctx: meta.evalContext,
                }
                const fieldPath = createFieldPath(formName, fieldName)
                const registeredInfo = get(state, fieldPath, {})

                set(state, fieldPath, Object.assign(registeredInfo, formState))
            },
        },

        unRegisterExtraField: {
            prepare(formName: string, fieldName: string) {
                return ({
                    payload: { formName, fieldName },
                })
            },

            reducer(state, action: UnregisterFieldAction) {
                const { formName, fieldName } = action.payload

                delete state[formName]?.fields[fieldName]
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
    },

    extraReducers: {
        [submitSuccess.type](state, action) {
            const { id, provider } = action.payload
            const { model } = provider

            updateDirty(id, model, false, state)
        },
        [successInvoke.type](state, action) {
            const { datasource, model } = action.payload

            updateDirty(datasource, model, false, state)
        },
        [updateModel.type](state, action) {
            const { key, prefix } = action.payload

            updateDirty(key, prefix, true, state)
        },
        [removeFieldFromArray.type](state, action: RemoveFieldFromArrayAction) {
            const { field, start, end, key: datasource, prefix } = action.payload
            const deleteAll = end !== undefined

            for (const form of Object.values(state)) {
                // eslint-disable-next-line no-continue
                if (form.datasource !== datasource || form.modelPrefix !== prefix) { continue }

                // Чистим мапу form[dsName].fields[fieldsetName[index].fieldName]
                const registredKeys = Object
                    .keys(form.fields)
                    .filter(fieldName => fieldName.startsWith(`${field}[`))
                    // Разделяем индекс и имя поля в строке мультифилдсета
                    .map(fieldName => fieldName.replace(field, '').match(/\[(\d+)]\.(.+)/))
                    // @ts-ignore ts не догоняет что Boolean уберёт всё null из списка
                    .filter<RegExpMatchArray>(Boolean)
                    .map(([, index, fieldName]) => ({
                        index: +index,
                        fieldName,
                    }))
                const groupedFields: string[][] = registredKeys.reduce((out: string[][], { index, fieldName }) => {
                    out[index] = out[index] || []
                    out[index].push(fieldName)

                    return out
                }, [])

                const deleteCount = deleteAll ? groupedFields.length - start : 1
                let i = start

                // Котыль для синхронизации контекста строки
                const ctxMap = Object.fromEntries(Object.entries(form.fields)
                    .filter(([fieldName]) => fieldName.startsWith(`${field}[`))
                    .map(([key, field]) => [key, field.ctx]))

                for (; i < start + deleteCount; i += 1) {
                    // eslint-disable-next-line no-loop-func,@typescript-eslint/no-loop-func
                    groupedFields[i]?.forEach((fieldName) => {
                        const sourceKey = `${field}[${i}].${fieldName}`

                        delete form.fields[sourceKey]
                    })
                }

                for (; i < groupedFields.length; i += 1) {
                    // eslint-disable-next-line no-loop-func,@typescript-eslint/no-loop-func
                    groupedFields[i]?.forEach((fieldName) => {
                        const newIndex = i - deleteCount
                        const sourceKey = `${field}[${i}].${fieldName}`
                        const destKey = `${field}[${newIndex}].${fieldName}`

                        form.fields[destKey] = form.fields[sourceKey]
                        form.fields[destKey].ctx = ctxMap[destKey]

                        delete form.fields[sourceKey]
                    })
                }
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
    setMultiFieldVisible,
    setMultiFieldDisabled,
    dangerouslySetFieldValue,
    BLUR: handleBlur,
    FOCUS: handleFocus,
    // TODO @touched удалить
    TOUCH: handleTouch,
    unRegisterExtraField,
    setDirty,
} = formSlice.actions
