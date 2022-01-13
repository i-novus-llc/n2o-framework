import {
    START_INVOKE,
    SUCCESS_INVOKE,
    FAIL_INVOKE,
} from '../constants/actionImpls'

import createActionHelper from './createActionHelper'

/**
 * @deprecated
 */

/**
 * Экшен начала инвока
 * @param widgetId
 * @param dataProvider
 * @param data
 * @param modelLink
 * @param meta
 * @param needResolve
 */
export function startInvoke(
    widgetId,
    dataProvider,
    data,
    modelLink,
    meta = { refresh: true },
    needResolve = true,
) {
    return createActionHelper(START_INVOKE)(
        {
            widgetId,
            dataProvider,
            data,
            modelLink,
            needResolve,
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
