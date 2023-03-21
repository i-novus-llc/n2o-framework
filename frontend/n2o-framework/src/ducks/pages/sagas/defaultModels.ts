import {
    actionChannel,
    ActionChannelEffect,
    call,
    cancelled,
    CancelledEffect,
    put,
    select,
    take,
} from 'redux-saga/effects'
import pickBy from 'lodash/pickBy'
import reduce from 'lodash/reduce'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'
import set from 'lodash/set'
import clone from 'lodash/clone'
import isEmpty from 'lodash/isEmpty'

import { DefaultModels, State as ModelsState } from '../../models/Models'
import { State } from '../../State'
import {
    combineModels,
    copyModel,
    removeModel,
    setModel,
    updateModel,
} from '../../models/store'
// @ts-ignore import from js file
import linkResolver from '../../../utils/linkResolver'

/**
 * Дополнительная функция для observeModels.
 * резолвит и сравнивает модели из стейта и резолв модели.
 * @param defaultModels - модели для резолва
 * @param stateModels - модели из стейта
 * @returns {*}
 */
export function compareAndResolve(defaultModels: DefaultModels, stateModels: State): Partial<ModelsState> {
    return reduce(
        defaultModels,
        (acc, value, path) => {
            const resolveValue = linkResolver(stateModels, value)
            const stateValue = get(stateModels, path)

            if (!isEqual(stateValue, resolveValue)) {
                return set(clone(acc), path, resolveValue)
            }

            return acc
        },
        {},
    )
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
    const initialModels: ModelsState = yield call(compareAndResolve, config, state)

    if (!isEmpty(initialModels)) {
        yield put(combineModels(initialModels))
    }

    const observableModels = pickBy(
        config,
        item => !!item.observe && !!item.link,
    )

    if (!isEmpty(observableModels)) {
        const modelsChan: ActionChannelEffect = yield actionChannel([
            setModel.type,
            copyModel.type,
            removeModel.type,
            updateModel.type,
        ])

        try {
            while (true) {
                const oldState: State = yield select()

                // @ts-ignore проблемы с типизацией
                yield take(modelsChan)
                const newState: State = yield select()
                const changedModels = pickBy(
                    observableModels,
                    cfg => !isEqual(get(oldState, cfg.link), get(newState, cfg.link)),
                )
                const newModels: ModelsState = yield call(
                    compareAndResolve,
                    changedModels,
                    newState,
                )

                if (!isEmpty(newModels)) {
                    yield put(combineModels(newModels))
                }
            }
        } finally {
            const task: CancelledEffect = yield cancelled()

            if (task) {
                // @ts-ignore проблемы с типизацией
                modelsChan.close()
            }
        }
    }
}
