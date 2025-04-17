import React, { useEffect, useLayoutEffect, ComponentType } from 'react'
import { connect, useStore } from 'react-redux'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import flowRight from 'lodash/flowRight'
import { Dispatch } from 'redux'

import { WithDataSource } from '../../core/widget/WithDataSource'
import {
    makeRegionActiveEntitySelector,
    makeRegionIsInitSelector,
    mapUrl,
    registerRegion,
    setActiveRegion,
    unregisterRegion,
} from '../../ducks/regions/store'
import { type State } from '../../ducks/State'
import { type State as WidgetsState } from '../../ducks/widgets/Widgets'
import { type State as RegionsState, type TabMeta } from '../../ducks/regions/Regions'
import { type ServiceInfo } from '../../ducks/regions/Actions'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { setFirstAvailableTab, checkTabAvailability, getFirstAvailableTab, getTabMetaById, Service } from './helpers'

export type Config = { listKey: string }
export type RegionEnhancer = { changeActiveEntity(value: string): void }

export interface RegionProps {
    id: string
    activeEntity?: string
    activeTabFieldId?: string
    query: Record<string, string>
    dispatch: Dispatch
    lazy?: boolean
    alwaysRefresh?: boolean
    widgetsState: WidgetsState
    serviceInfo: ServiceInfo
    regionsState: RegionsState
    activeParam: string
    setResolve?(model: Record<string, unknown>): void | undefined,
    tabs?: TabMeta[]
    active?: string
    routable?: string
    resolveModel?: Record<string, Record<string, string>>
    datasource: string
    parent?: string | null
}

export const createRegionContainer = <P extends RegionEnhancer>(config: Config) => (WrappedComponent: ComponentType<P & RegionProps>) => {
    const { listKey } = config

    const RegionContainer = (props: P & RegionProps) => {
        const {
            id: regionId,
            activeEntity,
            activeTabFieldId,
            query,
            dispatch,
            lazy,
            alwaysRefresh,
            widgetsState,
            serviceInfo,
            regionsState,
            activeParam,
            setResolve,
            tabs,
            active,
            routable,
            resolveModel = EMPTY_OBJECT as never,
            datasource = null,
            parent = null,
        } = props

        const { getState } = useStore()

        const service = { serviceInfo, widgetsState, regionsState, tabs }
        const isModelDependency = activeTabFieldId && datasource
        const prepared = !isEmpty(tabs) || !isEmpty(serviceInfo) || !isEmpty(widgetsState)

        const changeActiveEntity = (value: string) => {
            dispatch(setActiveRegion(regionId, value))
            dispatch(mapUrl(value))
        }

        const register = () => {
            dispatch(registerRegion(regionId, {
                regionId,
                activeEntity: activeEntity || active,
                isInit: true,
                lazy,
                alwaysRefresh,
                [listKey]: get(props, listKey, []),
                datasource,
                activeTabFieldId,
                parent,
                visible: true,
            }))
        }

        useLayoutEffect(() => {
            register()
            // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [])

        /** Эффект авто выбора активной вкладки TabsRegion **/
        useEffect(() => {
            if (!tabs || isEmpty(tabs) || !prepared) { return }

            if (!routable) {
                if (!activeEntity) {
                    const state = getState()

                    setFirstAvailableTab(
                        service,
                        changeActiveEntity,
                        state,
                    )

                    return
                }

                return
            }

            const delay = setTimeout(() => {
                const state = getState()

                if (isModelDependency) {
                    const model = resolveModel[datasource]

                    /** Авто выбор первой видимой вкладки + setResolve если model не существует (initial) **/
                    if (isEmpty(model) || model[activeTabFieldId] === undefined) {
                        setFirstAvailableTab(
                            service,
                            changeActiveEntity,
                            state,
                            activeTabFieldId,
                            setResolve,
                            model,
                        )

                        return
                    }

                    const activeFromResolve = model[activeTabFieldId]

                    if (!serviceInfo[activeFromResolve]) {
                        changeActiveEntity(activeFromResolve)

                        return
                    }

                    const tab = getTabMetaById(activeFromResolve, tabs)
                    const visible = checkTabAvailability(service, tab || {} as TabMeta, state)

                    /* TODO пересмотреть обоюдную зависимость, active из resolve model + setModel из табов.
                        текущая реализация:
                        если в tabs существует activeTabFieldId из model
                        (прим. tabs: { tab1, tab2 }, model: { activeTabFieldId: tab1 }),
                        но tab1 невидим -> срабатывает auto switch (setModel) на первый видимый (tab2),
                        иначе происходит игнорирование (прим. tabs: { tab1, tab2 }, model: { activeTabFieldId: tab15 }),
                        https://next.test.n2oapp.net/sandbox/view/k0FMU/ */
                    if (!visible) {
                        setFirstAvailableTab(
                            service,
                            changeActiveEntity,
                            state,
                            activeTabFieldId,
                            setResolve,
                            model,
                        )

                        return
                    }

                    /** active из resolve model **/
                    changeActiveEntity(activeFromResolve)

                    return
                }

                /** activeParam влияет на отображение имени региона в url **/
                const activeFromQuery = query[regionId] || query[activeParam]
                const tab = getTabMetaById(activeFromQuery, tabs)

                const visible = checkTabAvailability(service, tab || {} as TabMeta, state)

                if (!activeFromQuery || !visible) {
                    /** active отсутствует в query (initial),
                     либо active получен, но таб невидим,
                     active становится первая видимая вкладка **/

                    setFirstAvailableTab(service, changeActiveEntity, state)

                    return
                }

                changeActiveEntity(activeFromQuery)
            }, 0)

            // eslint-disable-next-line consistent-return
            return () => { clearTimeout(delay) }

            // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [widgetsState, resolveModel])

        /** Эффект автовыбора активной вкладки TabsRegion **/
        useEffect(() => {
            if (!prepared || activeEntity || routable) { return }

            const state = getState()
            const firstVisibleTab = getFirstAvailableTab(state, service)

            dispatch(setActiveRegion(regionId, firstVisibleTab))
            // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [widgetsState])

        useEffect(() => {
            return () => {
                dispatch(unregisterRegion(regionId))
            }
        }, [dispatch, regionId])

        return <WrappedComponent {...props} changeActiveEntity={changeActiveEntity} tabs={tabs} />
    }

    const mapStateToProps = (state: State, props: RegionProps) => ({
        isInit: makeRegionIsInitSelector(props.id)(state),
        activeEntity: makeRegionActiveEntitySelector(props.id)(state),
        resolveModel: get(state, 'models.resolve', {}),
        query: get(state, 'router.location.query', {}),
        serviceInfo: get(state, `regions.${props.id}.serviceInfo`, {}),
        widgetsState: get(state, 'widgets', {}),
        regionsState: get(state, 'regions', {}),
    })

    const mapDispatchToProps = (dispatch: Dispatch) => ({ dispatch })

    return flowRight(connect(mapStateToProps, mapDispatchToProps), WithDataSource)(RegionContainer)
}

export default createRegionContainer
