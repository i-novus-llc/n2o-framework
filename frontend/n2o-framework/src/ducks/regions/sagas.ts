import { put, takeEvery, cancel, select, call, delay } from 'redux-saga/effects'
import values from 'lodash/values'
import each from 'lodash/each'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'

import { makeRootPageSelector } from '../pages/selectors'
import { mapQueryToUrl } from '../pages/sagas/restoreFilters'
import { modelsSelector } from '../models/selectors'
import {
    makeDatasourceIdSelector,
    makeWidgetFetchOnInit,
    makeWidgetFetchOnVisibility,
    makeWidgetIsInitSelector,
} from '../widgets/selectors'
import { dataRequest, failValidate, resetValidation } from '../datasource/store'
import { dataSourcesSelector } from '../datasource/selectors'
import { State as DataSources } from '../datasource/DataSource'
import { formsSelector } from '../form/selectors'
import { FormsState as Forms } from '../form/types'
import { INVALID_TAB_REDUX_KEY } from '../../components/regions/Tabs/constants'
import { State as ModelsState } from '../models/Models'
import { checkTabAvailability } from '../../components/regions/helpers'
import { State } from '../State'
import { Page } from '../pages/Pages'

import { SetTabInvalid, RegisterRegion, ServiceInfo } from './Actions'
import {
    regionsSelector, registerRegion, MAP_URL,
    setRegionServiceInfo, setTabInvalid, setActiveRegion,
} from './store'
import { State as Regions } from './Regions'

const DEFAULT_INFO_META = {
    datasources: [],
    widgets: [],
}

function* createServiceInfo(action: RegisterRegion) {
    const { payload } = action
    const { regionState } = payload
    const { tabs } = regionState

    if (!tabs) {
        yield cancel()
    }

    const { regionId } = regionState

    const serviceInfo: ServiceInfo = {}

    for (const tab of tabs) {
        const { id: tabId, content = [] } = tab

        serviceInfo[tabId] = { ...DEFAULT_INFO_META }

        for (const meta of content) {
            const { id: widgetId, datasource } = meta
            const { widgets, datasources } = serviceInfo[tabId]

            /* collects widgets ids */
            serviceInfo[tabId].widgets = [...widgets, widgetId]

            if (datasource) {
                /* collects ds ids */
                serviceInfo[tabId].datasources = [...datasources, datasource]
            }
        }
    }

    yield put(setRegionServiceInfo(regionId, serviceInfo))
}

function* lazyFetch(id: string) {
    const regions: Regions = yield select(regionsSelector)
    const models: ModelsState = yield select(modelsSelector)
    const regionCollection = values(regions)
    const idsToFetch: string[] = []

    if (!isEmpty(regionCollection)) {
        each(regionCollection, (region) => {
            const { activeEntity, alwaysRefresh } = region

            const targetTab = { ...find(region.tabs, tab => tab.id === activeEntity) }

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
            const datasource: string = yield select(makeDatasourceIdSelector(widgetId))

            const isInit: boolean = yield select(makeWidgetIsInitSelector(widgetId))
            const fetchOnInit: boolean = yield select(makeWidgetFetchOnInit(widgetId))
            const fetchOnVisibility: boolean = yield select(makeWidgetFetchOnVisibility(widgetId))

            const needToFetch = isInit && (fetchOnInit || fetchOnVisibility)

            if (needToFetch) {
                yield put(dataRequest(datasource))
            }
        }

        idsToFetch.length = 0
    }
}

function* autoSwitch({ payload }: SetTabInvalid) {
    const { invalid } = payload

    if (!invalid) {
        yield cancel()
    }

    yield delay(75)

    const regions: Regions = yield select(regionsSelector)
    const tabsRegions = Object.fromEntries(Object.entries(regions).filter(([, meta]) => meta.tabs))
    const regionsIds = Object.keys(tabsRegions)

    for (const regionId of regionsIds) {
        const region = regions[regionId]
        const { tabs } = region

        const { activeEntity } = region
        const activeTab = tabs.find(({ id }) => id === activeEntity)
        const activeTabIsInvalid = get(activeTab, INVALID_TAB_REDUX_KEY, false)

        /** Если активный tab валидный то переключаем на первый невалидный tab **/
        if (!activeTabIsInvalid) {
            const firstInvalidTab = tabs.find(({ invalid = false }) => invalid)
            const tabId = get(firstInvalidTab, 'id', '')

            if (tabId) {
                yield put(setActiveRegion(regionId, tabId))
                yield mapUrl(tabId)

                yield cancel()
            }
        }
    }

    yield cancel()
}

function* validateTabs() {
    const regions: Regions = yield select(regionsSelector)

    if (isEmpty(regions)) {
        yield cancel()
    }

    const dataSources: DataSources = yield select(dataSourcesSelector)
    const forms: Forms = yield select(formsSelector)
    const tabsRegions = Object.fromEntries(Object.entries(regions).filter(([, meta]) => meta.tabs))

    if (isEmpty(tabsRegions)) {
        yield cancel()
    }

    const state: State = yield select()
    const regionsIds = Object.keys(tabsRegions)

    for (const regionId of regionsIds) {
        const region = regions[regionId]

        const { serviceInfo } = region

        for (const [tabId, service] of Object.entries(serviceInfo)) {
            const { widgets } = service

            const { tabs } = region

            const tab = tabs.find(({ id }) => id === tabId)

            const widgetsState = get(state, 'widgets', {})
            const regionsState = get(state, 'regions', {})

            const available = tab
                ? checkTabAvailability({ serviceInfo, widgetsState, regionsState }, tab, state)
                : false

            /** Проверка tab на содержание невалидных полей **/
            const invalid = available
                ? widgets.some((widget) => {
                    const form = forms[widget]

                    if (!form) {
                        return false
                    }

                    const { datasource = null } = form

                    if (!datasource) {
                        return false
                    }

                    const { fields } = forms[widget]
                    const { errors } = dataSources[datasource]

                    const { resolve, edit } = errors
                    const fieldKeys = Object.keys(fields)
                    const availableFields = fieldKeys.filter(key => fields[key].visible &&
                        !fields[key].disabled)

                    return availableFields.some(key => resolve[key] || edit[key])
                })
                : false

            const currentInvalid = get(tab, INVALID_TAB_REDUX_KEY, false)

            if ((invalid !== currentInvalid)) {
                const { parent } = tabsRegions[regionId]

                /** Вложенные tabs в tabs, установка invalid родительскому tab **/
                if (parent && !isEmpty(parent)) {
                    const { regionId, tabId } = parent

                    yield put(setTabInvalid(regionId, tabId, invalid))
                }

                yield put(setTabInvalid(regionId, tabId, invalid))
            }
        }
    }
}

function* mapUrl(value: string) {
    const rootPage: Page = yield select(makeRootPageSelector())
    const routes = rootPage?.metadata?.routes

    if (routes) {
        yield call(mapQueryToUrl, rootPage.id, null)
        yield call(lazyFetch, value)
    }
}

export const sagas = [
    takeEvery(registerRegion, createServiceInfo),
    takeEvery([failValidate, resetValidation], validateTabs),
    takeEvery(setTabInvalid, autoSwitch),
    // @ts-ignore FIXME переделать на rtk экшен
    takeEvery(MAP_URL, mapUrl),
]
