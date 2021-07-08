// eslint-disable-next-line spaced-comment
/// <reference path="./typedefs.js"
// TODO: дописать @return в jsDoc для функций prepare()
import { createSlice, createSelector } from '@reduxjs/toolkit'

import createActionHelper from '../../actions/createActionHelper'

import RegionResolver from './RegionResolver'
import { MAP_URL } from './constants'

/**
 * Начальный стейт
 * @type {RegionsStore.state}
 */
const initialState = {}
const regionsSlice = createSlice({
    name: 'n2o/regions',
    initialState,
    reducers: {
        REGISTER_REGION: {

            /**
             * @param {string} regionId
             * @param {RegionsStore.item} regionState
             * @return {{payload: RegionsStore.registerRegionPayload}}
             */
            prepare(regionId, regionState) {
                return ({
                    payload: { regionId, regionState },
                })
            },

            /**
             * Добавления региона
             * @param {RegionsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {RegionsStore.registerRegionPayload} action.payload
             */
            reducer(state, action) {
                const { regionId, regionState } = action.payload

                state[regionId] = regionState
            },
        },

        SET_ACTIVE_REGION_ENTITY: {

            /**
             * @param {string} regionId
             * @param {string} activeEntity
             * @return {{payload: RegionsStore.setActiveRegionEntityPayload}}
             */
            prepare(regionId, activeEntity) {
                return ({
                    payload: { regionId, activeEntity },
                })
            },

            /**
             * Переключение активного региона
             * @param {RegionsStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {RegionsStore.setActiveRegionEntityPayload} action.payload
             */
            reducer(state, action) {
                const { regionId, activeEntity } = action.payload

                if (!state[regionId]) {
                    state[regionId] = {}
                }

                state[regionId].activeEntity = RegionResolver.transformedEntity(activeEntity)
            },
        },
    },
})

export default regionsSlice.reducer

// Actions
export const {
    REGISTER_REGION: registerRegion,
    SET_ACTIVE_REGION_ENTITY: setActiveRegion,
} = regionsSlice.actions
export const mapUrl = value => createActionHelper(MAP_URL)(value)

// Selectors
/**
 * Селектор всех сторов с регионами
 * @param {Object} store
 * @return {RegionsStore.state}
 */
export const regionsSelector = store => store.regions || {}

/**
 * Селектор региона по regionId
 * @param {string} regionId
 * @return {Object.<string, RegionsStore.item>}
 */
export const makeRegionByIdSelector = regionId => createSelector(
    regionsSelector,
    regionState => regionState[regionId] || {},
)

/**
 * Селектор проверки активности региона по regionId
 * @param {string} regionId
 * @return {boolean}
 */
export const makeRegionIsInitSelector = regionId => createSelector(
    makeRegionByIdSelector(regionId),
    regionState => regionState.isInit,
)

/**
 * Селектор получения активной сущности в регионе по regionId
 * @param {string} regionId
 * @return {string | boolean}
 */
export const makeRegionActiveEntitySelector = regionId => createSelector(
    makeRegionByIdSelector(regionId),
    regionState => regionState.activeEntity,
)
