import {
    fork,
    takeEvery,
    call,
    select,
} from 'redux-saga/effects'
import get from 'lodash/get'
import escapeRegExp from 'lodash/escapeRegExp'

import evalExpression from '../utils/evalExpression'
import {
    setModel,
    clearModel,
    appendToArray,
    removeFromArray,
    copyFieldArray,
    updateModel,
    combineModels,
} from '../ducks/models/store'
import { MergeModelAction } from '../ducks/models/Actions'
import { registerButton } from '../ducks/toolbar/store'
import { RegisterButton } from '../ducks/toolbar/Actions'
import { toolbarSelector } from '../ducks/toolbar/selectors'
import { type State as ToolbarState, type Condition, type ButtonState } from '../ducks/toolbar/Toolbar'
// eslint-disable-next-line import/no-cycle
import { resolveButton } from '../ducks/toolbar/sagas'
import { State as GlobalState } from '../ducks/State'
import { type ModelLink, ModelPrefix } from '../core/models/types'

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

function isMatchingLink(baseModelLink: string, conditionLink: string, prefix: ModelPrefix): boolean {
    if (prefix === ModelPrefix.source) {
        // @INFO исключение для source
        // Проверка начинается с baseLink, затем либо конец строки, либо '['
        // Необходимо для ссылок с индексом models.datasource['_ds2'][1] или models.datasource['_ds2']
        const regex = new RegExp(`^${escapeRegExp(baseModelLink)}(\\[|$)`)

        return regex.test(conditionLink)
    }

    return conditionLink === baseModelLink
}

function hasMatchingCondition(button: ButtonState, baseModelLink: ModelLink, prefix: ModelPrefix): boolean {
    const { conditions } = button

    if (!conditions) { return false }

    const check = (
        list?: Array<{ modelLink: string }>,
    ) => list?.some(c => isMatchingLink(baseModelLink, c.modelLink, prefix)) ?? false

    return check(conditions.enabled) || check(conditions.visible)
}

function* callConditionHandlers(prefix: ModelPrefix, key: string) {
    const baseModelLink: ModelLink = `models.${prefix}['${key}']`
    const toolbar: ToolbarState = yield select(toolbarSelector)

    for (const buttons of Object.values(toolbar)) {
        const temp = new Set<string>()

        for (const [buttonId, button] of Object.entries(buttons)) {
            if (
                !temp.has(buttonId) &&
                hasMatchingCondition(button, baseModelLink, prefix)
            ) {
                yield fork(resolveButton, button)
                temp.add(buttonId)
            }
        }
    }
}

interface WatchModelPayload {
    prefix: ModelPrefix
    prefixes: ModelPrefix[]
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
            // TODO посмотреть типизацию MergeModelAction, тут должно работать без 'as' присваивания
            yield call(callConditionHandlers, prefix as ModelPrefix, key)
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
        appendToArray,
        removeFromArray,
        copyFieldArray,
    ], watchModel)
    yield takeEvery(combineModels, watchCombineModels)
}

function* watchRegister({ payload }: RegisterButton) {
    yield fork(resolveButton, payload)
}

export const conditionsSaga = [fork(conditionWatchers)]
