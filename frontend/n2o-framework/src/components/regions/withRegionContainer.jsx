import React, { useEffect, useLayoutEffect } from 'react'
import { compose, lifecycle } from 'recompose'
import { connect, useStore } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

import { WithDataSource } from '../../core/widget/WithDataSource'
import {
    makeRegionActiveEntitySelector,
    makeRegionIsInitSelector,
    mapUrl,
    registerRegion,
    setActiveRegion,
    unregisterRegion,
} from '../../ducks/regions/store'

import { setFirstAvailableTab, checkTabAvailability, getFirstAvailableTab, getTabMetaById } from './helpers'

export const createRegionContainer = config => (WrappedComponent) => {
    const { listKey } = config

    const mapStateToProps = createStructuredSelector({
        isInit: (state, props) => makeRegionIsInitSelector(props.id)(state),
        activeEntity: (state, props) => makeRegionActiveEntitySelector(props.id)(state),
        resolveModel: state => get(state, 'models.resolve', {}),
        query: state => get(state, 'router.location.query', {}),
        serviceInfo: (state, props) => get(state, `regions.${props.id}.serviceInfo`, {}),
        widgetsState: state => get(state, 'widgets', {}),
        regionsState: state => get(state, 'regions', {}),
    })

    const mapDispatchToProps = dispatch => ({
        dispatch,
    })

    const RegionContainer = (props) => {
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
            resolveModel = {},
            datasource = null,
            routable = true,
            parent = null,
        } = props

        const { getState } = useStore()

        const service = { serviceInfo, widgetsState, regionsState, tabs }
        const isModelDependency = activeTabFieldId && datasource
        const prepared = !isEmpty(tabs) || !isEmpty(serviceInfo) || !isEmpty(widgetsState)

        const changeActiveEntity = (value) => {
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
            }))
        }

        useLayoutEffect(() => {
            register()
            // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [])

        /** Эффект автовыбора активной вкладки TabsRegion **/
        useEffect(() => {
            if (!prepared || !routable) {
                return
            }

            const delay = setTimeout(() => {
                const state = getState()

                if (isModelDependency) {
                    const model = resolveModel[datasource]

                    /** Автовыбор первой видимой вкладки + setResolve если model не существует (initial) **/
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
                    const visible = checkTabAvailability(service, tab, state)

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

                const visible = checkTabAvailability(service, tab, state)

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
            return () => {
                clearTimeout(delay)
            }

            // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [widgetsState, resolveModel])

        /** Эффект автовыбора активной вкладки TabsRegion **/
        useEffect(() => {
            if (!prepared || activeEntity || routable) {
                return
            }

            const state = getState()
            const firstVisibleTab = getFirstAvailableTab(state, service)

            dispatch(setActiveRegion(regionId, firstVisibleTab))
            // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [widgetsState])

        return <WrappedComponent {...props} changeActiveEntity={changeActiveEntity} tabs={tabs} />
    }

    const enhance = compose(
        connect(
            mapStateToProps,
            mapDispatchToProps,
        ),
        lifecycle({
            componentWillUnmount() {
                const { dispatch, id } = this.props

                dispatch(unregisterRegion(id))
            },
        }),
    )

    return enhance(WithDataSource(RegionContainer))
}

export default createRegionContainer
