import { put, select } from 'redux-saga/effects'

// @ts-ignore ignore import error from js file
// eslint-disable-next-line import/no-cycle
import { resolveConditions } from '../../sagas/conditions'
import { State } from '../State'

import { changeColumnVisibility } from './store'
import { Column } from './Columns'

/**
 * Resolve columns conditions
 * @param column
 * @return
 */
export function* resolveColumn(column: Column) {
    const state: State = yield select()

    if (!column.conditions) {
        return
    }

    const { visible } = column.conditions

    if (!visible) {
        return
    }

    const nextVisible = resolveConditions(state, visible).resolve

    yield put(
        changeColumnVisibility(column.key, column.columnId, nextVisible),
    )
}
