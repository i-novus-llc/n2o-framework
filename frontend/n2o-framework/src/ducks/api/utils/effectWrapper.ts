import { put } from 'redux-saga/effects'

import { Action } from '../../Action'
import { failOperation, startOperation, successOperation } from '../Operation'

/**
 * Обёртка над сагами, проверяющая наличие operationId в экшене
 * и запускающая экшены начала/конца работы операции
 */
export function EffectWrapper<
    TAction extends Action
>(effect: (action: TAction) => unknown) {
    return function* wrappedEffect(...args: Parameters<typeof effect>) {
        const action = args[args.length - 1] as TAction
        const { meta, type } = action
        const operationId = meta?.operationId

        if (!operationId) {
            return (yield effect(...args)) as ReturnType<typeof effect>
        }

        try {
            yield put(startOperation(type, operationId))

            const result: ReturnType<typeof effect> = yield effect(...args)

            yield put(successOperation(type, operationId))

            return result
        } catch (error) {
            yield put(failOperation(
                type,
                operationId,
                error instanceof Error ? error.message : error,
            ))

            throw error
        }
    }
}
