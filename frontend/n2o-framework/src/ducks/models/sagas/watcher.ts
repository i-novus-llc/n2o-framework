import { debounce, fork, takeEvery } from 'redux-saga/effects'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

import { logger } from '../../../utils/logger'
import { getOnAppend, getOnRemove, mapMultiFields } from '../../../core/models/mapMultiFields'
import { ModelLink, ModelPrefix } from '../../../core/models/types'
import { remove as removeDatasource } from '../../datasource/store'
import { diffKeys } from '../../form/helpers'
import { getModelLink } from '../../../core/models/getModelLink'
import {
    appendToArray,
    clearModel,
    copyFieldArray,
    removeFromArray,
    setModel,
    updateModel,
    removeAllModel,
    combineModels,
} from '../store'
import { SetModelAction } from '../Actions'
import { Model } from '../selectors'

import { Handler } from './types'
import { getSubscribers } from './subscribe'

type BufferValue = {
    keys: Record<ModelLink, true>
    handlers: Set<Handler>
}

const allModels = [ModelPrefix.active, ModelPrefix.edit, ModelPrefix.filter, ModelPrefix.selected, ModelPrefix.source]
let buffer: BufferValue = { keys: {}, handlers: new Set() }

const addSubscriberToBuffer = (action: unknown) => {
    getSubscribers().forEach(({ handler, predicate }) => {
        if (!predicate || predicate(action)) { buffer.handlers.add(handler) }
    })
}

export const sagas = [
    /// accumulate changes to buffer
    takeEvery(setModel.type, (action: SetModelAction) => {
        const { prefix, key: id, model } = action.payload
        const { prevState } = action.meta || {}
        const key = getModelLink(prefix, id)
        const prevModel = prevState?.models[prefix][id]

        if (isEmpty(model) && isEmpty(prevModel)) { return }

        addSubscriberToBuffer(action)
        buffer.keys[key] = true

        // TODO array diff
        if (!Array.isArray(model) && !Array.isArray(prevModel)) {
            diffKeys(model, prevModel as Model).forEach((field) => {
                buffer.keys[getModelLink(prefix, id, field)] = true
            })
        }
    }),
    takeEvery(combineModels, (action) => {
        const { combine } = action.payload

        if (isEmpty(combine)) { return }

        addSubscriberToBuffer(action)

        for (const [prefix, models] of Object.entries(combine)) {
            for (const [id, model] of Object.entries(models)) {
                for (const field of Object.keys(model)) {
                    buffer.keys[getModelLink(prefix as ModelPrefix, id, field)] = true
                }
            }
        }
    }),
    takeEvery([
        clearModel,
        removeAllModel,
    ], (action) => {
        addSubscriberToBuffer(action)

        const { key: id } = action.payload
        const prefixes: ModelPrefix[] = get(action.payload, 'prefixes', allModels)

        prefixes.forEach((prefix) => {
            buffer.keys[getModelLink(prefix, id)] = true
        })
    }),
    takeEvery(updateModel, (action) => {
        const { field, key: id, prefix, value } = action.payload
        const key = getModelLink(prefix, id, field)

        if (!isEqual(value, get(action.meta?.prevState, key))) {
            addSubscriberToBuffer(action)
            buffer.keys[key] = true
        }
    }),
    takeEvery(copyFieldArray, (action) => {
        addSubscriberToBuffer(action)

        const { field, key: id, prefix } = action.payload
        const key = getModelLink(prefix, id, field)

        buffer.keys[key] = true
    }),
    takeEvery(appendToArray, (action) => {
        addSubscriberToBuffer(action)

        const { field, position, key: id, prefix } = action.payload
        const key = getModelLink(prefix, id, field)

        buffer.keys[key] = true

        buffer.keys = mapMultiFields(
            buffer.keys,
            key,
            getOnAppend<true>(field, position),
        )
    }),
    takeEvery(removeFromArray, (action) => {
        addSubscriberToBuffer(action)

        const { field, start, count = 1, key: id, prefix } = action.payload
        const key = getModelLink(prefix, id, field)

        buffer.keys[key] = true

        buffer.keys = mapMultiFields(
            buffer.keys,
            key,
            getOnRemove<true>(field, start, count),
        )
    }),

    /// garbage
    takeEvery(removeDatasource, ({ payload }) => {
        for (const prefix of allModels) {
            buffer.keys = Object.fromEntries(
                Object
                    .entries(buffer.keys)
                    .filter(key => !key[0].startsWith(getModelLink(prefix, payload.id))),
            )
        }
    }),

    /// fire
    // TODO dynamic debounce for subscribers
    debounce(300, [
        appendToArray,
        clearModel,
        copyFieldArray,
        combineModels,
        removeFromArray,
        setModel,
        updateModel,
        removeAllModel,
    ], function* callHandlers() {
        const currentBuffer = buffer

        buffer = { keys: {}, handlers: new Set() }
        const keys = Object.keys(currentBuffer.keys) as ModelLink[]

        for (const handler of currentBuffer.handlers) {
            try {
                yield fork(handler, keys)
            } catch (error) {
                logger.warn(error)
            }
        }
    }),
]
