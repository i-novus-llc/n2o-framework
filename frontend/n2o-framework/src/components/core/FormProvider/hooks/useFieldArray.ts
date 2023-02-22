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
    const { name: formName, prefix } = useFormContext()
    const dispatch = useDispatch()
    /**
     * Список элементов, обновляется только если изменилась длина списка
     */
    const fields = useSelector<State, T[]>((state: State) => {
        const value = getFormDataSelector(state, `${prefix}.${formName}.${fieldName}`)

        return (value === undefined || value === null) ? defaultValue : value
    }, isEqualArrayLength)

    /**
     * Добавление пустого элемента в массив
     */
    const append = useCallback(() => {
        dispatch(appendFieldToArray({ prefix, key: formName, fieldName, primaryKey }))
    }, [dispatch, fieldName, formName, prefix, primaryKey])

    /**
     * Удаление элемента по индексу или диапазон элементов
     */
    const remove = useCallback((index: number | [number, number]) => {
        dispatch(removeFieldFromArray(prefix, formName, fieldName, index))
    }, [dispatch, fieldName, formName, prefix])

    /**
     * Копирование элемента по индексу. Вставляется в конец массива
     */
    const copy = useCallback((index: number) => {
        dispatch(copyFieldArray(prefix, formName, fieldName, index, primaryKey))
    }, [dispatch, fieldName, formName, prefix, primaryKey])

    return ({
        fields,
        append,
        remove,
        copy,
    })
}
