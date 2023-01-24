import { call, put, select, takeEvery, cancel } from 'redux-saga/effects'
import { actionTypes } from 'redux-form'
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
import { makeDatasourceIdSelector } from '../widgets/selectors'
import { registerWidget } from '../widgets/store'
import { failValidate, startValidate, dataRequest } from '../datasource/store'
import { dataSourceErrors } from '../datasource/selectors'

import { setActiveRegion, regionsSelector, setTabInvalid, registerRegion } from './store'
import { MAP_URL } from './constants'

function* mapUrl(value) {
    const rootPageId = yield select(rootPageSelector)
    const routes = yield select(makePageRoutesByIdSelector(rootPageId))

    if (routes) {
        yield call(mapQueryToUrl, rootPageId)
        yield call(lazyFetch, value.payload)
    }
}

export function* tabTraversal(action, tabs, regionId, form, param = null, options = {}) {
    let isTargetFormInTabs = false

    for (const { content, id: tabId } of tabs) {
        for (const { datasource, tabs: childrenTabs } of content) {
            if (childrenTabs) {
                return tabTraversal(action, childrenTabs, regionId, form, param)
            }

            if (datasource === form) {
                isTargetFormInTabs = true

                if (action) {
                    yield put(action(regionId, tabId, param, options))
                }
            }
        }
    }

    return isTargetFormInTabs
}

function* switchTab(action) {
    const { type, meta, payload } = action
    const { regionId } = payload

    const regions = yield select(regionsSelector)

    if (regionId && get(regions, `${regionId}.datasource`)) {
        /* no switch logic if a datasource is passed */
        yield cancel()
    }

    if (type === actionTypes.TOUCH) {
        const regions = yield select(regionsSelector)
        const tabsRegions = filter(values(regions), region => region.tabs)

        const { form } = meta

        for (const { tabs, regionId } of tabsRegions) {
            yield tabTraversal(setActiveRegion, tabs, regionId, form)
            yield mapUrl(regionId)
        }
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

        const { id } = meta

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
                yield mapUrl(active)
                yield cancel()
            }

            const activeEntity = first(visibleEntity[regionId])

            if (regionId && activeEntity) {
                yield put(setActiveRegion(regionId, activeEntity))
                yield mapUrl(activeEntity)
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

            yield put(dataRequest(datasource))
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

function* validateTabs({ payload, meta }) {
    const { id } = payload
    const { blurValidation = false } = meta

    const { regions = {}, widgets = {} } = yield select()
    const errors = yield select(dataSourceErrors(id)) || {}

    const form = find(Object.keys(widgets), widgetId => get(widgets, `${widgetId}.datasource`) === id)

    if (!form || isEmpty(regions)) {
        return
    }

    const invalid = Object.values(errors).some(error => error)

    const tabsRegions = Object.values(regions)
        .filter(region => region.tabs)

    for (const { tabs, regionId } of tabsRegions) {
        yield tabTraversal(
            setTabInvalid,
            tabs,
            regionId,
            form,
            invalid,
            { ...payload, blurValidation, form },
        )
    }
}

export default [
    takeEvery(MAP_URL, mapUrl),
    takeEvery([
        METADATA_SUCCESS,
        actionTypes.TOUCH,
        registerRegion,
        registerWidget,
        setActiveRegion,
    ], switchTab),
    takeEvery([failValidate, startValidate], validateTabs),
]
