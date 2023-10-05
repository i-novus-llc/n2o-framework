import { Action } from '../Action'

import { Region } from './Regions'

export type RegisterRegionPayload = {
    regionId: string
    regionState: Region
}

export type SetActiveRegionEntityPayload = {
    regionId: string
    activeEntity: string
}

export type SetTabInvalidPayload = {
    regionId: string
    tabId: string
    invalid: boolean
}

export interface InfoMeta {
    datasources: string[]
    widgets: string[]
}

export type ServiceInfo = Record<string, InfoMeta>

export type SetRegionServiceInfoPayload = {
    regionId: string
    serviceInfo: ServiceInfo
}

export type RegisterRegion = Action<string, RegisterRegionPayload>
export type UnregisterRegion = Action<string, { regionId: string }>
export type SetActiveRegionEntity = Action<string, SetActiveRegionEntityPayload>
export type SetTabInvalid = Action<string, SetTabInvalidPayload>
export type SetRegionServiceInfo = Action<string, SetRegionServiceInfoPayload>
