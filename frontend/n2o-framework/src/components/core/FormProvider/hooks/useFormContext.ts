import { useContext } from 'react'

import { FormContext } from '../provider'

export const useFormContext = () => {
    const context = useContext(FormContext)

    if (!context) {
        throw new Error('useFormContext must be used with FormProvider')
    }

    return context
}
