import { call, put, select, takeEvery } from 'redux-saga/effects'
import values from 'lodash/values'
import filter from 'lodash/filter'
import get from 'lodash/get'
import reduce from 'lodash/reduce'
import first from 'lodash/first'
import some from 'lodash/some'
import every from 'lodash/every'
import each from 'lodash/each'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import includes from 'lodash/includes'

import { metadataSuccess as METADATA_SUCCESS } from '../pages/store'
import { makePageRoutesByIdSelector } from '../pages/selectors'
import { getLocation, rootPageSelector } from '../global/store'
import { modelsSelector } from '../models/selectors'
import { authSelector } from '../user/selectors'
// eslint-disable-next-line import/no-cycle
import { routesQueryMapping } from '../widgets/sagas'
import { makeWidgetVisibleSelector } from '../widgets/selectors'
import { dataRequestWidget } from '../widgets/store'

import { setActiveRegion, regionsSelector } from './store'
import { MAP_URL } from './constants'

function* mapUrl(value) {
    const state = yield select()
    const location = yield select(getLocation)
    const rootPageId = yield select(rootPageSelector)
    const routes = yield select(makePageRoutesByIdSelector(rootPageId))

    if (routes) {
        yield call(routesQueryMapping, state, routes, location)
        yield call(lazyFetch, value.payload)
    }
}

function* switchTab() {
    const state = yield select()

    const auth = authSelector(state)
    const userPermissions = get(auth, 'permissions')
    const userRoles = get(auth, 'roles')

    const regions = regionsSelector(state)

    const tabsRegions = filter(values(regions), region => region.tabs)

    const hasActiveEntity = some(tabsRegions, region => region.activeEntity)

    const atLeastOneVisibleWidget = content => some(content, (meta) => {
        if (meta.visible === false) {
            return false
        } if (meta.content) {
            return atLeastOneVisibleWidget(meta.content)
        }

        return makeWidgetVisibleSelector(meta.id)
    })

    const visibleEntities = reduce(
        tabsRegions,
        (acc, region) => {
            const { tabs, regionId } = region
            const widgetsIds = []

            each(tabs, (tab) => {
                const content = get(tab, 'content')
                let isPassedSecurity = true

                if (tab.security) {
                    const securityRules = get(tab, 'security.object')

                    const isPassed = (tabSecurityRule, userProperties) => {
                        if (tabSecurityRule) {
                            return every(tabSecurityRule, rule => userProperties.includes(rule))
                        }

                        return true
                    }

                    const tabSecurityPermissions = get(securityRules, 'permissions')
                    const tabSecurityRoles = get(securityRules, 'roles')

                    isPassedSecurity =
                        isPassed(tabSecurityPermissions, userPermissions) &&
                        isPassed(tabSecurityRoles, userRoles)
                }
                if (isPassedSecurity && atLeastOneVisibleWidget(content)) {
                    widgetsIds.push(tab.id)
                }
            })

            acc.push({ [regionId]: widgetsIds })

            return acc
        },
        [],
    )

    for (let index = 0; index <= visibleEntities.length - 1; index += 1) {
        const visibleEntity = visibleEntities[index]

        if (!hasActiveEntity) {
            for (let index = 0; index <= tabsRegions.length - 1; index += 1) {
                const { regionId } = tabsRegions[index]
                const activeEntity = first(visibleEntity[regionId])

                yield put(setActiveRegion(regionId, activeEntity))
            }
        }
    }
    mapUrl()
}

function* lazyFetch(id) {
    const regions = yield select(regionsSelector)
    const models = yield select(modelsSelector)
    const regionCollection = values(regions)
    let targetTab = {}
    const idsToFetch = []

    if (!isEmpty(regionCollection)) {
        each(regionCollection, (region) => {
            const { activeEntity, alwaysRefresh } = region

            targetTab = { ...find(region.tabs, tab => tab.id === activeEntity) }

            if (!isEmpty(targetTab.content)) {
                each(targetTab.content, (item) => {
                    if (
                        (alwaysRefresh && targetTab.id === id) ||
                        (!includes(Object.keys(models.datasource), item.id))
                    ) {
                        idsToFetch.push(item.id)
                    }
                })
            }
        })

        for (let i = 0; i < idsToFetch.length; i++) {
            yield put(dataRequestWidget(idsToFetch[i]))
        }

        idsToFetch.length = 0
    }
}

export function* checkIdBeforeLazyFetch() {
    const regions = yield select(regionsSelector)
    const regionsCollection = values(regions)
    const activeWidgetIds = []
    let tabsWidgetIds = {}
    const firstTabs = []

    const mapRegions = (tabs, activeEntity, lazy, regionId) => {
        if (tabs) {
            each(tabs, (tab, index) => {
                if (!isEmpty(tab.content)) {
                    each(tab.content, (contentItem) => {
                        if (tab.id === activeEntity) {
                            activeWidgetIds.push(contentItem.id)
                        } else if (!activeEntity && index === 0) {
                            // если lazy=true и нет активных табов, первый таб добавляется в массив
                            firstTabs.push({ regionId, id: tab.id })
                            activeWidgetIds.push(contentItem.id)
                        }

                        tabsWidgetIds = { ...tabsWidgetIds, [contentItem.id]: lazy }

                        if (!isEmpty(contentItem.tabs)) {
                            const { activeEntity, lazy, tabs } = contentItem

                            mapRegions(tabs, activeEntity, lazy)
                        }
                    })
                }
            })
        }
    }

    if (!isEmpty(regionsCollection)) {
        each(regionsCollection, (region) => {
            const { activeEntity, lazy, tabs, regionId } = region

            mapRegions(tabs, activeEntity, lazy, regionId)
        })
    }

    if (firstTabs) {
        for (let i = 0; i < firstTabs.length; i++) {
            const { regionId, id } = firstTabs[i]

            yield put(setActiveRegion(regionId, id))
        }
    }

    return {
        activeWidgetIds,
        tabsWidgetIds,
    }
}

export default [
    takeEvery(MAP_URL, mapUrl),
    takeEvery(METADATA_SUCCESS, switchTab),
]
