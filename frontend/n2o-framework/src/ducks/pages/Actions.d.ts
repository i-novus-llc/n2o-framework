import { Action } from '../Action'

import { Location, Metadata } from './Pages'

export type MetadataRequestPayload = {
    pageId: string
    rootPage: boolean
    pageUrl: string
    mapping: Record<string, unknown>
    parentId?: string
}

export type MetadataSuccessPayload = {
    pageId: string
    json: Metadata
    pageUrl: string
    rootPage: boolean
    rootChild: boolean
}

export type MetadataFailPayload = {
    pageId: string
    err: Record<string, unknown> | boolean
}

export type SetLocationPayload = {
    pageId: string
    location: string | Location
}

export type SetScrollPayload = {
    pageId: string
    scroll: boolean
}

export type MetadataRequest = Action<string, MetadataRequestPayload>
export type MetadataSuccess = Action<string, MetadataSuccessPayload>
export type MetadataFail = Action<string, MetadataFailPayload>
export type Reset = Action<string, string>
export type SetLocation = Action<string, SetLocationPayload>
export type SetScroll = Action<string, SetScrollPayload>
