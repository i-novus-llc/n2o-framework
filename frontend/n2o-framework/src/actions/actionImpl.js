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
 * @param needResolve
 */
export function startInvoke(
    datasource,
    dataProvider,
    model,
    pageId,
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
export function successInvoke(datasource, meta) {
    return createActionHelper(SUCCESS_INVOKE)(
        {
            datasource,
        },
        meta,
    )
}

/**
 * Экшен неудачного завершения инвока
 * @param datasource
 * @param meta
 */
export function failInvoke(datasource, meta) {
    return createActionHelper(FAIL_INVOKE)(
        {
            datasource,
        },
        meta,
    )
}
