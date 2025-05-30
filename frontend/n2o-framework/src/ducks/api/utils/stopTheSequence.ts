import { insertDrawer, insertOverlay } from '../../overlays/store'
import { openPageCreator } from '../page'
import { Action } from '../../Action'

export const ERROR = 'завершает выполнение последовательности действий и последующие шаги не будут выполнены'
// The list of actions after which the sequence ends
const ERRORS = {
    [insertOverlay.type]: `Открытие модального окна ${ERROR}`,
    [insertDrawer.type]: `Открытие дровера ${ERROR}`,
    [openPageCreator.type]: `Открытие страницы ${ERROR}`,
}

export function stopTheSequence(action: Action) {
    if (action?.meta?.operationId) {
        const { abortController } = action.meta

        abortController?.abort()

        throw new Error(ERRORS[action.type])
    }
}
