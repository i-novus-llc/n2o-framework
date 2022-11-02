import { createAction } from '@reduxjs/toolkit'
import { cloneDeep, get, set } from 'lodash'
import { put, select } from 'redux-saga/effects'

import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { setModel } from '../../models/store'
import { ModelPrefix } from '../../../core/datasource/const'
import { MODELS_PREFIX } from '../constants'

import { NOT_ARRAY, Operations } from './const'
import { updateList } from './updateList'

type ModelLink = {
    datasource: string
    model: ModelPrefix
    field?: string
}

export type Payload = {
    operation: Operations
    primaryKey: string
    list: ModelLink
    item: ModelLink
}

export const creator = createAction(
    `${MODELS_PREFIX}edit_list`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

export function* effect({ payload, type }: ReturnType<typeof creator>) {
    try {
        const { operation, item, list, primaryKey } = payload

        const targetModel: object = yield select(makeGetModelByPrefixSelector(list.model, list.datasource))
        const sourceModel: object = yield select(makeGetModelByPrefixSelector(item.model, item.datasource))

        const targetList = list.field ? get(targetModel, list.field) : targetModel
        const element = item.field ? get(sourceModel, item.field) : sourceModel

        if (!Array.isArray(targetList)) {
            throw new Error(NOT_ARRAY)
        }

        const newList = updateList(targetList, element, primaryKey, operation)

        let newModel

        if (list.field) {
            newModel = cloneDeep(targetModel)
            set(newModel, list.field, newList)
        } else {
            newModel = newList
        }

        yield put(setModel(list.model, list.datasource, newModel))
    } catch (error) {
        const message = error instanceof Error ? error.message : error

        // eslint-disable-next-line no-console
        console.warn(`Ошибка выполнения операции "${type}": ${message}`)
    }
}
