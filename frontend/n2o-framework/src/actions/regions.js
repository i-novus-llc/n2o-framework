import {
    REGISTER_REGION,
    SET_ACTIVE_REGION_ENTITY,
    MAP_URL,
} from '../constants/regions'

import createActionHelper from './createActionHelper'

export const registerRegion = (regionId, initProps) => createActionHelper(REGISTER_REGION)({ regionId, initProps })

export const setActiveEntity = (regionId, activeEntity) => (
    createActionHelper(SET_ACTIVE_REGION_ENTITY)({ regionId, activeEntity })
)

export const mapUrl = value => createActionHelper(MAP_URL)(value)
