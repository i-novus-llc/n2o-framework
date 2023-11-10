import { put, takeEvery, cancel } from 'redux-saga/effects'
import omit from 'lodash/omit'

import { registerWidget } from '../widgets/store'
import { Register } from '../widgets/Actions'

import { registerTable } from './store'

function* registerTableEffect(action: Register) {
    const { payload } = action
    const { widgetId, initProps } = payload
    const { table } = initProps

    if (!table) {
        yield cancel()
    }

    const tableInitProps = { ...omit(initProps, 'table'), ...table }

    yield put(registerTable(widgetId, tableInitProps))
}

export const sagas = [
    takeEvery(registerWidget, registerTableEffect),
]
