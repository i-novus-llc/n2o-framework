import { put, select, takeEvery } from 'redux-saga/effects'
import uniqueId from 'lodash/uniqueId'
import printJS from 'print-js'

import { dataProviderResolver } from '../../core/dataProviderResolver'
// eslint-disable-next-line import/no-cycle
import { resolveConditions } from '../../sagas/conditions'
import request from '../../utils/request'
import { State } from '../State'
import { SequenceMeta, creator as sequence, finisher as sequenceEnd } from '../api/action/sequence'
import { failOperation, startOperation, successOperation } from '../api/Operation'

import {
    DEFAULT_PRINT_ERROR_MESSAGE,
    DEFAULT_PRINT_INCOMPATIBLE_BROWSER_MESSAGE,
    PRINT_BUTTON,
    PrintType,
} from './constants'
import { changeButtonDisabled, changeButtonMessage, changeButtonVisibility } from './store'
import { buttonSelector } from './selectors'
import { ButtonState } from './Toolbar'
import { Print } from './Actions'

/**
 * Resolve buttons conditions
 * @param button
 * @return
 */
export function* resolveButton({ buttonId, key }: Pick<ButtonState, 'buttonId' | 'key'>) {
    const state: State = yield select()
    const button: ButtonState | void = yield select(buttonSelector, key, buttonId)

    if (!button) { return }

    if (button.conditions) {
        const { visible, enabled } = button.conditions

        if (visible) {
            const nextVisible = resolveConditions(state, visible).resolve

            yield put(
                changeButtonVisibility(button.key, button.buttonId, nextVisible),
            )
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
            // eslint-disable-next-line no-console
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
            // @ts-ignore import from js file
            const { url: printUrl } = yield dataProviderResolver(state, { url, pathMapping, queryMapping })

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
        // eslint-disable-next-line no-console
        console.error(err)
    }
}

function* startOperationEffect({ key, buttonId }: SequenceMeta) {
    if (buttonId) { yield put(changeButtonDisabled(key, buttonId, true)) }
}

function* endOperationEffect({ key, buttonId }: SequenceMeta) {
    if (buttonId) { yield put(changeButtonDisabled(key, buttonId, false)) }
}

export default [
    takeEvery(PRINT_BUTTON, print),
    takeEvery([startOperation, sequence], ({ meta }) => startOperationEffect(meta)),
    takeEvery([failOperation, successOperation, sequenceEnd], ({ meta }) => endOperationEffect(meta)),
]
