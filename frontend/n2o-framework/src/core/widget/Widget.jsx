import React, { useEffect, useMemo } from 'react'
import { connect } from 'react-redux'

import { usePrevious } from '../../utils/usePrevious'
import { dataSourceByIdSelector, dataSourceModelsSelector } from '../../ducks/datasource/selectors'
import {
    dataRequest,
    setActiveModel,
    setMultiModel,
    setFilter as setDataSourceFilter,
    setSorting as setDataSourceSorting,
    changePage,
    changeSize,
} from '../../ducks/datasource/store'
import { registerDependency } from '../../actions/dependency'

import { widgetPropTypes } from './propTypes'
import { withRedux } from './withRedux'
import { FETCH_TYPE } from './const'
import { WidgetContext } from './context'

/**
 * ХОК для получения параметров в виджет из DataSource и методов взаимодействия с ним
 */
export const WidgetHOC = (WidgetComponent) => {
    /**
     * Обёртка, осуществляющая запрос за данными при смене видимости виджета
     * и прокидыванием методов взаимодействия с DataSource в контекст
     */
    const Widget = (props) => {
        const {
            visible,
            fetchData,
            setFilter,
            setResolve,
            setSelected,
            setSorting,
            setPage,
            setSize,
            fetchOnInit,
            id,
            dependency,
            dispatch,
            isInit,
        } = props
        const prevVisible = usePrevious(visible)

        useEffect(() => {
            // dispatch(registerDependency(id, dependency))
            // FIXME удалить нижнее, раскомментить верхнее, после того как бек перестанет присылать fetch зависимость
            const { fetch, ...deps } = (dependency || {})

            dispatch(registerDependency(id, deps))
        }, [id, dispatch, dependency])

        // fetch on change visible
        useEffect(() => {
            if (
                visible &&
                (typeof prevVisible === 'undefined' ? fetchOnInit : !prevVisible)
            ) { fetchData() }
        }, [visible, prevVisible, fetchData, fetchOnInit, isInit])

        const methods = useMemo(
            () => ({ fetchData, setFilter, setResolve, setSelected, setSorting, setPage, setSize }),
            [fetchData, setFilter, setResolve, setSelected, setSorting, setPage, setSize],
        )

        return (
            <WidgetContext.Provider value={methods}>
                <WidgetComponent
                    {...props}
                />
            </WidgetContext.Provider>
        )
    }

    Widget.propTypes = widgetPropTypes

    Widget.defaultProps = {
        fetch: FETCH_TYPE.always,
        fetchOnInit: true,
    }

    /**
     * Получение данных из DataSource, необходимых для виджета
     * @param {object} state
     * @param {WidgetInitialTypes} props
     * @return {WidgetDatasourceTypes}
     */
    const withDataSourceProps = (state, props) => {
        const {
            loading,
            sorting,
            page,
            count,
            size,
        } = dataSourceByIdSelector(props.datasource)(state)
        const models = dataSourceModelsSelector((props.datasource))(state)

        return {
            size,
            models,
            loading,
            sorting,
            page,
            count,
        }
    }

    /**
     * Методы взаимодействия с DataSource
     * @param {function} dispatch
     * @param {WidgetInitialTypes, WidgetDatasourceTypes} props
     * @return {WidgetMethods}
     */
    const withDatasourceMethods = (dispatch, { visible, datasource, models, fetch }) => ({
        dispatch,
        fetchData(options, force) {
            if (
                visible && (
                    fetch === FETCH_TYPE.always ||
                    (fetch === FETCH_TYPE.lazy && (!models.datasource?.length || force))
                )
            ) {
                dispatch(dataRequest(datasource, options))
            }
        },
        setFilter(filterModel) {
            dispatch(setDataSourceFilter(datasource, filterModel))
        },
        setResolve(model) {
            dispatch(setActiveModel(datasource, model))
        },
        setSelected(models) {
            dispatch(setMultiModel(datasource, models))
        },
        setSorting(field, sorting) {
            dispatch(setDataSourceSorting(datasource, field, sorting))
        },
        setPage(page = 1) {
            dispatch(changePage(datasource, page))
        },
        setSize(size) {
            dispatch(changeSize(datasource, size))
        },
    })

    return withRedux(connect(
        withDataSourceProps,
        withDatasourceMethods,
    )(Widget))
}
