import { useCallback } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { State } from '../../../../ducks/State'
import { getFormDataSelector } from '../../../../ducks/models/form/selectors'
import { appendFieldToArray, copyFieldArray, removeFieldFromArray } from '../../../../ducks/models/store'

import { useFormContext } from './useFormContext'

type TUseFieldArray<T> = {
    name: string
    defaultValue?: T[]
    primaryKey?: string
}

const isEqualArrayLength = (current: unknown[], prev: unknown[]) => current.length === prev.length

// TODO: добавить уникальный id на каждый эллемент
export const useFieldArray = <T>({ defaultValue = [], name: fieldName, primaryKey }: TUseFieldArray<T>) => {
    const { datasource, prefix } = useFormContext()
    const dispatch = useDispatch()
    /**
     * Список элементов, обновляется только если изменилась длина списка
     */
    const fields = useSelector<State, T[]>((state: State) => {
        const value = getFormDataSelector(state, `${prefix}.${datasource}.${fieldName}`)

        return (value === undefined || value === null) ? defaultValue : value
    }, isEqualArrayLength)

    /**
     * Добавление пустого элемента в массив
     */
    const append = useCallback(() => {
        dispatch(appendFieldToArray({ prefix, key: datasource, fieldName, primaryKey }))
    }, [dispatch, fieldName, datasource, prefix, primaryKey])

    /**
     * Удаление элемента по индексу или диапазон элементов
     */
    const remove = useCallback((start: number, end?: number) => {
        dispatch(removeFieldFromArray(prefix, datasource, fieldName, start, end))
    }, [dispatch, fieldName, datasource, prefix])

    /**
     * Копирование элемента по индексу. Вставляется в конец массива
     */
    const copy = useCallback((index: number) => {
        dispatch(copyFieldArray(prefix, datasource, fieldName, index, primaryKey))
    }, [dispatch, fieldName, datasource, prefix, primaryKey])

    return ({
        fields,
        append,
        remove,
        copy,
    })
}
