import { Action } from '../../Action'

export function stopTheSequence(action: Action) {
    if (action?.meta?.operationId) {
        const { abortController } = action.meta

        abortController?.abort()

        throw new Error(`"${action.type}" прерывает выполнение последующей цепочки действий`)
    }
}
