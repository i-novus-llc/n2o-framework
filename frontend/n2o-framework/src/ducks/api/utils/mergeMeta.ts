/* eslint-disable indent */
import { Action, Meta } from '../../Action'

/*
 * eslint тут чего-то сходит с ума на отступах
 * FIXME разобраться
 */
export const mergeMeta = <
    TMeta extends Meta,
    TAdditionalMeta extends TMeta,
    TType extends string = string,
    TPayload = unknown
>(
    action: Action<TType, TPayload, TMeta | void>,
    meta: TAdditionalMeta,
): Action<TType, TPayload, TMeta> => ({
    ...action,
    meta: {
        ...(action.meta || {}),
        ...meta,
    },
})
/* eslint-enable indent */
