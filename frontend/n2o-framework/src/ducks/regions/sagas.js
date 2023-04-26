import { call, put, select, takeEvery, cancel } from 'redux-saga/effects'
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

import { metadataSuccess as METADATA_SUCCESS } from '../pages/store'
import { makePageRoutesByIdSelector } from '../pages/selectors'
import { rootPageSelector } from '../global/store'
import { modelsSelector } from '../models/selectors'
import { authSelector } from '../user/selectors'
import { mapQueryToUrl } from '../pages/sagas/restoreFilters'
import {
    makeDatasourceIdSelector,
    makeWidgetIsInitSelector,
    makeWidgetFetchOnInit,
    makeWidgetFetchOnVisibility,
} from '../widgets/selectors'
import { registerWidget } from '../widgets/store'
import { failValidate, dataRequest, resetValidation } from '../datasource/store'
import { dataSourceErrors } from '../datasource/selectors'
import { evalExpression } from '../../utils/evalExpression'
import { setModel } from '../models/store'

import { setActiveRegion, regionsSelector, setTabInvalid, registerRegion } from './store'
import { MAP_URL } from './constants'
import { getTabsRegions, checkTabErrors, activeTabHasErrors, tabsIncludesId } from './utils'

function* mapUrl(value) {
    const rootPageId = yield select(rootPageSelector)
    const routes = yield select(makePageRoutesByIdSelector(rootPageId))

    if (routes) {
        yield call(mapQueryToUrl, rootPageId, null, true)
        yield call(lazyFetch, value.payload)
    }
}

function* switchTab(action) {
    const { payload } = action
    const { regionId } = payload

    const regions = yield select(regionsSelector)

    if (regionId && get(regions, `${regionId}.datasource`)) {
        /* no switch logic if a datasource is passed */
        yield cancel()
    }

    const state = yield select()
    const { widgets = {} } = state

    const tabsRegions = filter(values(regions), region => region.tabs)

    const auth = authSelector(state)
    const userPermissions = get(auth, 'permissions')
    const userRoles = get(auth, 'roles')

    const atLeastOneVisibleWidget = (content, widgets) => some(content, (meta) => {
        if (meta.visible === false) {
            return false
        }

        if (meta.content) {
            return atLeastOneVisibleWidget(meta.content, widgets)
        }

        const { id, dependency = {} } = meta
        const { visible } = dependency

        /* FIXME
           требуется рефакторинг,
           если завязаться на show/hide widget
           авто переключение конфликтует с регистрацией региона и resolve model
        */
        if (!isEmpty(visible)) {
            for (const { on, condition } of visible) {
                const model = get(state, on)

                if (!evalExpression(condition, model)) {
                    return false
                }
            }

            return true
        }

        return get(widgets, `${id}.visible`, true)
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
                if (isPassedSecurity && atLeastOneVisibleWidget(content, widgets)) {
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

        for (let index = 0; index <= tabsRegions.length - 1; index += 1) {
            const { regionId } = tabsRegions[index]
            const state = yield select()

            /* active from url */
            const active = get(state, `regions.${regionId}.activeEntity`)

            /* list of visible tabs of the region */
            const passedEntities = get(visibleEntity, regionId, [])

            if (passedEntities.includes(active)) {
                const currentRegion = get(state, `regions.${regionId}`)
                const { datasource, activeTabFieldId } = currentRegion

                yield mapUrl(active)

                if (datasource && activeTabFieldId) {
                    yield put(setModel('resolve', datasource, { [activeTabFieldId]: active }))
                }

                yield cancel()
            }

            const activeEntity = first(visibleEntity[regionId])

            if (regionId && activeEntity) {
                yield put(setActiveRegion(regionId, activeEntity))
                yield mapUrl(activeEntity)

                const currentRegion = get(state, `regions.${regionId}`)
                const { datasource, activeTabFieldId } = currentRegion

                if (datasource && activeTabFieldId) {
                    yield put(setModel('resolve', datasource, { [activeTabFieldId]: activeEntity }))
                }
            }
        }
    }
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
                        !Object.keys(models.datasource).includes(item.datasource || item.id)
                    ) {
                        idsToFetch.push(item.id)
                    }
                })
            }
        })
        for (const widgetId of idsToFetch) {
            const datasource = yield select(makeDatasourceIdSelector(widgetId))

            const isInit = yield select(makeWidgetIsInitSelector(widgetId))
            const fetchOnInit = yield select(makeWidgetFetchOnInit(widgetId))
            const fetchOnVisibility = yield select(makeWidgetFetchOnVisibility(widgetId))

            const needToFetch = isInit && (fetchOnInit || fetchOnVisibility)

            if (needToFetch) {
                yield put(dataRequest(datasource))
            }
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

function* switchToFirstInvalidTab(regionId, tabs) {
    const firstInvalid = find(tabs, tab => get(tab, 'invalid'))

    if (!firstInvalid) {
        yield cancel()
    }

    const { id } = firstInvalid

    yield put(setActiveRegion(regionId, id))
}

function* validateTabs({ payload, meta, type }) {
    const { id } = payload
    const { blurValidation = false } = meta

    const state = yield select()
    const tabsRegions = getTabsRegions(state)

    if (!tabsIncludesId(id, tabsRegions)) {
        yield cancel()
    }

    const errors = yield select(dataSourceErrors(id)) || {}
    const fieldsWithErrors = Object.keys(errors)

    for (const { regionId, tabs } of tabsRegions) {
        for (const { id: tabId, content } of tabs) {
            const invalid = checkTabErrors(content, fieldsWithErrors)

            yield put(setTabInvalid(regionId, tabId, invalid))
        }
    }

    if (type === failValidate.type && !blurValidation) {
        const newState = yield select()
        const tabsRegions = getTabsRegions(newState)

        for (const { regionId, activeEntity, tabs } of tabsRegions) {
            /* if there are no errors on the active tab, switching the tab to the first invalid */
            if (!activeTabHasErrors(activeEntity, tabs)) {
                yield switchToFirstInvalidTab(regionId, tabs)
            }
        }
    }
}

export default [
    takeEvery(MAP_URL, mapUrl),
    takeEvery([
        METADATA_SUCCESS,
        registerRegion,
        registerWidget,
        setActiveRegion,
    ], switchTab),
    takeEvery([failValidate, resetValidation], validateTabs),
]
