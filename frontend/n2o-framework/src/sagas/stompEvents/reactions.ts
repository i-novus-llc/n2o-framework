import isEmpty from 'lodash/isEmpty'

import { updateModel } from '../../ducks/models/store'
import { ModelPrefix } from '../../core/models/types'

import type { Emitter, StompMessage } from './types'

/**
 * Реакция на ответ с данными модели
 */
export function subscribeMetadata(emitter: Emitter, dataSourceId: string) {
    // eslint-disable-next-line consistent-return
    return (response: StompMessage) => {
        if (response.body) {
            const metadata = JSON.parse(response.body) || {}
            const responseKeys = Object.keys(metadata)

            if (responseKeys.length) {
                for (const fieldKey of responseKeys) {
                    // @ts-ignore TODO почему то обновляется ds model, нужно понять для чего и поменять
                    emitter(updateModel(ModelPrefix.source, dataSourceId, fieldKey, metadata[fieldKey]))
                }
            }
        } else {
            return {}
        }
    }
}

/**
 * Реакция на ответ с redux action
 */
export function doReduxAction(emitter: Emitter) {
    return (response: StompMessage) => {
        if (response.body) {
            const action = JSON.parse(response.body) || {}

            if (!isEmpty(action)) {
                emitter(action)
            } else {
                return {}
            }
        }

        return {}
    }
}
