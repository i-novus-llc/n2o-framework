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
    setEditModel,
} from '../../ducks/datasource/store'
import { FETCH_TYPE } from '../widget/const'
import { DataSourceContext } from '../widget/context'
import { dataSourceModelsSelector } from '../../ducks/datasource/selectors'

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

    const mapStateToProps = (state, { datasource }) => ({
        models: datasource ? dataSourceModelsSelector(datasource)(state) : {},
    })

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
            removeComponent: () => removeComponentFromSource(),
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
            setEdit(model) {
                dispatch(setEditModel(dataSourceId, model))
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

    return connect(mapStateToProps, mapDispatchToProps)(Register)
}
