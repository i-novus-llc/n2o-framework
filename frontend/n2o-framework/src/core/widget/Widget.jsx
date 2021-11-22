import React, { useEffect, useState } from 'react'
import { connect } from 'react-redux'

import { usePrevious } from '../../utils/usePrevious'
import { dataSourceByIdSelector, dataSourceModelsSelector } from '../../ducks/datasource/selectors'
import {
    dataRequest,
    setActiveModel,
    setMultiModel,
    setFilter as setDataSourceFilter,
    setSorting as setDataSourceSorting,
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
            fetchOnInit,
            id,
            dependency,
            dispatch,
            isInit,

            form, table, list,
        } = props
        const prevVisible = usePrevious(visible)
        // FIXME удалить после того как fetchOnInit начнёт приходить уровнем выше
        const fOnInit = fetchOnInit || form?.fetchOnInit || table?.fetchOnInit || list?.fetchOnInit

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
                (typeof prevVisible === 'undefined' ? fOnInit : !prevVisible)
            ) { fetchData() }
        }, [visible, prevVisible, fetchData, fOnInit, isInit])

        const [methods] = useState(() => ({ fetchData, setFilter, setResolve, setSelected, setSorting }))

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
        } = dataSourceByIdSelector(props.datasource)(state)
        const models = dataSourceModelsSelector((props.datasource))(state)

        return {
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
    })

    return withRedux(connect(
        withDataSourceProps,
        withDatasourceMethods,
    )(Widget))
}
