import { insertDrawer, insertOverlay } from '../../overlays/store'
import { openPagecreator } from '../page'
import { Action } from '../../Action'

const ERROR = 'завершает выполнение последовательности действий и последующие шаги не будут выполнены'
// The list of actions after which the sequence ends
const ERRORS = {
    [insertOverlay.type]: `Открытие модального окна ${ERROR}`,
    [insertDrawer.type]: `Открытие дровера ${ERROR}`,
    [openPagecreator.type]: `Открытие страницы ${ERROR}`,
}

export function stopTheSequence(action: Action) {
    if (action?.meta?.operationId) { throw new Error(ERRORS[action.type]) }
}
