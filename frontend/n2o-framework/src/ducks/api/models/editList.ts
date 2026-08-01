import { createAction } from '@reduxjs/toolkit'
import { put, select } from 'redux-saga/effects'

import { getByLinkSelector, Model } from '../../models/selectors'
import { setModel, updateModel } from '../../models/store'
import { ModelPrefix, FieldLink, ModelLink } from '../../../core/models/types'
import { MODELS_PREFIX } from '../constants'
import { logger } from '../../../utils/logger'

import { EMPTY_ELEMENT, NOT_ARRAY, Operations, UNKNOWN_OPERATION } from './const'
import { create } from './editList/create'
import { update } from './editList/update'
import { deleteItem } from './editList/delete'
import { deleteMany } from './editList/deleteMany'

type Link = {
    datasource: string
    model: ModelPrefix
    field?: string
}

export type Payload = {
    operation: Operations
    primaryKey: string
    list: Link
    item: Link
}

export const creator = createAction(
    `${MODELS_PREFIX}edit_list`,
    (payload: Payload, meta: object) => ({
        payload,
        meta,
    }),
)

const mapLink = (link: Link): FieldLink | ModelLink => ({ prefix: link.model, id: link.datasource, field: link.field })

export function* effect({ payload, type }: ReturnType<typeof creator>) {
    try {
        const { operation, item, list, primaryKey } = payload
        const listModelLink = mapLink(list)
        const listModel: Model[] = yield select(getByLinkSelector(listModelLink, []))
        const itemModel: Model | Model[] = yield select(getByLinkSelector(mapLink(item), []))

        if (!Array.isArray(listModel)) { throw new Error(NOT_ARRAY) }

        if (!itemModel) { throw new Error(EMPTY_ELEMENT) }

        const newList = updateList(listModel, itemModel, primaryKey, operation)

        if (list.field) {
            yield put(updateModel(listModelLink, list.field, newList))
        } else {
            yield put(setModel(listModelLink, newList))
        }
    } catch (error) {
        const message = error instanceof Error ? error.message : error

        logger.warn(`Ошибка выполнения операции "${type}": ${message}`)
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
