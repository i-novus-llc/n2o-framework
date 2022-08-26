import {
    put,
    call,
    take,
    race,
    select,
    cancelled,
    actionChannel,
    CancelledEffect,
    ActionChannelEffect,
} from 'redux-saga/effects'
import queryString from 'query-string'
import pickBy from 'lodash/pickBy'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import reduce from 'lodash/reduce'
import clone from 'lodash/clone'
import set from 'lodash/set'
import get from 'lodash/get'
import findIndex from 'lodash/findIndex'

// @ts-ignore import from js file
import linkResolver from '../../utils/linkResolver'
import {
    combineModels,
    copyModel,
    removeModel,
    setModel,
    updateMapModel,
    updateModel,
} from '../models/store'
import { State as ModelsState, DefaultModels } from '../models/Models'
import { routesQueryMapping } from '../datasource/Providers/service/routesQueryMapping'
import { State } from '../State'

// @ts-ignore import from js file, ругается на import/no-cycle, непонятно почему
// eslint-disable-next-line import/no-cycle
import { mappingUrlToRedux } from './sagas'
// @ts-ignore import from js file
import { resetPage } from './store'

interface Location {
    pathname: string
    search: string
}

// В connected-react-router кривой тип экшена
interface ILocationChangeAction {
    type: string
    payload: {
        location: Location
    }
}

const FiltersCache: Array<{ pageUrl: string, query: object }> = []
let prevPageUrl: string | null = null

// Смотрит за изменением location и кеширует фильтры по url страницы
export function watchPageFilters({ payload }: ILocationChangeAction) {
    const currentPageUrl = payload.location.pathname
    const isSamePage = prevPageUrl === currentPageUrl
    const cachedFiltersIndex = findIndex(FiltersCache, ({ pageUrl }) => pageUrl === currentPageUrl)
    const query = queryString.parse(payload.location.search)

    if (!isSamePage && cachedFiltersIndex === -1) {
        FiltersCache.push({ pageUrl: currentPageUrl, query: {} })
    } else if (isSamePage && cachedFiltersIndex !== -1) {
        FiltersCache[cachedFiltersIndex] = { pageUrl: currentPageUrl, query }
    }

    if (prevPageUrl !== currentPageUrl) {
        prevPageUrl = currentPageUrl
    }
}

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
            updateMapModel.type,
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

export function* resolvePageFilters(pageUrl: string, pageId: string, routes: object, defaultModels: DefaultModels) {
    const cachedFiltersIndex = findIndex(FiltersCache, page => page.pageUrl === pageUrl)
    const filtersFromCache = FiltersCache[cachedFiltersIndex]

    if (!isEmpty(filtersFromCache?.query)) {
        const { query } = filtersFromCache

        yield call(routesQueryMapping, pageId, routes, query)
        yield call(mappingUrlToRedux, routes)

        // все что после текущей страницы - чистим
        FiltersCache.splice(cachedFiltersIndex + 1)
    } else {
        yield race([call(flowDefaultModels, defaultModels), take(resetPage.type)])
    }
}
