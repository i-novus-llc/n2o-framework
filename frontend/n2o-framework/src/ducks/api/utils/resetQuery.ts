import { select } from 'redux-saga/effects'

import type { Routes } from '../../pages/sagas/types'
import { makePageRoutesByIdSelector } from '../../pages/selectors'
import { mapQueryToUrl } from '../../pages/sagas/restoreFilters'

export function* resetQuery(pageId: string) {
    const routes: Routes = yield select(makePageRoutesByIdSelector(pageId))

    if (routes) {
        const resetQuery: Record<string, undefined> = {}

        for (const key of Object.keys(routes.queryMapping)) {
            resetQuery[key] = undefined
        }

        yield mapQueryToUrl(pageId, resetQuery)
    }
}
