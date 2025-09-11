import React, { ComponentType, CSSProperties, useEffect } from 'react'
import { Dispatch } from 'redux'
import { useStore } from 'react-redux'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'

import { State as RegionsState, TabMeta } from '../../../ducks/regions/Regions'
import { DataSourceModels } from '../../../core/datasource/const'
import { ServiceInfo } from '../../../ducks/regions/Actions'
import { State as WidgetsState } from '../../../ducks/widgets/Widgets'
import { setRegionVisibility } from '../../../ducks/regions/store'
import { EMPTY_OBJECT } from '../../../utils/emptyTypes'

import { create } from './helpers'

export interface Props {
    activeEntity: string
    changeActiveEntity(id: string): void
    className?: string
    hideSingleTab: boolean
    maxHeight?: string
    scrollbar: boolean
    title?: string
    style: CSSProperties
    contentStyle: CSSProperties
    contentClassName: string
    pageId: string
    disabled?: boolean
    lazy: boolean
    datasource?: string
    activeTabFieldId?: string
    setResolve(model: Record<string, unknown>): void
    models: DataSourceModels
    resolveModel: Record<string, Record<string, unknown>>
}

interface Tabs extends Props {
    tabs: TabMeta[]
    id: string
    active: string
    serviceInfo: ServiceInfo
    widgetsState: WidgetsState
    regionsState: RegionsState
    dispatch: Dispatch
    alwaysRefresh?: boolean
}

export function TabsController<TProps extends Tabs>(Component: ComponentType<TProps>): ComponentType<TProps> {
    function Wrapper(props: TProps) {
        const { getState } = useStore()
        const state = getState()

        const {
            tabs: tabsMeta, dispatch, id: regionId, pageId, activeEntity: active,
            lazy, serviceInfo, widgetsState, regionsState,
            className, maxHeight, alwaysRefresh, style = EMPTY_OBJECT, scrollbar = true,
        } = props

        const regionParams = {
            pageId,
            active,
            lazy,
            regionId,
            serviceInfo,
            widgetsState,
            regionsState,
            alwaysRefresh,
            tabSubContentClass: 'tab-sub-content',
        }

        const tabs = create(tabsMeta, regionParams, state)

        useEffect(() => {
            if (
                isEmpty(regionsState) ||
                isEmpty(widgetsState) ||
                isEmpty(serviceInfo) ||
                isEmpty(tabs)
            ) { return }

            const visible = tabs.some(({ visible }) => visible)
            const reduxVisible = get(regionsState, `${regionId}.visible`, true)

            if (visible !== reduxVisible) { dispatch(setRegionVisibility(regionId, visible)) }
        }, [tabs])

        if (isEmpty(tabs)) { return null }

        const contentStyle = maxHeight ? { maxHeight } : {}
        const contentClassName = classNames({ scrollable: maxHeight, 'scroll-hidden': maxHeight && !scrollbar })

        return (
            <Component
                {...props}
                tabs={tabs}
                className={classNames('n2o-tabs-region', className)}
                style={style}
                contentStyle={contentStyle}
                contentClassName={contentClassName}
            />
        )
    }

    return Wrapper
}
