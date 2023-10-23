import React from 'react'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'
import classNames from 'classnames'
import get from 'lodash/get'

import { RegionContent } from '../RegionContent'
import { TabMeta, State as RegionsState } from '../../../ducks/regions/Regions'
import { ServiceInfo } from '../../../ducks/regions/Actions'
import { State as WidgetsState } from '../../../ducks/widgets/Widgets'
import { checkTabAvailability, getTabReduxMeta, VISIBLE, ENABLED } from '../helpers'
import { State } from '../../../ducks/State'

import { INVALID_TAB_REDUX_KEY, INVALID_TEXT } from './constants'

interface RegionParams {
    pageId: string
    active: string
    lazy: boolean
    serviceInfo: ServiceInfo
    widgetsState: WidgetsState
    regionsState: RegionsState
    tabSubContentClass: string
    regionId: string
}
const mapping = (tab: TabMeta, regionParams: RegionParams, state: State) => {
    const { content: metaContent, id: tabId } = tab
    const { serviceInfo, regionId } = regionParams

    if (isEmpty(serviceInfo)) {
        return { ...tab, content: [] }
    }

    const { widgetsState, regionsState } = regionParams
    const service = {
        serviceInfo,
        widgetsState,
        regionsState,
    }

    const visible = checkTabAvailability(service, tab, state, VISIBLE)
    const enabled = checkTabAvailability(service, tab, state, ENABLED)

    const content = [<RegionContent tabId={tabId} content={metaContent} {...omit(regionParams, ['serviceInfo', 'widgetsState'])} />]
    const tabReduxMeta = getTabReduxMeta(regionsState, regionId, tabId)
    const invalid = get(tabReduxMeta, INVALID_TAB_REDUX_KEY, false)

    return {
        ...tab,
        content,
        visible,
        disabled: !enabled,
        className: classNames({ invalid }),
        tooltip: invalid && INVALID_TEXT,
    }
}

/* replaces meta content with React components */
export const create = (
    tabsMeta: TabMeta[],
    regionParams: RegionParams,
    state: State,
) => tabsMeta.map(tab => mapping(tab, regionParams, state))
