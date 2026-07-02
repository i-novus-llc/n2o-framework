import { useCallback } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { State } from '../../../../ducks/State'
import { getByLinkSelector } from '../../../../ducks/models/selectors'
import { appendToArray, copyFieldArray, removeFromArray } from '../../../../ducks/models/store'

import { useFormContext } from './useFormContext'

type UseFieldArray<T> = {
    name: string
    defaultValue?: T[]
    primaryKey?: string
}

const isEqualArrayLength = <T extends unknown[]>(current: T, prev: T) => current.length === prev.length

// TODO: добавить уникальный id на каждый эллемент
export const useFieldArray = <T extends Record<string, unknown>>({ defaultValue = [], name: fieldName, primaryKey }: UseFieldArray<T>) => {
    const { modelLink } = useFormContext()
    const dispatch = useDispatch()
    /**
     * Список элементов, обновляется только если изменилась длина списка
     */
    const fields = useSelector((state: State) => {
        const value = getByLinkSelector({ ...modelLink, field: fieldName })(state)

        return value as T[] ?? defaultValue
    }, isEqualArrayLength)

    /**
     * Добавление пустого элемента в массив
     */
    const append = useCallback((position?: number) => {
        dispatch(appendToArray({ modelLink, fieldName, primaryKey, position }))
    }, [dispatch, fieldName, modelLink, primaryKey])

    /**
     * Удаление элемента по индексу или диапазон элементов
     */
    const remove = useCallback((start: number, count = 1) => {
        dispatch(removeFromArray({ modelLink, fieldName, start, count }))
    }, [dispatch, fieldName, modelLink])

    /**
     * Копирование элемента по индексу. Вставляется в конец массива
     */
    const copy = useCallback((index: number) => {
        dispatch(copyFieldArray(modelLink, fieldName, index, primaryKey))
    }, [dispatch, fieldName, modelLink, primaryKey])

    return ({
        fields,
        append,
        remove,
        copy,
    })
}
