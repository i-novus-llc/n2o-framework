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
import { FETCH_PAGE_METADATA } from '../../core/api'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { setGlobalLoading, changeRootPage } from '../global/store'
import fetchSaga from '../../sagas/fetch'
import {
    clearModel,
    removeAllModel,
    removeModel,
    setModel,
    updateModel,
} from '../models/store'
import { modelsSelector } from '../models/selectors'

import { pagesSelector } from './selectors'
import {
    metadataFail,
    metadataSuccess,
    setStatus,
    metadataRequest,
    resetPage,
} from './store'
import { flowDefaultModels } from './sagas/defaultModels'

function* setDefaultModels(models, pageId) {
    yield race([
        call(flowDefaultModels, models),
        take(({ type, payload }) => (type === resetPage.type && payload === pageId)),
    ])
}

/**
 * сага, фетчит метадату
 * @param apiProvider
 * @param action
 */
export function* getMetadata(apiProvider, action) {
    const { pageId, rootPage, pageUrl, mapping } = action.payload
    let url = pageUrl

    try {
        yield put(setGlobalLoading(true))

        const { search } = yield select(getLocation)
        let resolveProvider = {}

        if (!isEmpty(mapping)) {
            const state = yield select()
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

        const metadata = yield call(
            fetchSaga,
            FETCH_PAGE_METADATA,
            { pageUrl: url, headers: resolveProvider.headersParams },
            apiProvider,
        )

        if (rootPage) {
            yield put(changeRootPage(metadata.id))
            yield put(destroyOverlay())
        }

        yield put(setStatus(metadata.id, 200))
        yield put(metadataSuccess(metadata.id, metadata))

        yield fork(setDefaultModels, metadata.models, metadata.id)
    } catch (err) {
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
let prevModels

export function* watchEvents() {
    const models = yield select(modelsSelector)
    const pagesMap = yield select(pagesSelector)
    const pagesList = Object.values(pagesMap)

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
export default apiProvider => [
    takeEvery(metadataRequest, getMetadata, apiProvider),
    debounce(100, [
        setModel,
        removeModel,
        removeAllModel,
        clearModel,
        updateModel,
    ], watchEvents),
]
