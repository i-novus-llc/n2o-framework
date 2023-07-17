import { put } from 'redux-saga/effects'

import { setModel } from '../../models/store'
import { ModelPrefix } from '../../../core/datasource/const'

import {
    runResolve,
} from '../sagas'

describe('Проверка саги widgets', () => {
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
