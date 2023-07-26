import { Action } from '../Action'

import { IMetadata } from './Pages'

export type MetadataRequestPayload = {
    pageId: string
    rootPage: string
    pageUrl: string
    mapping: Record<string, unknown>
}

export type MetadataSuccessPayload = {
    pageId: string
    json: IMetadata
}

export type MetadataFailPayload = {
    pageId: string
    err: Record<string, unknown> | boolean
}

export type SetStatusPayload = {
    pageId: string
    status: number | null
}

export type SetPageLoadingPayload = {
    pageId: string
    loading: boolean
    spinner: boolean
}

export type MetadataRequest = Action<string, MetadataRequestPayload>
export type MetadataSuccess = Action<string, MetadataSuccessPayload>
export type MetadataFail = Action<string, MetadataFailPayload>
export type SetStatus = Action<string, SetStatusPayload>
export type SetPageLoading = Action<string, SetPageLoadingPayload>
export type Reset = Action<string, string>
