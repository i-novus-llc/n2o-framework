import { FETCH_TYPE } from '../../core/widget/const'
import { ServiceInfo } from '../../ducks/regions/Actions'
import { State as WidgetsState } from '../../ducks/widgets/Widgets'
import { State as RegionsState } from '../../ducks/regions/Regions'

interface Service {
    serviceInfo: ServiceInfo,
    widgetsState: WidgetsState,
    regionsState: RegionsState,
}

export const getFirstVisibleTab = (service: Service) => {
    const { serviceInfo, widgetsState, regionsState } = service
    const tabsIds = Object.keys(serviceInfo)

    return tabsIds.find((id) => {
        const { widgets } = serviceInfo[id]

        return widgets.some(id => widgetsState[id]?.visible || regionsState[id])
    })
}

export const setFirstVisibleTab = (
    service: Service,
    changeActiveEntity: (id: string) => void,
    activeTabFieldId?: string | undefined,
    setResolve?: (model: Record<string, unknown>) => void | undefined,
    model?: Record<string, unknown>,
) => {
    const firstVisibleTab = getFirstVisibleTab(service)

    if (firstVisibleTab) {
        changeActiveEntity(firstVisibleTab)

        if (setResolve && activeTabFieldId && model) {
            setResolve({ ...model, [activeTabFieldId]: firstVisibleTab })
        }
    }
}

export const checkTabVisibility = (service: Service, tabId?: string) => {
    if (!tabId) {
        return false
    }

    const { serviceInfo, widgetsState, regionsState } = service
    const { widgets } = serviceInfo[tabId]

    return widgets.some(id => widgetsState[id]?.visible || regionsState[id])
}

export const getTabReduxMeta = (regionsState: RegionsState, regionId: string, tabId: string) => {
    const { tabs } = regionsState[regionId]

    return tabs.find(({ id }) => id === tabId)
}

export function getFetchOnInit(metaFetchOnInit: boolean, lazy: boolean, active: string) {
    if (!lazy || active) {
        return metaFetchOnInit
    }

    return false
}

export function getFetch(lazy: boolean, active: string) {
    if (!lazy) {
        return FETCH_TYPE.always
    }

    if (active) {
        return FETCH_TYPE.lazy
    }

    return FETCH_TYPE.never
}
