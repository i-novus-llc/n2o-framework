import React, { FC, useCallback, useEffect, useMemo, createContext } from 'react'
import get from 'lodash/get'
import { useDispatch, useStore } from 'react-redux'

import { ModelPrefix } from '../../../../core/datasource/const'
import { updateModel } from '../../../../ducks/models/store'
// @ts-ignore ignore import error from js file
import { handleBlur, handleFocus, register, remove } from '../../../../ducks/form/store'
import { TGetValues, TSetBlur, TSetFocus, TSetValue } from '../types'
import { getModelFieldByPath } from '../../../../ducks/models/selectors'
import { ValidationsKey } from '../../../../core/validation/IValidation'

type TMethods = {
    setValue: TSetValue
    setFocus: TSetFocus
    setBlur: TSetBlur
}

type TFormContext = {
    getValues: TGetValues
    formName: string
    prefix: ModelPrefix
    datasource: string
} & TMethods

type TFormProvider = {
    formName: string,
    datasource: string,
    prefix: ModelPrefix,
    validationKey?: ValidationsKey,
}

const FormContext = createContext<TFormContext | null>(null)

const FormProvider: FC<TFormProvider> = ({ children, formName, datasource, prefix, validationKey }) => {
    const dispatch = useDispatch()
    const { getState } = useStore()

    const getValues = useCallback<TGetValues>((fieldName) => {
        const state = getState()
        const models = getModelFieldByPath(`${prefix}.${datasource}`)(state)

        if (Array.isArray(fieldName)) {
            return get(models, fieldName.map(name => name))
        }

        return get(models, fieldName)
    }, [getState, datasource, prefix])

    const methods = useMemo<TMethods>(() => ({
        setValue: (fieldName, value) => {
            dispatch(updateModel(prefix, datasource, fieldName, value))
        },
        setFocus: (fieldName) => {
            dispatch(handleFocus(formName, fieldName))
        },
        setBlur: (fieldName) => {
            dispatch(handleBlur(formName, fieldName))
        },
    }), [dispatch, prefix, formName, datasource])

    useEffect(() => {
        dispatch(register(formName, {
            datasource,
            modelPrefix: prefix,
            validationKey: validationKey || ValidationsKey.Validations,
        }))

        return () => {
            dispatch(remove(formName))
        }
    }, [datasource, dispatch, formName, prefix, validationKey])

    return (
        <FormContext.Provider value={{
            ...methods,
            getValues,
            formName,
            prefix,
            datasource,
        }}
        >
            {children}
        </FormContext.Provider>
    )
}

FormProvider.displayName = 'FormProvider'

export { FormProvider, FormContext }
