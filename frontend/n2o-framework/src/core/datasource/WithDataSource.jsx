import React, { useEffect, useMemo } from 'react'
import { connect } from 'react-redux'

import { usePrevious } from '../../utils/usePrevious'
import {
    addComponent,
    changePage,
    changeSize,
    dataRequest,
    removeComponent,
    setActiveModel,
    setFilter as setDataSourceFilter,
    setMultiModel,
    setSorting as setDataSourceSorting,
} from '../../ducks/datasource/store'
import { FETCH_TYPE } from '../widget/const'
import { DataSourceContext } from '../widget/context'

import { WithDataSourceTypes } from './propTypes'

/**
 * ХОК для подключения Component: any к datasource
 */
export const WithDataSource = (Component) => {
    /**
     * Обёртка, осуществляет регистрацию или отписку от datasource (при visibility changed)
     * Запрос за data
     * прокидывает methods - управление datasource
     */
    const Register = (props) => {
        const {
            visible,
            addComponent,
            switchRegistration,
            setFilter,
            setResolve,
            setSelected,
            setSorting,
            setPage,
            setSize,
            fetchData,
        } = props

        const prevVisible = usePrevious(visible)

        const methods = useMemo(
            () => ({ fetchData, setFilter, setResolve, setSelected, setSorting, setPage, setSize }),
            [fetchData, setFilter, setResolve, setSelected, setSorting, setPage, setSize],
        )

        /* FIXME for debugging, remove temp useEffect with addComponent() */

        useEffect(() => {
            addComponent()
        }, [addComponent])

        useEffect(() => {
            if (visible !== prevVisible) {
                switchRegistration(visible)
                fetchData()
            }
        }, [visible, prevVisible, switchRegistration, fetchData])

        return (
            <DataSourceContext.Provider value={methods}>
                <Component {...props} methods={methods} />
            </DataSourceContext.Provider>
        )
    }

    Register.propTypes = WithDataSourceTypes

    const mapDispatchToProps = (dispatch,
        {
            datasource: dataSourceId,
            id: componentId,
            visible,
            models,
            fetch,
        }) => {
        const addComponentToSource = () => dispatch(addComponent(dataSourceId, componentId))
        const removeComponentFromSource = () => dispatch(removeComponent(dataSourceId, componentId))

        /**
         * Методы взаимодействия с DataSource
         */

        return {
            addComponent: () => addComponentToSource(),
            switchRegistration: visible => (visible ? addComponentToSource() : removeComponentFromSource()),
            fetchData(options, force) {
                if (
                    visible && (
                        fetch === FETCH_TYPE.always ||
                        (fetch === FETCH_TYPE.lazy && (!models.datasource?.length || force))
                    )
                ) {
                    dispatch(dataRequest(dataSourceId, options))
                }
            },
            setFilter(filterModel) {
                dispatch(setDataSourceFilter(dataSourceId, filterModel))
            },
            setResolve(model) {
                dispatch(setActiveModel(dataSourceId, model))
            },
            setSelected(models) {
                dispatch(setMultiModel(dataSourceId, models))
            },
            setSorting(field, sorting) {
                dispatch(setDataSourceSorting(dataSourceId, field, sorting))
            },
            setPage(page = 1) {
                dispatch(changePage(dataSourceId, page))
            },
            setSize(size) {
                dispatch(changeSize(dataSourceId, size))
            },
        }
    }

    return connect(null, mapDispatchToProps)(Register)
}
