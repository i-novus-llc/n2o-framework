import { put } from 'redux-saga/effects'

import { mergeMeta } from '../../core/Redux/utils/mergeMeta'
import { type FullModelPath, ModelPrefix } from '../../core/models/types'
import { getFullModelPath } from '../../core/models/getModelPath'
import { type N2OAction } from '../Action'
import { DEFAULT_CONTEXT } from '../../utils/evalExpression'

export const enum EventTypes {
    stomp = 'stomp',
    OnChange = 'OnChange',
}

export interface StompEvent {
    destination: string
    type: EventTypes.stomp
    id: string
}

export interface OnChangeEvent {
    datasource: string
    model: ModelPrefix
    field: string
    action: N2OAction
    type: EventTypes.OnChange
}

export type EventType = StompEvent | OnChangeEvent

export const isOnChangeEvent = (event: EventType): event is OnChangeEvent => event.type === EventTypes.OnChange
export const getOnChangeEvents = (events: EventType[]) => events.filter(isOnChangeEvent)

export const isStompEvent = (event: EventType): event is StompEvent => event.type === EventTypes.stomp
export const getStompEvents = (events: EventType[]) => events.filter(isStompEvent)

/**
 * Сага, обрабатывающая список событий и выполняющая действие,
 * если ключи модели соответствуют переданным.
 * @param events - массив событий, извлечённых из страниц
 * @param keys - ключи моделей, по которым происходит отслеживание
 */
export function* watchOnChangeEvents(
    events: OnChangeEvent[],
    isChanged: (modelPath: FullModelPath) => boolean,
) {
    for (const { datasource, model: prefix, field, action } of events) {
        const modelPath = getFullModelPath({ prefix, id: datasource, field })

        if (isChanged(modelPath)) {
            // FIXME костыльный проброс контекста
            yield put(mergeMeta(action, { evalContext: DEFAULT_CONTEXT }))
        }
    }
}
