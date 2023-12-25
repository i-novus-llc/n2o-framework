import { call, put, select, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import filter from 'lodash/filter'
import every from 'lodash/every'
import some from 'lodash/some'
import uniqueId from 'lodash/uniqueId'
import printJS from 'print-js'

// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../../core/dataProviderResolver'
// @ts-ignore ignore import error from js file
// eslint-disable-next-line import/no-cycle
import { resolveConditions } from '../../sagas/conditions'
// @ts-ignore ignore import error from js file
import request from '../../utils/request'
import { State } from '../State'
import { SequenceMeta } from '../api/action/sequence'
import { failOperation, startOperation, successOperation } from '../api/Operation'

import { getContainerButtons } from './selectors'
import {
    DEFAULT_PRINT_ERROR_MESSAGE,
    DEFAULT_PRINT_INCOMPATIBLE_BROWSER_MESSAGE,
    PRINT_BUTTON,
    PrintType,
} from './constants'
import { changeButtonDisabled, changeButtonMessage, changeButtonVisibility } from './store'
import { IButton } from './Toolbar'
import { Print } from './Actions'

/**
 * Resolve buttons conditions
 * @param button
 * @return
 */
export function* resolveButton(button: IButton) {
    const state: State = yield select()

    if (button.conditions) {
        const { visible, enabled } = button.conditions

        if (visible) {
            const nextVisible = resolveConditions(state, visible).resolve

            yield put(
                changeButtonVisibility(button.key, button.buttonId, nextVisible),
            )
            yield call(setParentVisibleIfAllChildChangeVisible, button)
        }

        if (enabled) {
            const resolvedEnabled = resolveConditions(state, enabled)

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
export function* setParentVisibleIfAllChildChangeVisible({ key, buttonId }: { key: string, buttonId: string }) {
    const buttons: IButton[] = yield select(getContainerButtons(key))
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

function printText(text: string, keepIndent: boolean) {
    const uniqID = uniqueId('n2o-print-block-')
    const printFrameStyles = 'position: absolute; left: -1000000px; top: -1000000px; width: 100%; height: auto; z-index: -1000'

    const printFrame = document.createElement('iframe')

    printFrame.id = uniqID
    printFrame.name = uniqID
    printFrame.setAttribute('style', printFrameStyles)

    document.body.appendChild(printFrame)

    /* FIXME */
    // @ts-ignore поправить типы
    const iFrame = window.frames[uniqID]

    iFrame.document.write(keepIndent ? `<pre>${text}</pre>` : text)

    iFrame.addEventListener('afterprint', () => printFrame.remove())
    iFrame.print()
}

function* print(action: Print) {
    try {
        const state: State = yield select()
        const {
            url,
            pathMapping,
            queryMapping,
            printable = null,
            type = PrintType.TEXT,
            keepIndent,
            documentTitle,
            loader = false,
            loaderText,
            base64 = false,
        } = action.payload

        const onError = (text: string) => (err: unknown) => {
            alert(text)
            console.error(err)
        }

        const printConfig: printJS.Configuration = {
            printable,
            // @ts-ignore непонятно как работает, в printJS.Configuration нет type === text
            type,
            documentTitle,
            showModal: loader,
            modalMessage: loaderText,
            base64,
            onError: onError(DEFAULT_PRINT_ERROR_MESSAGE),
            // @ts-ignore непонятно как работает onIncompatibleBrowser в в printJS.Configuration не принимает параметров
            onIncompatibleBrowser: onError(DEFAULT_PRINT_INCOMPATIBLE_BROWSER_MESSAGE),
        }

        if (url) {
            const { url: printUrl } = yield dataProviderResolver(state, {
                url,
                pathMapping,
                queryMapping,
            })

            if (type === PrintType.TEXT) {
                const text: string = yield request(printUrl, {}, { parseJson: false })

                printText(text, keepIndent)

                return
            }

            if (type === PrintType.PDF) {
                if (base64) {
                    const canPrint: boolean = yield request(printUrl, {}, { parseJson: false })

                    printConfig.printable = canPrint
                } else {
                    printConfig.printable = printUrl
                }
            } else if (type === PrintType.IMAGE) {
                printConfig.printable = printUrl
            }
        }

        printJS(printConfig)
    } catch (err) {
        console.error(err)
    }
}

function* startOperationEffect({ key, buttonId }: SequenceMeta) {
    if (key && buttonId) { yield put(changeButtonDisabled(key, buttonId, true)) }
}

function* endOperationEffect({ key, buttonId }: SequenceMeta) {
    if (key && buttonId) { yield put(changeButtonDisabled(key, buttonId, false)) }
}

export default [
    takeEvery(PRINT_BUTTON, print),
    takeEvery([startOperation], ({ meta }) => startOperationEffect(meta)),
    takeEvery([failOperation, successOperation], ({ meta }) => endOperationEffect(meta)),
]
