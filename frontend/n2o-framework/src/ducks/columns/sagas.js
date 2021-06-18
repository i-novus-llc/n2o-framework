import { put, select } from 'redux-saga/effects'

// eslint-disable-next-line import/no-cycle
import { resolveConditions } from '../../sagas/conditions'

import { changeColumnVisibility } from './store'

/**
 * Resolve columns conditions
 * @param column
 * @return
 */
export function* resolveColumn(column) {
    const state = yield select()

    if (column.conditions) {
        const { visible } = column.conditions

        if (visible) {
            const nextVisible = resolveConditions(visible, state).resolve

            yield put(
                changeColumnVisibility(column.key, column.columnId, nextVisible),
            )
        }
    }
}
