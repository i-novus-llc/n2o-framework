import { Action } from '../Action'

import { IRegion } from './Regions'

export type RegisterRegionPayload = {
    regionId: string
    regionState: IRegion
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

export type RegisterRegion = Action<string, RegisterRegionPayload>
export type UnregisterRegion = Action<string, { regionId: string }>
export type SetActiveRegionEntity = Action<string, SetActiveRegionEntityPayload>
export type SetTabInvalid = Action<string, SetTabInvalidPayload>
