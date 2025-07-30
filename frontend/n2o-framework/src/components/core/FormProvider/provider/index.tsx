import React, { FC, useCallback, useEffect, useMemo, createContext } from 'react'
import get from 'lodash/get'
import { useDispatch, useSelector, useStore } from 'react-redux'

import { ModelPrefix } from '../../../../core/datasource/const'
import { updateModel } from '../../../../ducks/models/store'
import { handleBlur, handleFocus, register, remove, setMessage } from '../../../../ducks/form/store'
import { GetValues, SetBlur, SetFocus, SetValue, SetMessage } from '../types'
import { getModelFieldByPath } from '../../../../ducks/models/selectors'
import { ValidationsKey } from '../../../../core/validation/types'
import { makeFormByName } from '../../../../ducks/form/selectors'
import { State } from '../../../../ducks/State'

type Methods = {
    setValue: SetValue
    setFocus: SetFocus
    setBlur: SetBlur
    setMessage: SetMessage
}

type FormContextType = {
    getValues: GetValues
    formName: string
    prefix: ModelPrefix
    datasource: string
} & Methods

type FormProviderType = {
    formName: string,
    datasource: string,
    prefix: ModelPrefix,
    validationKey?: ValidationsKey,
}

const FormContext = createContext<FormContextType | null>(null)

const FormProvider: FC<FormProviderType> = ({ children, formName, datasource, prefix, validationKey }) => {
    const dispatch = useDispatch()
    const isInitForm = useSelector((state: State) => Boolean(makeFormByName(formName)(state).isInit))
    const { getState } = useStore()

    const getValues = useCallback<GetValues>((fieldName) => {
        const state = getState()
        const models = getModelFieldByPath(`${prefix}.${datasource}`)(state)

        if (Array.isArray(fieldName)) {
            return get(models, fieldName.map(name => name))
        }

        return get(models, fieldName)
    }, [getState, datasource, prefix])

    const methods = useMemo<Methods>(() => ({
        setValue: (fieldName, value) => {
            dispatch(updateModel(prefix, datasource, fieldName, value))
        },
        setFocus: (fieldName) => {
            dispatch(handleFocus(formName, fieldName))
        },
        setBlur: (fieldName) => {
            dispatch(handleBlur(formName, fieldName))
        },
        setMessage: (fieldName, message) => {
            dispatch(setMessage(formName, fieldName, message))
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
        isInitForm ? (
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
        ) : null
    )
}

FormProvider.displayName = 'FormProvider'

export { FormProvider, FormContext }
