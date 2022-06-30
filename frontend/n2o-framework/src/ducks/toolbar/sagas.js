import { call, put, select, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import filter from 'lodash/filter'
import every from 'lodash/every'
import some from 'lodash/some'
import trim from 'lodash/trim'
import printJS from 'print-js'

import { dataProviderResolver } from '../../core/dataProviderResolver'
// eslint-disable-next-line import/no-cycle
import { resolveConditions } from '../../sagas/conditions'
import request from '../../utils/request'

import { getContainerButtons } from './selectors'
import {
    DEFAULT_PRINT_ERROR_MESSAGE,
    DEFAULT_PRINT_INCOMPATIBLE_BROWSER_MESSAGE,
    PRINT_BUTTON,
    PrintType,
} from './constants'
import { changeButtonDisabled, changeButtonMessage, changeButtonVisibility } from './store'

/**
 * Resolve buttons conditions
 * @param button
 * @return
 */
export function* resolveButton(button) {
    const state = yield select()

    if (button.conditions) {
        const { visible, enabled } = button.conditions

        if (visible) {
            const nextVisible = resolveConditions(visible, state).resolve

            yield put(
                changeButtonVisibility(button.key, button.buttonId, nextVisible),
            )
            yield call(setParentVisibleIfAllChildChangeVisible, button)
        }

        if (enabled) {
            const resolvedEnabled = resolveConditions(enabled, state)

            yield put(changeButtonDisabled(button.key, button.buttonId, !resolvedEnabled?.resolve))
            if (!resolvedEnabled?.resolve) {
                yield put(
                    changeButtonMessage(
                        button.key,
                        button.buttonId,
                        resolvedEnabled?.message,
                    ),
                )
            }
        }
    }

    if (button.resolveEnabled) {
        const { modelLink, on } = button.resolveEnabled
        const modelOnLink = get(state, modelLink, {})
        const nextEnabled = on.some(o => modelOnLink[o])

        yield put(changeButtonDisabled(button.key, button.buttonId, !nextEnabled))
    }
}

/**
 * Функция для мониторинга изменения видимости родителя списка
 * @param key
 * @param buttonId
 */
export function* setParentVisibleIfAllChildChangeVisible({ key, buttonId }) {
    const buttons = yield select(getContainerButtons(key))
    const currentBtn = get(buttons, buttonId)
    const parentId = get(currentBtn, 'parentId')

    if (parentId) {
        const currentBtnGroup = filter(buttons, ['parentId', parentId])

        const isAllChildHidden = every(currentBtnGroup, ['visible', false])
        const isAllChildVisible = some(currentBtnGroup, ['visible', true])
        const isParentVisible = get(buttons, [parentId, 'visible'], false)

        if (isAllChildHidden && isParentVisible) {
            yield put(changeButtonVisibility(key, parentId, false))
        }
        if (isAllChildVisible && !isParentVisible) {
            yield put(changeButtonVisibility(key, parentId, true))
        }
    }
}

function prepareHTMLToPrint(html) {
    const uniqID = 'n2o-print-block-53a6bb'
    const printBlockStyles = 'position: absolute; left: -1000000px; top: -1000000px; width: 100%; height: auto; z-index: -1000'

    const printBlock = document.createElement('div')

    printBlock.id = uniqID
    printBlock.style = printBlockStyles
    printBlock.innerHTML = trim(html)

    document.body.appendChild(printBlock)

    return {
        elementId: uniqID,
        cleanUp: () => printBlock.remove(),
    }
}

function* print(action) {
    const state = yield select()
    const {
        dataProvider,
        printable = null,
        type = PrintType.PDF,
        documentTitle,
        loader = false,
        loaderText,
        base64 = false,
    } = action.payload

    const onError = text => (err) => {
        alert(text)
        console.error(err)
    }

    const printConfig = {
        printable,
        type,
        documentTitle,
        showModal: loader,
        modalMessage: loaderText,
        base64,
        onError: onError(DEFAULT_PRINT_ERROR_MESSAGE),
        onIncompatibleBrowser: onError(DEFAULT_PRINT_INCOMPATIBLE_BROWSER_MESSAGE),
    }

    if (dataProvider) {
        const { url } = yield dataProviderResolver(state, dataProvider)

        if ((type === PrintType.PDF && !base64) || type === PrintType.IMAGE) {
            printConfig.printable = url
        } else if (type === PrintType.PDF && base64) {
            printConfig.printable = yield request(url)
        } else if (type === PrintType.HTML) {
            const html = yield request(url)

            const { elementId, cleanUp } = prepareHTMLToPrint(html)

            printConfig.printable = elementId
            printConfig.onPrintDialogClose = cleanUp
        }
    }

    printJS(printConfig)
}

// export function* handleAction(action) {
//   const buttons = yield select(
//     getContainerButtons(action.payload.key || action.payload.widgetId)
//   );
//   yield all(values(buttons || []).map(v => call(resolveEntity, v)));
// }

export default [takeEvery(PRINT_BUTTON, print)]
