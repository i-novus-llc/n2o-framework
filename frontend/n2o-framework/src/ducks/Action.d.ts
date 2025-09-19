import { Action as ReduxAction } from 'redux'

import { State } from './State'

export interface N2OMeta {
    abortController?: AbortController
    buttonId?: string
    evalContext?: Record<string, unknown>
    operationId?: string
    initAction?: boolean
    isTriggeredByFieldChange?: boolean
    key?: string
    pageId?: string
    prevState?: State
    target?: string
    validate?: boolean
    [key: string]: unknown
}

export interface N2OAction<
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
