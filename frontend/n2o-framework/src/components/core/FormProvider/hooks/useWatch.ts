import { useSelector } from 'react-redux'

import { getFormDataSelector } from '../../../../ducks/models/form/selectors'
import { State } from '../../../../ducks/State'

import { useFormContext } from './useFormContext'

type TUseWatch<T> = {
    name?: string
    defaultValue?: T
}

export const useWatch = <T = unknown>({ defaultValue, name: fieldName }: TUseWatch<T>) => {
    const { datasource, prefix } = useFormContext()

    return useSelector<State, T>((state: State) => {
        const value = getFormDataSelector(state, `${prefix}.${datasource}.${fieldName}`)

        return typeof value === 'undefined' ? defaultValue : value
    })
}
