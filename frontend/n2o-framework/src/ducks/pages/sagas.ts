import {
    call,
    put,
    select,
    takeEvery,
    take,
    race,
    fork,
    cancel,
    delay,
} from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import { getLocation } from 'connected-react-router'
import queryString from 'query-string'
import get from 'lodash/get'

import { destroyOverlay } from '../overlays/store'
import { FETCH_PAGE_METADATA } from '../../core/api'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { changeRootPage } from '../global/store'
import fetchSaga from '../../sagas/fetch'
import { DefaultModels } from '../models/Models'
import { State } from '../State'
import { userSelector } from '../user/selectors'
import { resolveMetadata } from '../../core/auth/resolveMetadata'
import { AuthProvider } from '../../core/auth/Provider'
import { endValidation } from '../datasource/store'
import { ValidateEndAction } from '../datasource/Actions'
import { dataSourceByIdSelector } from '../datasource/selectors'
import { DATASOURCE_PREFIX } from '../api/constants'
import { logger } from '../../utils/logger'
import { subscribe } from '../models/sagas/subscribe'
import { ModelLink } from '../../core/models/types'
import { watchOnChangeEvents, getOnChangeEvents } from '../watchEvents/watchEvents'

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
 * Сага, получающая страницы, извлекающая из них события и передающая их в watchEvents для обработки.
 * @param keys - ключи моделей, по которым происходит отслеживание
 */
export function* watchPageEvents(keys: ModelLink[]) {
    const pagesMap: Page[] = yield select(pagesSelector)
    const pagesList: Page[] = Object.values(pagesMap)

    const events = pagesList.flatMap(page => page.metadata?.events || [])

    yield call(watchOnChangeEvents, getOnChangeEvents(events), keys)
}

function* pageScrolling(action: ValidateEndAction) {
    const { payload, meta } = action
    const isTriggeredByFieldChange = get(meta, 'isTriggeredByFieldChange', false)

    if (isTriggeredByFieldChange || isEmpty(payload.messages)) { yield cancel() }

    const { id } = payload
    const { pageId } = yield select(dataSourceByIdSelector(id))

    // чтобы FailValidateAction отработал раньше и кинул error на fields
    yield delay(10)
    yield put(setPageScrolling(pageId, true))
}

subscribe(watchPageEvents)

/**
 * Сайд-эффекты для page редюсера
 * @ignore
 */
export default (apiProvider: unknown, security: { provider: AuthProvider }) => [
    // @ts-ignore fixme: разобрать почему takeEvery не разрулил автоматом тип metadataRequest
    takeEvery(metadataRequest, getMetadata, apiProvider, security.provider),
    takeEvery(endValidation, pageScrolling),
]
