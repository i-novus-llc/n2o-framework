import { createAction } from '@reduxjs/toolkit'
import { cloneDeep, get, set } from 'lodash'
import { put, select } from 'redux-saga/effects'

import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { setModel } from '../../models/store'
import { ModelPrefix } from '../../../core/datasource/const'
import { MODELS_PREFIX } from '../constants'

import { EMPTY_ELEMENT, NOT_ARRAY, Operations, UNKNOWN_OPERATION } from './const'
import { create } from './editList/create'
import { update } from './editList/update'
import { deleteItem } from './editList/delete'
import { deleteMany } from './editList/deleteMany'

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

        let targetList = list.field ? get(targetModel, list.field) : targetModel

        if (!targetList && (operation === Operations.create || operation === Operations.createMany)) {
            targetList = []
        }

        if (!Array.isArray(targetList)) {
            throw new Error(NOT_ARRAY)
        }

        const element = item.field ? get(sourceModel, item.field) : sourceModel

        if (!element) {
            throw new Error(EMPTY_ELEMENT)
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

function updateList<TItem extends object>(
    list: TItem[],
    item: TItem | TItem[],
    primaryKey: keyof TItem,
    operation: Operations,
): TItem[] {
    switch (operation) {
        case Operations.createMany:
        case Operations.create: { return create(list, item) }
        case Operations.update: { return update(list, item as TItem, primaryKey) }
        case Operations.delete: { return deleteItem(list, item as TItem, primaryKey) }
        case Operations.deleteMany: { return deleteMany(list, item as TItem[], primaryKey) }
        default: {
            throw new Error(UNKNOWN_OPERATION)
        }
    }
}
