import {
    call,
    put,
    select,
} from 'redux-saga/effects'
import get from 'lodash/get'
import { isEqual } from 'lodash'

import { dataProviderResolver } from '../../../core/dataProviderResolver'
import { setModel } from '../../models/store'
import { generateErrorMeta } from '../../../utils/generateErrorMeta'
import { id as generateId } from '../../../utils/id'
import { MODEL_PREFIX } from '../../../core/datasource/const'
import { dataSourceByIdSelector } from '../selectors'
import {
    changeCount,
    changePage,
    rejectRequest,
    resolveRequest,
} from '../store'
import { makeGetModelByPrefixSelector } from '../../models/selectors'
import { getLocation, rootPageSelector } from '../../global/store'
import { makePageRoutesByIdSelector } from '../../pages/selectors'

import { routesQueryMapping } from './routesQueryMapping'
import { fetch } from './fetch'

export function* dataRequest({ payload }) {
    const { datasource: id, options } = payload

    try {
        const state = yield select()
        const { provider, size, sorting, page, components, pageId } = yield select(dataSourceByIdSelector(id))

        if (!components.length) {
            return
        }

        if (!provider?.url) {
            yield put(rejectRequest(id))

            return
        }

        const currentPageId = pageId || (yield select(rootPageSelector))
        const routes = yield select(makePageRoutesByIdSelector(currentPageId))

        if (routes?.queryMapping) {
            const location = yield select(getLocation)

            yield* routesQueryMapping(state, routes, location)
        }

        const query = {
            page: get(options, 'page', page),
            size,
            sorting,
        }
        const resolvedProvider = yield call(
            dataProviderResolver,
            state,
            provider,
            query,
            options,
        )

        const response = yield fetch(id, resolvedProvider)

        const aciveModel = yield select(makeGetModelByPrefixSelector(MODEL_PREFIX.active, id))

        // Если есть активная модель и её нету в новом списке - убираем активную модель
        if (
            !aciveModel ||
            (aciveModel && !response.list?.some(model => isEqual(model, aciveModel)))
        ) {
            yield put(setModel(MODEL_PREFIX.active, id, response.list[0]))
        }

        yield put(changeCount(id, response.count))
        yield put(setModel(MODEL_PREFIX.source, id, response.list))

        if (response.page && response.page !== query.page) {
            yield put(changePage(id, response.page))
        }
        yield put(resolveRequest(id, response))
    } catch (err) {
        yield put(setModel(MODEL_PREFIX.source, id, []))
        yield put(setModel(MODEL_PREFIX.active, id, null))
        // eslint-disable-next-line no-console
        console.warn(`JS Error: DataSource(${id}) fetch saga. ${err.message}`)
        yield put(
            rejectRequest(
                id,
                err,
                err.json?.meta ||
                {
                    meta: generateErrorMeta({
                        id: generateId(),
                        text: 'Произошла внутренняя ошибка',
                        stacktrace: err.stack,
                        closeButton: true,
                    }),
                },
            ),
        )
    }
}
