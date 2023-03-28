import { put } from 'redux-saga/effects'

import { Action } from '../../Action'
import { failOperation, startOperation, successOperation } from '../Operation'

const getErrorMessage = (error: unknown): string => {
    if (!error) { return 'Unknown error' }
    if (typeof error === 'string') { return error }
    if (error instanceof Error && error.message) { return error.message }
    if (typeof error === 'object' && error.toString) { return error.toString() }

    return String(error)
}

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
            const message = getErrorMessage(error)

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
