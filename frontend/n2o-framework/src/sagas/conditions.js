import {
    all,
    fork,
    takeEvery,
    call,
} from 'redux-saga/effects'
import map from 'lodash/map'
import reduce from 'lodash/reduce'
import get from 'lodash/get'
import set from 'lodash/set'
import isEmpty from 'lodash/isEmpty'
import keys from 'lodash/keys'
import find from 'lodash/find'
import forOwn from 'lodash/forOwn'
import { values } from 'lodash'

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
import { registerButton, removeButton } from '../ducks/toolbar/store'
// eslint-disable-next-line import/no-cycle
import { resolveColumn } from '../ducks/columns/sagas'
import { registerColumn } from '../ducks/columns/store'
// eslint-disable-next-line import/no-cycle
import { resolveButton } from '../ducks/toolbar/sagas'

/**
 * Обработчики вызова зависимостей
 */
const ConditionHandlers = {
    [registerButton.type]: resolveButton,
    [registerColumn.type]: resolveColumn,
}

const REMOVE_TO_ADD_ACTIONS_NAMES_DICT = {
    [removeButton.type]: registerButton.type,
}

function evalExpressionMulti(expression, context) {
    const multiContext = Array.isArray(context) ? context : values(context)

    if (isEmpty(multiContext)) {
        return evalExpression(expression, multiContext, {})
    }

    return multiContext.every(item => evalExpression(expression, multiContext, item))
}

/**
 * резолв кондишена, резолв message из expression
 * @param conditions
 * @param state
 * @returns {object}
 */
export const resolveConditions = (conditions = [], state) => {
    const falsyExpressions = reduce(
        conditions,
        (acc, condition) => {
            const { expression, modelLink } = condition

            const context = get(state, modelLink, {})

            const evalResult = modelLink.includes('multi')
                ? evalExpressionMulti(expression, context)
                : evalExpression(expression, context)

            return !evalResult
                ? acc.concat(condition)
                : acc
        },
        [],
    )

    // message первого ложного expression
    const message = get(find(falsyExpressions, 'message'), 'message')

    return { resolve: isEmpty(falsyExpressions), message }
}

function* callConditionHandlers(entities, prefix, key, type) {
    const modelLink = `models.${prefix}['${key}']`
    const entity = get(entities, [type, modelLink], null)

    if (entity) {
        yield all(map(entity, entity => fork(ConditionHandlers[type], entity)))
    }
}

/**
 * резолв всех условий
 * @param entities
 * @param action
 */
function* watchModel(entities, action) {
    const { prefix, prefixes, key } = action.payload
    const groupTypes = keys(entities)

    for (let i = 0; i < groupTypes.length; i++) {
        const type = groupTypes[i]

        // setModel пришлет prefix
        if (prefix) {
            yield call(callConditionHandlers, entities, prefix, key, type)

            // clearModel пришлет prefixes
        } else if (prefixes) {
            for (const prefix of prefixes) {
                yield call(callConditionHandlers, entities, prefix, key, type)
            }
        }
    }
}

function* watchCombineModels(entities, { payload: { combine } }) {
    const groupTypes = keys(entities)
    const [prefix] = keys(combine)
    const [key] = keys(combine[prefix])

    for (let i = 0; i < groupTypes.length; i++) {
        const type = groupTypes[i]

        yield call(callConditionHandlers, entities, prefix, key, type)
    }
}

function watchRemove(entities, action) {
    const { type, payload } = action
    const conditions = entities[REMOVE_TO_ADD_ACTIONS_NAMES_DICT[type]]
    const modelLinks = keys(conditions)
    const newConditions = {}

    modelLinks.forEach((modelLink) => {
        const curModelLinkConditions = conditions[modelLink].filter(
            ({ key, buttonId }) => key !== payload.key || buttonId !== payload.buttonId,
        )

        if (curModelLinkConditions.length) {
            newConditions[modelLink] = curModelLinkConditions
        }
    })
    entities[REMOVE_TO_ADD_ACTIONS_NAMES_DICT[type]] = newConditions
}

function* conditionWatchers() {
    const entities = {}

    yield takeEvery([registerButton, registerColumn], watchRegister, entities)
    yield takeEvery([
        setModel,
        clearModel,
        updateModel,
        appendFieldToArray,
        removeFieldFromArray,
        copyFieldArray,
    ], watchModel, entities)
    yield takeEvery(combineModels, watchCombineModels, entities)
    yield takeEvery(removeButton, watchRemove, entities)
}

/**
 * Наблюдение за регистрацией сущностей
 * @return
 */
function* watchRegister(entities, { type, payload }) {
    const { conditions } = payload

    if (conditions && !isEmpty(conditions)) {
        prepareEntity(entities, payload, type)
        yield fork(ConditionHandlers[type], payload)
    }
}

/**
 * Группировка сущностей по их register_type, которые в свою очередь группируются по modelLink
 * @param entities
 * @param payload
 * @param type
 * @return {any}
 */
export function prepareEntity(entities, payload, type) {
    const { conditions } = payload
    const linksBuffer = []

    forOwn(conditions, condition => map(condition, ({ modelLink }) => {
        if (!linksBuffer.includes(modelLink)) {
            const entityData = get(entities, [type, modelLink], [])
            const modelLinks = [...entityData, payload]

            set(entities, [type, modelLink], modelLinks)

            linksBuffer.push(modelLink)
        }
    }))

    return entities
}

export const conditionsSaga = [fork(conditionWatchers)]
