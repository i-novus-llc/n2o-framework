import React, { FC, useCallback, useEffect, useMemo, createContext, useRef } from 'react'
import { useDispatch, useSelector, useStore } from 'react-redux'

import { ModelLink } from '../../../../core/models/types'
import { updateModel } from '../../../../ducks/models/store'
import { handleBlur, handleFocus, register, remove, setMessage } from '../../../../ducks/form/store'
import { GetValues, SetBlur, SetFocus, SetValue, SetMessage } from '../types'
import { getByLinkSelector } from '../../../../ducks/models/selectors'
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
    modelLink: ModelLink,
} & Methods

type FormProviderType = {
    formName: string
    modelLink: ModelLink
    needActiveModel?: boolean
}

const FormContext = createContext<FormContextType | null>(null)

const FormProvider: FC<FormProviderType> = ({ children, formName, modelLink, needActiveModel }) => {
    const linkRef = useRef(modelLink)
    const dispatch = useDispatch()
    const isInitForm = useSelector((state: State) => Boolean(makeFormByName(formName)(state)?.isInit))
    const { getState } = useStore()

    linkRef.current = modelLink

    const getValues = useCallback<GetValues>((fieldName?) => {
        const state = getState()

        return getByLinkSelector({ ...linkRef.current, field: fieldName })(state)
    }, [getState])

    const methods = useMemo<Methods>(() => ({
        setValue: (fieldName, value) => {
            dispatch(updateModel(linkRef.current, fieldName, value))
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
    }), [dispatch, formName])

    useEffect(() => {
        dispatch(register(formName, {
            modelLink: linkRef.current,
            needActiveModel,
        }))

        return () => {
            dispatch(remove(formName))
        }
    }, [dispatch, formName, needActiveModel])

    return (
        isInitForm ? (
            <FormContext.Provider value={{
                ...methods,
                getValues,
                formName,
                modelLink,
            }}
            >
                {children}
            </FormContext.Provider>
        ) : null
    )
}

FormProvider.displayName = 'FormProvider'

export { FormProvider, FormContext }
