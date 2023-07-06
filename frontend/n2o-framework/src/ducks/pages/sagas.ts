import {
    call,
    put,
    select,
    takeEvery,
    take,
    debounce,
    race,
    fork,
} from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import { getLocation } from 'connected-react-router'
import queryString from 'query-string'
import { get, isEqual, cloneDeep } from 'lodash'

import { destroyOverlay } from '../overlays/store'
// @ts-ignore ignore import error from js file
import { FETCH_PAGE_METADATA } from '../../core/api'
// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { setGlobalLoading, changeRootPage } from '../global/store'
// @ts-ignore ignore import error from js file
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

import { pagesSelector } from './selectors'
import {
    metadataFail,
    metadataSuccess,
    setStatus,
    metadataRequest,
    resetPage,
} from './store'
import { flowDefaultModels } from './sagas/defaultModels'
import { MetadataRequest, Reset } from './Actions'
import { IMetadata, IPage } from './Pages'

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
export function* getMetadata(apiProvider: unknown, action: MetadataRequest) {
    const { pageId, rootPage, pageUrl, mapping } = action.payload
    let url: string = pageUrl

    try {
        yield put(setGlobalLoading(true))

        const { search } = yield select(getLocation)
        /* FIXME */
        let resolveProvider: { url: string, headersParams: object } = { url: '', headersParams: {} }

        if (!isEmpty(mapping)) {
            const state: State = yield select()
            const extraQueryParams = rootPage && queryString.parse(search)

            resolveProvider = dataProviderResolver(
                state,
                { url, ...mapping },
                extraQueryParams,
            )

            url = resolveProvider.url
        } else if (rootPage) {
            url += search
        }

        const metadata: IMetadata = yield call(
            fetchSaga,
            FETCH_PAGE_METADATA,
            { pageUrl: url, headers: resolveProvider.headersParams },
            apiProvider,
        )

        if (rootPage) {
            yield put(changeRootPage(metadata.id))
            yield put(destroyOverlay())
        }

        if (metadata.id) {
            yield put(setStatus(metadata.id, 200))
            yield put(metadataSuccess(metadata.id, metadata))
        }

        // @ts-ignore проблемы с типизацией saga
        yield fork(setDefaultModels, metadata.models, metadata.id)
    } catch (error: unknown) {
        const err = error as {
            message: string,
            stack: string,
            json?: { meta: Record<string, unknown>},
            status: number | null
        }

        if (err && err.status) {
            yield put(setStatus(pageId, err.status))
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
                err.json && err.json.meta ? err.json.meta : {},
            ),
        )
    } finally {
        yield put(setGlobalLoading(false))
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
    const pagesMap: IPage[] = yield select(pagesSelector)
    const pagesList: IPage[] = Object.values(pagesMap)

    for (const { metadata } of pagesList) {
        const { events } = metadata

        if (!events || !events.length) {
            // eslint-disable-next-line no-continue
            continue
        }

        for (const { datasource, model: prefix, field, action } of events) {
            const modelLink = [prefix, datasource]
            const model = get(models, modelLink)
            const prevModel = get(prevModels, modelLink)

            let value = model
            let prevValue = prevModel

            if (field) {
                value = get(value, field)
                prevValue = get(prevValue, field)
            }

            if (!isEqual(value, prevValue)) {
                yield put(cloneDeep(action))
            }
        }
    }

    prevModels = models
}

/**
 * Сайд-эффекты для page редюсера
 * @ignore
 */
export default (apiProvider: unknown) => [
    takeEvery(metadataRequest, getMetadata, apiProvider),
    debounce(100, [
        setModel,
        removeModel,
        removeAllModel,
        clearModel,
        updateModel,
    ], watchEvents),
]
