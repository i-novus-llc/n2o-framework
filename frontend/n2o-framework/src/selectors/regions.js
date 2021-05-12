import { createSelector } from 'reselect'

export const regionsSelector = state => state.regions || {}

export const makeRegionByIdSelector = regionId => createSelector(
    regionsSelector,
    regionState => regionState[regionId] || {},
)

export const makeRegionIsInitSelector = regionId => createSelector(
    makeRegionByIdSelector(regionId),
    regionState => regionState.isInit,
)

export const makeRegionActiveEntitySelector = regionId => createSelector(
    makeRegionByIdSelector(regionId),
    regionState => regionState.activeEntity,
)
