import React, { FC, useCallback, useEffect, useMemo } from 'react'
import { createContext } from 'use-context-selector'
import get from 'lodash/get'
import { useDispatch, useStore } from 'react-redux'

import { ModelPrefix } from '../../../../core/datasource/const'
import { modelInit, updateModel } from '../../../../ducks/models/store'
// @ts-ignore ignore import error from js file
import { handleBlur, handleFocus, setDirty } from '../../../../ducks/form/store'
import { TGetValues, TReset, TSetBlur, TSetFocus, TSetValue } from '../types'
import { getModelFieldByPath } from '../../../../ducks/models/selectors'

type TMethods = {
    setValue: TSetValue
    setFocus: TSetFocus
    setBlur: TSetBlur
    reset: TReset
}

type TFormContext = {
    getValues: TGetValues
    name: string
    prefix: ModelPrefix
} & TMethods

type TFormProvider = {
    name: string,
    prefix: ModelPrefix
    initialValues?: object
}

const FormContext = createContext<TFormContext | null>(null)

const FormProvider: FC<TFormProvider> = ({ children, name, prefix, initialValues }) => {
    const dispatch = useDispatch()
    const { getState } = useStore()

    const getValues = useCallback<TGetValues>((fieldName) => {
        const state = getState()
        const models = getModelFieldByPath(`${prefix}.${name}`)(state)

        if (Array.isArray(fieldName)) {
            return get(models, fieldName.map(name => name))
        }

        return get(models, fieldName)
    }, [getState, name, prefix])

    const methods = useMemo<TMethods>(() => ({
        setValue: (fieldName, value) => {
            dispatch(updateModel(prefix, name, fieldName, value))
        },
        setFocus: (fieldName) => {
            dispatch(handleFocus(prefix, name, fieldName))
        },
        setBlur: (fieldName) => {
            dispatch(handleBlur(prefix, name, fieldName))
        },
        reset: (data) => {
            let firstInit = true // Костыль, для саги

            return (() => {
                dispatch(modelInit(prefix, name, data, firstInit))

                if (prefix === ModelPrefix.edit) {
                    dispatch(modelInit(ModelPrefix.active, name, data, firstInit))
                }

                firstInit = false
            })()
        },
    }), [dispatch, prefix, name])

    const { reset } = methods

    const remove = useCallback(() => {
        dispatch(setDirty(name, false))
    }, [dispatch, name])

    useEffect(() => {
        if (initialValues !== undefined) {
            reset(initialValues)
        }
    }, [reset, initialValues])

    useEffect(() => remove, [remove])

    return (
        <FormContext.Provider value={{
            ...methods,
            getValues,
            name,
            prefix,
        }}
        >
            {children}
        </FormContext.Provider>
    )
}

FormProvider.displayName = 'FormProvider'

export { FormProvider, FormContext }
