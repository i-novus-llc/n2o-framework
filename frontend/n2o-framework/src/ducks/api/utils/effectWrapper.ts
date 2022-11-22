import { put } from 'redux-saga/effects'

import { Action } from '../../Action'
import { failOperation, startOperation, successOperation } from '../Operation'

/**
 * Обёртка над сагами, проверяющая наличие operationId в экшене
 * и запускающая экшены начала/конца работы операции
 */
export function EffectWrapper<
    TAction extends Action,
    TAgs extends [...rest: unknown[], action: TAction]
>(effect: (...args: TAgs) => unknown) {
    // eslint-disable-next-line consistent-return
    return function* wrappedEffect(...args: Parameters<typeof effect>) {
        const action = args[args.length - 1] as TAction
        const { meta, type } = action
        const operationId = meta?.operationId

        try {
            if (!operationId) {
                return (yield effect(...args)) as ReturnType<typeof effect>
            }

            yield put(startOperation(type, operationId))

            // TODO тут скорее всего надо удалить текущий operationId перед вызовом
            const result: ReturnType<typeof effect> = yield effect(...args)

            yield put(successOperation(type, operationId, result))

            return result
        } catch (error) {
            const message = error instanceof Error ? error.message : error

            if (operationId) {
                yield put(failOperation(
                    type,
                    operationId,
                    message,
                ))
            } else {
                console.warn(`Saga effect<${action.type}> error: ${message}`)
            }
        }
    }
}
