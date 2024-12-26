import isEmpty from 'lodash/isEmpty'

import { FETCH_TYPE } from '../../core/widget/const'
import { ServiceInfo } from '../../ducks/regions/Actions'
import { State as WidgetsState } from '../../ducks/widgets/Widgets'
import { State as RegionsState, TabMeta } from '../../ducks/regions/Regions'
import { State } from '../../ducks/State'
import { resolveConditions } from '../../sagas/conditions'

export const VISIBLE = 'visible'
export const ENABLED = 'enabled'

type AvailableParam = 'visible' | 'enabled'

interface Service {
    serviceInfo: ServiceInfo
    widgetsState: WidgetsState
    regionsState: RegionsState
    tabs?: TabMeta[]
}

/* checking by an available parameter (visible or enabled) */
const check = (tab: TabMeta, param: AvailableParam, state: State) => {
    const availableParam = tab[param]

    if (availableParam === false) {
        return false
    }

    const { conditions } = tab

    if (conditions && !isEmpty(conditions)) {
        const condition = conditions[param]

        if (condition) {
            const { resolve } = resolveConditions(state, condition)

            return resolve
        }
    }

    return true
}

export const checkTabAvailability = (
    service: Service,
    tab: TabMeta,
    state: State,
    param?: AvailableParam,
) => {
    if (isEmpty(tab)) {
        return false
    }

    const { id } = tab

    if (param === ENABLED) {
        return check(tab, ENABLED, state)
    }

    const { serviceInfo, widgetsState, regionsState } = service

    if (!serviceInfo[id]) {
        return false
    }

    const { widgets } = serviceInfo[id]

    const hasVisibleWidget = widgets.some(id => widgetsState[id]?.visible || regionsState[id])

    if (param === VISIBLE) {
        const visible = check(tab, VISIBLE, state)

        return visible && hasVisibleWidget
    }

    return hasVisibleWidget &&
        check(tab, ENABLED, state) &&
        check(tab, VISIBLE, state)
}

export const getFirstAvailableTab = (state: State, service: Service): string | null => {
    const { tabs = [] } = service
    const availableTab = tabs.find((tab => checkTabAvailability(service, tab, state)))

    if (availableTab) {
        const { id } = availableTab

        return id
    }

    return null
}

export const setFirstAvailableTab = (
    service: Service,
    changeActiveEntity: (id: string) => void,
    state: State,
    activeTabFieldId?: string | undefined,
    setResolve?: (model: Record<string, unknown>) => void | undefined,
    model?: Record<string, unknown>,
) => {
    const firstVisibleTab = getFirstAvailableTab(state, service)

    if (firstVisibleTab) {
        changeActiveEntity(firstVisibleTab)

        if (setResolve && activeTabFieldId && model) {
            setResolve({ ...model, [activeTabFieldId]: firstVisibleTab })
        }
    }
}

export const getTabMetaById = (tabId: string, tabs: TabMeta[]) => tabs?.find(({ id }) => id === tabId)

export const getTabReduxMeta = (regionsState: RegionsState, regionId: string, tabId: string) => {
    const { tabs } = regionsState[regionId]

    return getTabMetaById(tabId, tabs)
}

export function getFetchOnInit(metaFetchOnInit: boolean, lazy?: boolean, active?: string) {
    if (!lazy || active) {
        return metaFetchOnInit
    }

    return false
}

export function getFetch(lazy?: boolean, active?: string, tabId?: string) {
    if (!lazy || (active === tabId)) {
        return FETCH_TYPE.always
    }

    if (active) {
        return FETCH_TYPE.lazy
    }

    return FETCH_TYPE.never
}
