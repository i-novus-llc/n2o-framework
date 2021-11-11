import {
    call,
    put,
    select,
} from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'

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

import { fetch } from './fetch'

export function* dataRequest({ id, options }) {
    try {
        const state = yield select()
        const { provider, size, sorting, page } = yield select(dataSourceByIdSelector(id))

        if (!provider?.url) {
            yield put(rejectRequest(id))

            return
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

        yield put(changeCount(id, response.count))
        yield put(setModel(MODEL_PREFIX.source, id, response.list))
        if (isEmpty(response.list)) {
            yield put(setModel(MODEL_PREFIX.active, id, null))
        }
        if (response.page && response.page !== query.page) {
            yield put(changePage(id, response.page))
        }
        yield put(resolveRequest(id, response))
    } catch (err) {
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
