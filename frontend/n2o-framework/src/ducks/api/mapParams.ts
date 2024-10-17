import isEmpty from 'lodash/isEmpty'
import isNaN from 'lodash/isNaN'
import set from 'lodash/set'
import { put, takeEvery } from 'redux-saga/effects'
import { createAction } from '@reduxjs/toolkit'

import { MapParamAction, MapParamPayload } from '../datasource/Actions'
import { updatePaging } from '../datasource/store'
import { DataSourceState } from '../datasource/DataSource'

import { DATASOURCE_PREFIX } from './constants'
import { EffectWrapper } from './utils/effectWrapper'

function getValue(key: string, hash: string) {
    const cleanKey = key.slice(1)

    const regex = new RegExp(`[?&]${cleanKey}=([^&]*)`)
    const match = hash.match(regex)

    return match ? decodeURIComponent(match[1]) : null
}

export const creator = createAction(
    `${DATASOURCE_PREFIX}mapParam`,
    (payload: MapParamPayload, meta: object) => ({
        payload,
        meta,
    }),
)

export function* mapParams({ payload }: MapParamAction) {
    const { id, ...params } = payload

    const { hash } = window.location
    const ds: Partial<DataSourceState> = {}

    for (const [key, param] of Object.entries(params)) {
        const value = getValue(param, hash)

        if (value !== null) {
            set(ds, key, isNaN(Number(value)) ? value : Number(value))
        }
    }
    if (!isEmpty(ds.paging)) {
        // @ts-ignore FIXME: Тут вывод типов думает что тут редюсер, а не prepare функция, и ждёт сразу action
        yield put(updatePaging(id, ds.paging))
    }
}

export const sagas = [takeEvery(creator.type, EffectWrapper(mapParams))]
