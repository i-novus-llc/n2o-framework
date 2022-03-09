import React, { useEffect, useMemo } from 'react'
import { useDispatch, useSelector } from 'react-redux'

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
    const WithDataSource = (props) => {
        const {
            id,
            visible,
            datasource,
            fetch,
        } = props
        const prevVisible = usePrevious(visible)
        const dispatch = useDispatch()
        const models = useSelector(dataSourceModelsSelector(datasource))
        const isEmptyData = !(models.datasource?.length)
        const methods = useMemo(() => {
            const addComponentToSource = () => dispatch(addComponent(datasource, id))
            const removeComponentFromSource = () => dispatch(removeComponent(datasource, id))

            /**
             * Методы взаимодействия с DataSource
             */
            return {
                switchRegistration: visible => (visible ? addComponentToSource() : removeComponentFromSource()),
                fetchData(options, force) {
                    if (
                        visible && (
                            fetch === FETCH_TYPE.always ||
                            (fetch === FETCH_TYPE.lazy && (isEmptyData || force))
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
                setEdit(model) {
                    dispatch(setEditModel(datasource, model))
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
            }
        }, [dispatch, datasource, id, visible, fetch, isEmptyData])

        useEffect(() => {
            if (visible !== prevVisible) {
                methods.switchRegistration(visible)
                methods.fetchData()
            }
        }, [visible, prevVisible, methods])

        return (
            <DataSourceContext.Provider value={methods}>
                <Component {...props} {...methods} models={models} />
            </DataSourceContext.Provider>
        )
    }

    WithDataSource.propTypes = WithDataSourceTypes

    return WithDataSource
}
