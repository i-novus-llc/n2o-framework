import React, { useMemo } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import {
    addComponent,
    changePage,
    changeSize,
    dataRequest,
    removeComponent,
    setSorting as setDataSourceSorting,
} from '../../ducks/datasource/store'
import { setModel } from '../../ducks/models/store'
import { DataSourceContext } from '../widget/context'
import { dataSourceByIdSelector } from '../../ducks/datasource/selectors'

import { WithDatasourceInitTypes } from './propTypes'
import { ModelPrefix } from './const'

export const useDatasourceProps = (datasource) => {
    const {
        loading,
        sorting,
        paging,
    } = useSelector(dataSourceByIdSelector(datasource))

    return {
        loading,
        sorting,
        ...paging,
    }
}

/**
 * Методы взаимодействия с DataSource
 */
export const useDatasourceMethods = (id, datasource) => {
    const dispatch = useDispatch()

    return useMemo(() => {
        const set = (prefix, model) => dispatch(setModel(prefix, datasource, model))

        return {
            register() {
                dispatch(addComponent(datasource, id))
            },
            unregister() {
                dispatch(removeComponent(datasource, id))
            },
            fetchData(options) {
                dispatch(dataRequest(datasource, options))
            },
            setFilter(filterModel) {
                set(ModelPrefix.filter, filterModel)
            },
            setResolve(model) {
                set(ModelPrefix.active, model)
            },
            setEdit(model) {
                set(ModelPrefix.edit, model)
            },
            setSelected(models) {
                set(ModelPrefix.selected, models)
            },
            setSorting(field, sorting) {
                dispatch(setDataSourceSorting(datasource, field, sorting))
            },
            setPage(page = 1, withCount) {
                dispatch(changePage(datasource, page, withCount))
            },
            setSize(size) {
                dispatch(changeSize(datasource, size))
            },
        }
    }, [dispatch, datasource, id])
}

/**
 * ХОК для подключения Component: any к datasource
 */
export const WithDataSource = (Component) => {
    const WithDataSource = (props) => {
        const { id, datasource } = props
        const methods = useDatasourceMethods(id, datasource)
        const datasourceProps = useDatasourceProps(datasource)

        return (
            <DataSourceContext.Provider value={methods}>
                <Component {...props} {...methods} {...datasourceProps} />
            </DataSourceContext.Provider>
        )
    }

    WithDataSource.propTypes = WithDatasourceInitTypes

    return WithDataSource
}
