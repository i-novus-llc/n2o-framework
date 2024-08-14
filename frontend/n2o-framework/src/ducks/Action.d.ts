import { Action as ReduxAction } from 'redux'

import { State } from './State'

interface N2OMeta {
    operationId?: string
    target?: string
    key?: string
    buttonId?: string
    evalContext?: Record<string, unknown>
    prevState?: State
    [key: string]: unknown
}

interface N2OAction<
    TType extends string = string,
    TPayload = unknown,
    TMeta extends N2OMeta = N2OMeta,
> extends ReduxAction<TType> {
    payload: TPayload,
    meta?: TMeta,
    error?: string
}

interface N2OErrorAction<
    TType extends string = string,
    TMeta extends N2OMeta = N2OMeta,
> extends N2OAction<TType, undefined, TMeta> {
    error: string
}

export type {
    N2OMeta as Meta,
    N2OAction as Action,
    N2OErrorAction as ErrorAction,
}
