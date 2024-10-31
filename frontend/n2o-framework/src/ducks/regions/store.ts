// TODO: дописать @return в jsDoc для функций prepare()
import { createSlice, createSelector, current } from '@reduxjs/toolkit'

// @ts-ignore ignore import error from js file
import createActionHelper from '../../actions/createActionHelper'
import { State as StoreState } from '../State'

import RegionResolver from './RegionResolver'
import { State } from './Regions'
import {
    RegisterRegion,
    SetActiveRegionEntity,
    SetTabInvalid,
    UnregisterRegion,
    SetRegionServiceInfo,
    SetRegionVisibility,
} from './Actions'

/**
 * Начальный стейт
 */
const defaultState = {
    regionId: null,
    activeEntity: null,
    isInit: false,
    panels: [],
    datasource: null,
    tabs: [],
    serviceInfo: {},
    visible: true,
}

export const initialState: State = {}

export const regionsSlice = createSlice({
    name: 'n2o/regions',
    initialState,
    reducers: {
        REGISTER_REGION: {
            prepare(regionId, regionState) {
                return ({
                    payload: { regionId, regionState },
                })
            },

            reducer(state, action: RegisterRegion) {
                const { regionId, regionState } = action.payload

                state[regionId] = regionState
            },
        },
        UNREGISTER_REGION: {
            prepare(regionId) {
                return ({
                    payload: { regionId },
                })
            },

            reducer(state, action: UnregisterRegion) {
                const { regionId } = action.payload

                delete state[regionId]
            },
        },

        SET_ACTIVE_REGION_ENTITY: {
            prepare(regionId, activeEntity) {
                return ({
                    payload: { regionId, activeEntity },
                })
            },

            reducer(state, action: SetActiveRegionEntity) {
                const { regionId, activeEntity } = action.payload

                if (state[regionId]) {
                    state[regionId].activeEntity = RegionResolver.transformedEntity(activeEntity)
                }
            },
        },

        SET_REGION_SERVICE_INFO: {
            prepare(regionId, serviceInfo) {
                return ({
                    payload: { regionId, serviceInfo },
                })
            },

            reducer(state, action: SetRegionServiceInfo) {
                const { regionId, serviceInfo } = action.payload

                if (!state[regionId]) {
                    state[regionId] = defaultState
                }

                state[regionId].serviceInfo = serviceInfo
            },
        },

        SET_TAB_INVALID: {
            prepare(regionId, tabId, invalid) {
                return ({
                    payload: { regionId, tabId, invalid },
                })
            },

            reducer(state, action: SetTabInvalid) {
                const { regionId, tabId, invalid } = action.payload

                const { tabs } = current(state)[regionId]
                const tabIndex = tabs.findIndex(tab => tab.id === tabId)

                if (tabIndex !== -1) {
                    if (invalid) {
                        state[regionId].tabs[tabIndex].invalid = invalid
                    } else {
                        delete state[regionId].tabs[tabIndex].invalid
                    }
                }
            },
        },

        setRegionVisibility: {
            prepare(regionId, visible) {
                return ({
                    payload: { regionId, visible },
                })
            },

            reducer(state, action: SetRegionVisibility) {
                const { regionId, visible } = action.payload

                state[regionId].visible = visible
            },
        },
    },
})

export default regionsSlice.reducer

export const {
    REGISTER_REGION: registerRegion,
    UNREGISTER_REGION: unregisterRegion,
    SET_ACTIVE_REGION_ENTITY: setActiveRegion,
    SET_TAB_INVALID: setTabInvalid,
    SET_REGION_SERVICE_INFO: setRegionServiceInfo,
    setRegionVisibility,
} = regionsSlice.actions

export const MAP_URL = 'n2o/regions/MAP_URL'
export const mapUrl = (value: string) => createActionHelper(MAP_URL)(value)

/**
 * Селектор всех сторов с регионами
 */
export const regionsSelector = (store: StoreState) => store.regions || {}

/**
 * Селектор региона по regionId
 */
export const makeRegionByIdSelector = (regionId: string) => createSelector(
    regionsSelector,
    regionState => regionState[regionId] || {},
)

/**
 * Селектор проверки активности региона по regionId
 */
export const makeRegionIsInitSelector = (regionId: string) => createSelector(
    makeRegionByIdSelector(regionId),
    regionState => regionState.isInit,
)

/**
 * Селектор получения активной сущности в регионе по regionId
 */
export const makeRegionActiveEntitySelector = (regionId: string) => createSelector(
    makeRegionByIdSelector(regionId),
    regionState => regionState.activeEntity,
)

/**
 * Селектор получения tabs по regionId
 */

export const makeRegionTabsSelector = (regionId: string) => createSelector(
    makeRegionByIdSelector(regionId),
    regionState => regionState.tabs,
)
