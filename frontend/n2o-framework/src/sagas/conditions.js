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
import forOwn from 'lodash/forOwn'

import evalExpression from '../utils/evalExpression'
import { SET } from '../constants/models'
import { REGISTER_BUTTON, REMOVE_BUTTON } from '../constants/toolbar'
import { REGISTER_COLUMN } from '../constants/columns'

import { resolveButton } from './toolbar'
import { resolveColumn } from './column'

/**
 * Обработчики вызова зависимостей
 */
const ConditionHandlers = {
    [REGISTER_BUTTON]: resolveButton,
    [REGISTER_COLUMN]: resolveColumn,
}

const REMOVE_TO_ADD_ACTIONS_NAMES_DICT = {
    [REMOVE_BUTTON]: REGISTER_BUTTON,
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

    yield takeEvery([REGISTER_BUTTON, REGISTER_COLUMN], watchRegister, entities)
    yield takeEvery(SET, watchModel, entities)
    yield takeEvery(REMOVE_BUTTON, watchRemove, entities)
}
/**
 * Наблюдение за регистрацией сущностей и SET'ом моделей
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
