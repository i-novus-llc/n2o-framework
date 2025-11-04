import {
    fork,
    takeEvery,
    call,
    select,
} from 'redux-saga/effects'
import get from 'lodash/get'

import evalExpression from '../utils/evalExpression'
import {
    setModel,
    clearModel,
    appendFieldToArray,
    removeFieldFromArray,
    copyFieldArray,
    updateModel,
    combineModels,
} from '../ducks/models/store'
import { MergeModelAction } from '../ducks/models/Actions'
import { registerButton } from '../ducks/toolbar/store'
import { RegisterButton } from '../ducks/toolbar/Actions'
import { toolbarSelector } from '../ducks/toolbar/selectors'
import { State as ToolbarState, Condition } from '../ducks/toolbar/Toolbar'
// eslint-disable-next-line import/no-cycle
import { resolveButton } from '../ducks/toolbar/sagas'
import { State as GlobalState } from '../ducks/State'

/**
 * резолв кондишена, резолв message из expression
 * @param conditions
 * @param state
 * @returns {object}
 */
export const resolveConditions = (
    state: GlobalState,
    conditions: Condition[] = [],
): { resolve: boolean, message: string | undefined } => {
    let resolve = true

    for (const { expression, modelLink, message } of conditions) {
        const context = get(state, modelLink) || {}
        const evalResult = evalExpression(expression, context)

        resolve = resolve && !!evalResult

        // until find first message
        if (!evalResult && message) {
            return { resolve: false, message }
        }
    }

    return { resolve, message: undefined }
}

function* callConditionHandlers(
    prefix: string,
    key: string,
) {
    const modelLink = `models.${prefix}['${key}']`
    const toolbar: ToolbarState = yield select(toolbarSelector)

    for (const buttons of Object.values(toolbar)) {
        const temp = new Set<string>()

        for (const [buttonId, button] of Object.entries(buttons)) {
            if (
                (button.conditions?.enabled?.some(c => c.modelLink === modelLink)) ||
                (button.conditions?.visible?.some(c => c.modelLink === modelLink))
            ) {
                if (!temp.has(buttonId)) {
                    yield fork(resolveButton, button)
                }

                temp.add(buttonId)
            }
        }
    }
}

interface WatchModelPayload {
    prefix: string
    prefixes: string
    key: string
}

function* watchModel(action: { payload: WatchModelPayload }) {
    const { prefix, prefixes, key } = action.payload

    // setModel пришлет prefix
    if (prefix) {
        yield call(callConditionHandlers, prefix, key)

        // clearModel пришлет prefixes
    } else if (prefixes) {
        for (const prefix of prefixes) {
            yield call(callConditionHandlers, prefix, key)
        }
    }
}

function* watchCombineModels({ payload: { combine } }: MergeModelAction) {
    for (const [prefix, models] of Object.entries(combine)) {
        for (const key of Object.keys(models)) {
            yield call(callConditionHandlers, prefix, key)
        }
    }
}

function* conditionWatchers() {
    yield takeEvery(registerButton, watchRegister)
    // @ts-ignore проблема с типизацией saga
    yield takeEvery([
        setModel,
        clearModel,
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], watchModel)
    yield takeEvery(combineModels, watchCombineModels)
}

function* watchRegister({ payload }: RegisterButton) {
    yield fork(resolveButton, payload)
}

export const conditionsSaga = [fork(conditionWatchers)]
