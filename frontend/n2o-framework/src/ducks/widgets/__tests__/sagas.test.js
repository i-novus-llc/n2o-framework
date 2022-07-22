import { put } from 'redux-saga/effects'

import { setModel } from '../../models/store'
import { ModelPrefix } from '../../../core/datasource/const'

import {
    runResolve,
    clearOnDisable,
} from '../sagas'

describe('Проверка саги widgets', () => {
    it('Должен произойти clearOnDisable', () => {
        const action = {
            payload: {
                widgetId: 'testId',
                modelId: 'testId',
            },
        }
        const gen = clearOnDisable(action)
        expect(gen.next().value.payload).toEqual(
            put(setModel(ModelPrefix.source, action.payload.widgetId, null)).payload,
        )
    })

    it('Должен произойти runResolve', () => {
        const action = {
            payload: {
                modelId: 'testId',
                model: {
                    some: 'value',
                },
            },
        }
        const gen = runResolve(action)
        expect(gen.next().value.payload).toEqual(
            put(
                setModel(
                    ModelPrefix.active,
                    action.payload.modelId,
                    action.payload.model,
                ),
            ).payload,
        )
    })
})
