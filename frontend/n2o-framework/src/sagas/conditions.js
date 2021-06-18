import {
    call,
    all,
    take,
    fork,
    takeEvery,
    cancelled,
} from 'redux-saga/effects'
import map from 'lodash/map'
import reduce from 'lodash/reduce'
import forOwn from 'lodash/forOwn'
import get from 'lodash/get'
import set from 'lodash/set'
import isEmpty from 'lodash/isEmpty'
import keys from 'lodash/keys'
import find from 'lodash/find'

import evalExpression from '../utils/evalExpression'
import { SET } from '../constants/models'
import { REGISTER_BUTTON } from '../constants/toolbar'
// eslint-disable-next-line import/no-cycle
import { resolveColumn } from '../ducks/columns/sagas'
import { registerColumn } from '../ducks/columns/store'

// eslint-disable-next-line import/no-cycle
import { resolveButton } from './toolbar'

/**
 * Обработчики вызова зависимостей
 */
const ConditionHandlers = {
    [REGISTER_BUTTON]: resolveButton,
    [registerColumn.type]: resolveColumn,
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
            const isMulti = modelLink.includes('multi')

            const type = isMulti ? { mode: 'multi' } : { mode: 'single' }

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
export function* watchModel(entities, { payload }) {
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

/**
 * Наблюдение за регистрацией сущностей и SET'ом моделей
 * @return
 */
function* watchRegister() {
    try {
        let entities = {}

        while (true) {
            const { type, payload: payloadRegister } = yield take([
                REGISTER_BUTTON,
                registerColumn.type,
            ])
            const { conditions } = payloadRegister

            if (conditions && !isEmpty(conditions)) {
                entities = yield call(prepareEntity, entities, payloadRegister, type)

                yield fork(ConditionHandlers[type], payloadRegister)
                // todo: Перейти на redux-saga@1.0.0 и использовать takeLeading
                yield takeEvery(SET, watchModel, entities)
            }
        }
    } finally {
        if (yield cancelled()) {
            // todo: добавить cancel саги, когда кнопка unregister
        }
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
    const newEntities = { ...entities }
    const modelsLinkBuffer = []

    forOwn(conditions, condition => map(condition, ({ modelLink }) => {
        if (!modelsLinkBuffer.includes(modelLink)) {
            const modelLinkArray = get(entities, [type, modelLink], null)
                ? [...get(entities, [type, modelLink], {}), { ...payload }]
                : [{ ...payload }]

            set(newEntities, [type, modelLink], modelLinkArray)
            modelsLinkBuffer.push(modelLink)
        }
    }))

    return newEntities
}

export const conditionsSaga = [fork(watchRegister)]
