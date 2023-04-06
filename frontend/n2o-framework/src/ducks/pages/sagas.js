import {
    call,
    fork,
    race,
    take,
    put,
    select,
    takeEvery,
} from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import { getLocation } from 'connected-react-router'
import queryString from 'query-string'

import { destroyOverlay } from '../overlays/store'
import { FETCH_PAGE_METADATA } from '../../core/api'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { changeRootPage, rootPageSelector } from '../global/store'
import fetchSaga from '../../sagas/fetch'

import { mappingUrlToRedux } from './sagas/mapUrlToRedux'
import { makePageRoutesByIdSelector } from './selectors'
import {
    metadataFail,
    metadataSuccess,
    setStatus,
    metadataRequest,
    resetPage,
} from './store'
import { flowDefaultModels } from './sagas/defaultModels'

export function* processUrl() {
    try {
        const pageId = yield select(rootPageSelector)
        const routes = yield select(makePageRoutesByIdSelector(pageId))

        yield call(mappingUrlToRedux, routes)
    } catch (err) {
        // eslint-disable-next-line no-console
        console.warn(err)
    }
}

function* setDefaultModels(models) {
    yield processUrl()
    yield race([
        call(flowDefaultModels, models),
        take(resetPage.type),
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

        yield fork(setDefaultModels, metadata.models)
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
                    text: err.message || err.status,
                    closeButton: false,
                    severity: 'danger',
                },
                err.json && err.json.meta ? err.json.meta : {},
            ),
        )
    }
}

/**
 * Сайд-эффекты для page редюсера
 * @ignore
 */
export default apiProvider => [
    takeEvery(metadataRequest, getMetadata, apiProvider),
]
