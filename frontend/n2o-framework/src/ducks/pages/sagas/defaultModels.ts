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
    appendToArray,
    combineModels,
    removeModel,
    setModel,
    updateModel,
} from '../../models/store'
// @ts-ignore import from js file
import linkResolver from '../../../utils/linkResolver'
import { INDEX_REGEXP } from '../../../core/validation/const'
import { getFieldPath, getFullModelPath } from '../../../core/models/getModelPath'
import { logger } from '../../../utils/logger'
import { Model } from '../../../core/models/types'

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

function splitPath(input: string): { pathToModel: string; tail: string } {
    const safeRegex = new RegExp(INDEX_REGEXP.source, INDEX_REGEXP.flags.replace('g', ''))

    const pos = input.search(safeRegex)

    if (pos === -1) {
        logger.warn(`No key matching ${INDEX_REGEXP.source} found in the input string`)
    }

    return {
        pathToModel: input.substring(0, pos),
        tail: input.substring(pos),
    }
}

function replaceIndexKey(tail: string, value: number | string): string {
    const safeRegex = new RegExp(INDEX_REGEXP.source, INDEX_REGEXP.flags.replace('g', ''))

    return tail.replace(safeRegex, `[${value}]`)
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
            removeModel.type,
            updateModel.type,
        ]
        let oldState = {}

        yield takeEvery(modelsChan, function* watcher(action) {
            const newState: State = yield select()
            const changedModels = pickBy(
                observable,
                cfg => !isEqual(get(oldState, cfg.link), get(newState, cfg.link)),
            )

            const modelsToCombine = compareAndResolve(changedModels, newState).combine

            // @INFO установка default элементам спиковой модели
            const modelsToArrayFields = compareAndResolve(changedModels, newState).arrayFields

            // FIXME (удалить) временный костыль, нужно для установки default поля элементам спиковой модели
            //  Кейс: После получения данных datasource модель установит сервер, при этом ожидается что
            //   проставятся default поля, но source модель не изменятся (changedModels)
            let defaultArrayModels = {} as DefaultModels

            if (
                isEmpty(modelsToCombine) &&
                isEmpty(modelsToArrayFields) &&
                // @ts-ignore FIXME (удалить) временно зашился на конкретный случай
                action.type === setModel.type && action?.payload?.isDefault) {
                defaultArrayModels = compareAndResolve(observable, newState).arrayFields
            }

            const arrayFields = isEmpty(modelsToArrayFields) ? defaultArrayModels : modelsToArrayFields

            if (!isEmpty(modelsToCombine)) {
                yield put(combineModels(modelsToCombine))
            } else if (!isEmpty(arrayFields)) {
                const [[path, { link }]] = Object.entries(arrayFields)

                const { pathToModel, tail } = splitPath(path)

                const { models } = newState
                const value = get(newState, link)
                const combine = {}
                const targetModel = get(models, pathToModel)

                targetModel?.forEach((_model: Model, index: number) => {
                    const resolvedTail = replaceIndexKey(tail, index)
                    const fullPath = `${pathToModel}${resolvedTail}`

                    set(combine, fullPath, value)
                })

                if (!isEmpty(combine)) {
                    yield put(combineModels(combine))
                }
            }

            oldState = newState
        })
    }

    if (!isEmpty(arrayFields)) {
        yield takeEvery([appendToArray], function* watcher({ payload }) {
            const state: State = yield select()
            const { modelLink, fieldName, position } = payload

            if (!fieldName) { return }

            const fieldPath = getFieldPath({ ...modelLink, field: fieldName })
            const arrSymbol = '[*]'
            const fieldMask = `${fieldPath.replaceAll(/\[\d+]/g, arrSymbol)}${arrSymbol}`
            const combine: Partial<ModelsState> = {}
            const index = position ?? get(state, getFullModelPath({ ...modelLink, field: fieldName })).length - 1
            const itemPath = `${fieldPath}[${index}]`
            const ctx = createContext(itemPath)

            for (const [path, value] of Object.entries(arrayFields)) {
                const pathMask = path.replaceAll(INDEX_REGEXP, `${arrSymbol}`)

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
