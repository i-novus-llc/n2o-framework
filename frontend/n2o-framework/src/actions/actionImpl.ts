import { createAction } from '@reduxjs/toolkit'

import { ModelPrefix } from '../core/datasource/const'
import {
    START_INVOKE,
    SUCCESS_INVOKE,
    FAIL_INVOKE,
} from '../constants/actionImpls'

import createActionHelper from './createActionHelper'

/**
 * Экшен начала инвока
 * @param datasource
 * @param dataProvider
 * @param model
 * @param pageId
 * @param meta
 */
export function startInvoke(
    datasource: string,
    dataProvider: Record<string, unknown>,
    model: Record<string, unknown>,
    pageId: string,
    meta = {},
) {
    return createActionHelper(START_INVOKE)(
        {
            datasource,
            dataProvider,
            model,
            pageId,
        },
        meta,
    )
}

/**
 * Экшен удачного завершения инвока
 * @param datasource
 * @param meta
 */
export const successInvoke = createAction(SUCCESS_INVOKE, (
    datasource: string | undefined,
    model: ModelPrefix,
    meta: Record<string, unknown>,
) => {
    return {
        payload: { datasource, model },
        meta,
    }
})

/**
 * Экшен неудачного завершения инвока
 * @param datasource
 * @param meta
 */
export function failInvoke(
    datasource: string | undefined,
    meta: Record<string, unknown>,
) {
    return createActionHelper(FAIL_INVOKE)({ datasource }, meta)
}
