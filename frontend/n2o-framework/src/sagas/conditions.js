import {
    all,
    fork,
    takeEvery,
} from 'redux-saga/effects'
import map from 'lodash/map'
import reduce from 'lodash/reduce'
import get from 'lodash/get'
import set from 'lodash/set'
import isEmpty from 'lodash/isEmpty'
import keys from 'lodash/keys'
import find from 'lodash/find'

import evalExpression from '../utils/evalExpression'
import { setModel } from '../ducks/models/store'
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

            const type = modelLink.includes('multi') ? { mode: 'multi' } : { mode: 'single' }

            return !evalExpression(expression, context, type)
                ? acc.concat(condition)
                : acc
        },
        [],
    )

    // message первого ложного expression
    const message = get(find(falsyExpressions, 'message'), 'message')

    return { resolve: isEmpty(falsyExpressions), message }
}

/**
 * резолв всех условий
 * @param entities
 * @param action
 */
function* watchModel(entities, { payload }) {
    const { prefix, key } = payload
    const groupTypes = keys(entities)
    const modelLink = `models.${prefix}['${key}']`

    for (let i = 0; i < groupTypes.length; i++) {
        const type = groupTypes[i]
        const entity = get(entities, [type, modelLink], null)

        if (entity) {
            yield all(map(entity, entity => fork(ConditionHandlers[type], entity)))
        }
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

    yield takeEvery([registerButton.type, registerColumn.type], watchRegister, entities)
    yield takeEvery(setModel, watchModel, entities)
    yield takeEvery(removeButton.type, watchRemove, entities)
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
    const handledModelLinks = {}

    // collect conditions objects grouping by modelLink
    keys(conditions).forEach((conditionType) => { // conditionType -- enabled, visible, etc(?)
        conditions[conditionType].forEach((conditionItem) => {
            const { modelLink } = conditionItem

            if (!handledModelLinks[modelLink]) {
                handledModelLinks[modelLink] = { [conditionType]: [] }
            }
            if (!handledModelLinks[modelLink][conditionType]) {
                handledModelLinks[modelLink][conditionType] = []
            }
            handledModelLinks[modelLink][conditionType].push(conditionItem)
        })
    })

    // iterate through modelLinks groups and set data to entity
    keys(handledModelLinks).forEach((modelLink) => {
        const currentModelLinkConditions = get(entities, [type, modelLink])

        if (Array.isArray(currentModelLinkConditions)) {
            currentModelLinkConditions.push({ ...payload, conditions: handledModelLinks[modelLink] })
        } else {
            set(entities, [type, modelLink], [{ ...payload, conditions: handledModelLinks[modelLink] }])
        }
    })

    return entities
}

export const conditionsSaga = [fork(conditionWatchers)]
