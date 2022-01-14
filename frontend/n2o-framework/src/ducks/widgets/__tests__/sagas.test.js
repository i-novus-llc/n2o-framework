import { put } from 'redux-saga/effects'

import { setModel } from '../../models/store'
import { PREFIXES } from '../../models/constants'

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
            put(setModel(PREFIXES.datasource, action.payload.widgetId, null)).payload,
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
                    PREFIXES.resolve,
                    action.payload.modelId,
                    action.payload.model,
                ),
            ).payload,
        )
    })
})
