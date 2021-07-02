import { call, put, select, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import filter from 'lodash/filter'
import every from 'lodash/every'
import some from 'lodash/some'
import printJS from 'print-js'

import { dataProviderResolver } from '../../core/dataProviderResolver'
// eslint-disable-next-line import/no-cycle
import { resolveConditions } from '../../sagas/conditions'

import { getContainerButtons } from './selectors'
import { PRINT_BUTTON } from './constants'
import {
    changeButtonDisabled,
    changeButtonVisibility,
    changeButtonMessage,
} from './store'

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

function* print(action) {
    const state = yield select()
    const {
        url,
        pathMapping,
        queryMapping,
        fileType = 'pdf',
        loader = false,
        base64 = false,
    } = action.payload
    const { url: printUrl } = yield dataProviderResolver(state, {
        url,
        pathMapping,
        queryMapping,
    })

    printJS({
        printable: printUrl,
        fileType,
        showModal: loader,
        base64,
    })
}

// export function* handleAction(action) {
//   const buttons = yield select(
//     getContainerButtons(action.payload.key || action.payload.widgetId)
//   );
//   yield all(values(buttons || []).map(v => call(resolveEntity, v)));
// }

export default [takeEvery(PRINT_BUTTON, print)]
