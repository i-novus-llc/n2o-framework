import {
    call,
    put,
    select,
    takeEvery,
    take,
    debounce,
    race,
    fork,
    cancel,
    delay,
} from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import { getLocation } from 'connected-react-router'
import queryString from 'query-string'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'

import { destroyOverlay } from '../overlays/store'
import { FETCH_PAGE_METADATA } from '../../core/api'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { changeRootPage } from '../global/store'
import fetchSaga from '../../sagas/fetch'
import {
    clearModel,
    removeAllModel,
    removeModel,
    setModel,
    updateModel,
} from '../models/store'
import { modelsSelector } from '../models/selectors'
import { DefaultModels } from '../models/Models'
import { State } from '../State'
import { mergeMeta } from '../api/utils/mergeMeta'
import { DEFAULT_CONTEXT } from '../../utils/evalExpression'
import { userSelector } from '../user/selectors'
import { resolveMetadata } from '../../core/auth/resolveMetadata'
import { AuthProvider } from '../../core/auth/Provider'
import { failValidate } from '../datasource/store'
import { FailValidateAction } from '../datasource/Actions'
import { dataSourceByIdSelector } from '../datasource/selectors'
import { DATASOURCE_PREFIX } from '../api/constants'
import { logger } from '../../core/utils/logger'

import { makeIsRootChildByIdSelector, pagesSelector } from './selectors'
import {
    metadataFail,
    metadataSuccess,
    metadataRequest,
    resetPage,
    setPageScrolling,
} from './store'
import { flowDefaultModels } from './sagas/defaultModels'
import { MetadataRequest, Reset } from './Actions'
import { Metadata, Page } from './Pages'

function* setDefaultModels(models: DefaultModels, pageId: string) {
    yield race([
        call(flowDefaultModels, models),
        // @ts-ignore проблемы с типизацией saga
        take(({ type, payload }: Reset) => (type === resetPage.type && payload === pageId)),
    ])
}

/**
 * сага, фетчит метадату
 * @param apiProvider
 * @param action
 */
export function* getMetadata(
    apiProvider: unknown,
    authProvider: AuthProvider,
    action: MetadataRequest,
) {
    const { pageId, rootPage, pageUrl, mapping } = action.payload
    let url: string = pageUrl

    try {
        const { search, pathname } = yield select(getLocation)

        let resolveProvider: { url: string, headersParams: object } = { url: '', headersParams: {} }

        if (!isEmpty(mapping)) {
            const state: State = yield select()
            const extraQueryParams = rootPage && queryString.parse(search)

            // @ts-ignore import from js file
            resolveProvider = dataProviderResolver(state, { url, ...mapping }, extraQueryParams)

            url = resolveProvider.url
        } else if (rootPage) {
            url += search
        }

        const rawMetadata: Metadata = yield call(
            // @ts-ignore import from js file
            fetchSaga,
            FETCH_PAGE_METADATA,
            { pageUrl: url, headers: resolveProvider.headersParams },
            apiProvider,
        )
        const user: object = yield select(userSelector)
        const metadata = (yield resolveMetadata(rawMetadata, user, [
            'action', 'models', 'routes', 'datasources',
        ], authProvider)) as Metadata

        if (rootPage) {
            if (pageId !== metadata.id) {
                yield put(resetPage(pageId))
            }

            yield put(changeRootPage(metadata.id))
            yield put(destroyOverlay())
        }

        const rootChild: boolean = rootPage || (yield select(makeIsRootChildByIdSelector(metadata.id as string)))

        yield put(metadataSuccess(metadata.id, metadata, pageUrl, rootPage, rootChild))

        const { queryMapping } = metadata?.routes || {}

        // actions for the n2o/api from the page query mapping to the data source.
        if (queryMapping) {
            for (const key of Object.keys(queryMapping)) {
                const { get: action } = queryMapping[key]

                if (action) {
                    const type = get(action, 'type', '')

                    if (type.includes(DATASOURCE_PREFIX)) {
                        yield put({ ...action })
                    }
                }
            }
        }

        // @ts-ignore проблемы с типизацией saga
        yield fork(setDefaultModels, metadata.models, metadata.id)
    } catch (error: unknown) {
        const err = error as {
            message: string,
            stack: string,
            json?: { meta: Record<string, unknown> },
            status: number | null
        }

        if (rootPage) {
            yield put(changeRootPage(pageId))
        }

        yield put(
            metadataFail(
                pageId,
                {
                    title: err.status ? err.status : 'Ошибка',
                    status: err.status,
                    text: err.message,
                    closeButton: false,
                    severity: 'danger',
                },
                err?.json?.meta || {},
            ),
        )

        logger.error(err?.json?.meta || `Ошибка при получении метеданных: ${err.message}`)
    }
}

/**
 * Сага наблюдения за изменением моделей для отстреливания событий
 * Повторяет логику наблюдения зависимостей из датасурсов
 * FIXME вынести на общий механизм
 * @param action
 */
let prevModels: DefaultModels

export function* watchEvents() {
    const models: DefaultModels = yield select(modelsSelector)
    const pagesMap: Page[] = yield select(pagesSelector)
    const pagesList: Page[] = Object.values(pagesMap)

    for (const { metadata } of pagesList) {
        const { events } = metadata

        if (!events || !events.length) {
            // eslint-disable-next-line no-continue
            continue
        }

        for (const { datasource, model: prefix, field, action } of events) {
            const modelLink = [prefix, datasource]
            const model = get(models, modelLink, null)
            const prevModel = get(prevModels, modelLink, null)

            let value = model
            let prevValue = prevModel

            if (field) {
                value = get(value, field)
                prevValue = get(prevValue, field)
            }

            if (!isEqual(value, prevValue)) {
                // FIXME костыльный проброс контекста
                yield put(mergeMeta(action, { evalContext: DEFAULT_CONTEXT }))
            }
        }
    }

    prevModels = models
}

function* pageScrolling(action: FailValidateAction) {
    const { payload, meta } = action
    const isTriggeredByFieldChange = get(meta, 'isTriggeredByFieldChange', false)

    if (isTriggeredByFieldChange) { yield cancel() }

    const { id } = payload
    const { pageId } = yield select(dataSourceByIdSelector(id))

    // чтобы FailValidateAction отработал раньше и кинул error на fields
    yield delay(10)
    yield put(setPageScrolling(pageId, true))
}

/**
 * Сайд-эффекты для page редюсера
 * @ignore
 */
export default (apiProvider: unknown, security: { provider: AuthProvider }) => [
    // @ts-ignore fixme: разобрать почему takeEvery не разрулил автоматом тип metadataRequest
    takeEvery(metadataRequest, getMetadata, apiProvider, security.provider),
    takeEvery(failValidate, pageScrolling),
    debounce(100, [
        setModel,
        removeModel,
        removeAllModel,
        clearModel,
        updateModel,
    ], watchEvents),
]
