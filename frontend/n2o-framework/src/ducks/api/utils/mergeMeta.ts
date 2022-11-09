/* eslint-disable indent */
import { Action } from '../../Action'

/*
 * eslint тут чего-то сходит с ума на отступах
 * FIXME разобраться
 */
export const mergeMeta = <
    TMeta extends object,
    TAdditionalMeta extends object,
    TType extends string = string,
    TPayload = unknown
>(
    action: Action<TType, TPayload, TMeta>,
    meta: TAdditionalMeta,
): Action<TType, TPayload, TAdditionalMeta & TMeta> => ({
    ...action,
    meta: {
        ...(action.meta || {}),
        ...meta,
    },
})
/* eslint-enable indent */
