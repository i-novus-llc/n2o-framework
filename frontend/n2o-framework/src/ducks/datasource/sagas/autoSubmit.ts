import { PayloadAction } from '@reduxjs/toolkit'
import { put, select } from 'redux-saga/effects'
import { isEmpty } from 'lodash'

import { FieldPath } from '../../models/Actions'
import { DataSourceState } from '../DataSource'
import { dataSourceByIdSelector } from '../selectors'
import { submit } from '../store'

export function* autoSubmit({ payload }: PayloadAction<FieldPath>) {
    const { key, field } = payload
    const datasource: DataSourceState = yield select(dataSourceByIdSelector(key))

    if (isEmpty(datasource)) { return }

    // @ts-ignore FIXME ругается на ".auto"/".autoSubmitOn", пофиксить типы
    const provider = (datasource.submit?.auto || datasource.submit?.autoSubmitOn)
        ? datasource.submit
        : datasource.fieldsSubmit[field]

    if (!isEmpty(provider)) {
        yield put(submit(key, provider))
    }
}
