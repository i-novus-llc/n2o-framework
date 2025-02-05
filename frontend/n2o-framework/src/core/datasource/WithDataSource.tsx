import React, { useMemo, ComponentType, FC } from 'react'
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
import { ExpressionContext } from '../Expression/Context'
import { type Model } from '../../ducks/models/selectors'

import { ModelPrefix, SortDirection } from './const'

export const useDatasourceProps = (datasource: string) => {
    const {
        additionalInfo,
        loading,
        sorting,
        paging,
    } = useSelector(dataSourceByIdSelector(datasource))

    return {
        additionalInfo,
        loading,
        sorting,
        ...paging,
    }
}

/**
 * Методы взаимодействия с DataSource
 */
export const useDatasourceMethods = (id: string, datasource: string) => {
    const dispatch = useDispatch()

    return useMemo(() => {
        const set = (prefix: ModelPrefix, model: Model) => dispatch(setModel(prefix, datasource, model))

        return {
            register() {
                dispatch(addComponent(datasource, id))
            },
            unregister() {
                dispatch(removeComponent(datasource, id))
            },
            fetchData(options: Partial<{ size: number, page: number }>) {
                dispatch(dataRequest(datasource, options))
            },
            setFilter(filterModel: Model) {
                set(ModelPrefix.filter, filterModel)
            },
            setResolve(model: Model) {
                set(ModelPrefix.active, model)
            },
            setEdit(model: Model) {
                set(ModelPrefix.edit, model)
            },
            setSelected(models: Model) {
                set(ModelPrefix.selected, models)
            },
            setSorting(field: string, sorting: SortDirection) {
                dispatch(setDataSourceSorting(datasource, field, sorting))
            },
            // eslint-disable-next-line @typescript-eslint/default-param-last
            setPage(page = 1, options: Record<string, unknown>) {
                dispatch(changePage(datasource, page, options))
            },
            setSize(size: number) {
                dispatch(changeSize(datasource, size))
            },
        }
    }, [dispatch, datasource, id])
}

export interface WithDataSourceProps {
    id: string
    datasource: string
}

/**
 * ХОК для подключения Component: any к datasource
 */
export const WithDataSource = <P extends WithDataSourceProps>(
    Component: ComponentType<P & WithDataSourceProps>,
) => {
    const WithDataSourceWrapper: FC<P> = (props: WithDataSourceProps & P) => {
        const { id, datasource } = props

        const methods = useDatasourceMethods(id, datasource)
        const { additionalInfo, ...datasourceProps } = useDatasourceProps(datasource)

        return (
            <DataSourceContext.Provider value={methods}>
                <ExpressionContext.Provider value={{ $additional: additionalInfo }}>
                    <Component {...props} {...methods} {...datasourceProps} />
                </ExpressionContext.Provider>
            </DataSourceContext.Provider>
        )
    }

    return WithDataSourceWrapper
}
