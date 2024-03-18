import {
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import pickBy from 'lodash/pickBy'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'
import set from 'lodash/set'
import isEmpty from 'lodash/isEmpty'

import { DefaultModels, State as ModelsState } from '../../models/Models'
import { State } from '../../State'
import {
    appendFieldToArray,
    combineModels,
    copyModel,
    removeModel,
    setModel,
    updateModel,
} from '../../models/store'
// @ts-ignore import from js file
import linkResolver from '../../../utils/linkResolver'

const INDEX_REGEXP = /\[(index|\$index_\d+)]\./ig

const createContext = (fieldName: string) => {
    const match = fieldName.match(/\b\d+\b/g)

    if (!match) { return {} }

    const ctx: Record<string, number> = {}

    match.forEach((index, depth) => {
        if (depth === 0) {
            ctx.index = +index
        }

        ctx[`$index_${depth}`] = +index
    })

    return ctx
}

/**
 * Дополнительная функция для observeModels.
 * резолвит и сравнивает модели из стейта и резолв модели.
 * @param defaultModels - модели для резолва
 * @param stateModels - модели из стейта
 * @returns {*}
 */
export function compareAndResolve(
    defaultModels: DefaultModels,
    stateModels: State,
) {
    const combine: Partial<ModelsState> = {}
    const observable: DefaultModels = {}
    const arrayFields: DefaultModels = {}

    for (const [path, value] of Object.entries(defaultModels)) {
        if (path.match(INDEX_REGEXP)) {
            arrayFields[path] = value
        } else {
            const resolveValue = linkResolver(stateModels, value)
            const stateValue = get(stateModels, path)

            if (!isEqual(stateValue, resolveValue)) {
                set(combine, path, resolveValue)
            }
        }
        if (value.observe && value.link) {
            observable[path] = value
        }
    }

    return { combine, observable, arrayFields }
}

/**
 * Сага для первоначальной установки моделей по умолчанию
 * и подписка на изменения через канал
 * @param config - конфиг для моделей по умолчанию
 * @returns {boolean}
 */
// eslint-disable-next-line consistent-return
export function* flowDefaultModels(config: DefaultModels) {
    if (isEmpty(config)) { return false }

    const state: State = yield select()
    const { combine, observable, arrayFields } = compareAndResolve(config, state)

    if (!isEmpty(combine)) {
        yield put(combineModels(combine))
    }

    if (!isEmpty(observable)) {
        const modelsChan = [
            setModel.type,
            copyModel.type,
            removeModel.type,
            updateModel.type,
        ]
        let oldState = {}

        yield takeEvery(modelsChan, function* watcher() {
            const newState: State = yield select()
            const changedModels = pickBy(
                observable,
                cfg => !isEqual(get(oldState, cfg.link), get(newState, cfg.link)),
            )
            const newModels = compareAndResolve(
                changedModels,
                newState,
            ).combine

            if (!isEmpty(newModels)) {
                yield put(combineModels(newModels))
            }

            oldState = newState
        })
    }

    if (!isEmpty(arrayFields)) {
        yield takeEvery([appendFieldToArray], function* watcher({ payload }) {
            const state: State = yield select()
            const { prefix, key, field } = payload
            const fieldPath = `${prefix}['${key}'].${field}`
            const arrSymbol = '[*]'
            const fieldMask = `${fieldPath.replaceAll(/\[\d+]/g, arrSymbol)}${arrSymbol}`
            const combine: Partial<ModelsState> = {}
            const index = get(state, `models.${fieldPath}`).length - 1
            const itemPath = `${fieldPath}[${index}]`
            const ctx = createContext(itemPath)

            for (const [path, value] of Object.entries(arrayFields)) {
                const pathMask = path.replaceAll(INDEX_REGEXP, `${arrSymbol}.`)

                if (
                    pathMask.startsWith(fieldMask) &&
                    !pathMask.replace(fieldMask, '').includes(arrSymbol)
                ) {
                    const newPath = pathMask.replace(fieldMask, itemPath)

                    const resolveValue = linkResolver(state, value, ctx)
                    const stateValue = get(state, newPath)

                    if (!isEqual(stateValue, resolveValue)) {
                        set(combine, newPath, resolveValue)
                    }
                }
            }

            if (!isEmpty(combine)) {
                yield put(combineModels(combine))
            }
        })
    }
}
