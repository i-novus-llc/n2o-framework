import React, { ComponentType } from 'react'
import { connect } from 'react-redux'
import { Dispatch } from 'redux'

import { WithDataSource as DataSourceHOC } from '../datasource/WithDataSource'
import { EMPTY_ARRAY, EMPTY_OBJECT } from '../../utils/emptyTypes'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'
import { updatePaging } from '../../ducks/datasource/store'
import { ModelPrefix } from '../models/types'
import { State } from '../../ducks/State'
import { DataSourceCache } from '../../ducks/datasource/DataSource'

import { FETCH_TYPE } from './const'
import { DataSourceContext, METHODS } from './context'
import { getData } from './useData'

export type PropsFromComponent<P> = P extends ComponentType<infer U> ? U : never

export interface BaseProps {
    visible: boolean
    dispatch: Dispatch
    paging?: { showLast?: boolean }
    datasource: string
    updatePaging(datasource: string, options: { withCount: boolean }): void
    id: string
}

export interface LifecycleProps {
    isInit: boolean
    fetchOnInit: boolean
    fetchOnVisibility: boolean
    fetchData(options?: Record<string, unknown>, force?: boolean): void
    datasourceModelLength: number
    fetch: 'always' | 'lazy' | 'never'
}

export type CombinedProps<P extends object> = PropsFromComponent<P> & BaseProps & LifecycleProps

export const WithDatasourceLifeCycle = <P extends object>(Component: ComponentType<P>) => {
    class WithDatasourceLifeCycle extends React.Component<CombinedProps<P>> {
        componentDidMount() {
            const { dispatch, paging = {}, datasource } = this.props

            if (paging.showLast === false) {
                dispatch(updatePaging(datasource, {}))
            }
        }

        componentDidUpdate({ visible: prevVisible, isInit: prevInit }: { visible: boolean, isInit: boolean }) {
            const { visible, isInit, fetchOnInit, fetchOnVisibility } = this.props

            if (isInit !== prevInit && fetchOnInit) {
                const { id } = this.props

                const cache = getData<DataSourceCache>(id)

                if (cache?.paging || cache?.sorting !== undefined) { return }

                this.fetchData()
            } else if (visible !== prevVisible && fetchOnVisibility) {
                // TableAT.fetchOnVisibilityTest
                // https://sandbox-dev.i-novus.ru/editor/3tghM/?stand=https://next-n2o.i-novus.ru/tests/
                // не выполняется fetch из за указанного fetchOnVisibility = false
                this.fetchData()
            }
        }

        render() {
            const methods = this.context
            const patchedMethods = { ...methods, fetchData: this.fetchData }

            return (
                <DataSourceContext.Provider value={patchedMethods}>
                    <Component {...this.props} {...patchedMethods} />
                </DataSourceContext.Provider>
            )
        }

        fetchData = (options?: Record<string, unknown>, force?: boolean) => {
            const { fetchData, visible, datasourceModelLength, fetch } = this.props
            const isEmptyData = datasourceModelLength === 0

            if (
                (visible && (
                    fetch === FETCH_TYPE.always ||
                    (fetch === FETCH_TYPE.lazy && isEmptyData)
                )) || force
            ) {
                fetchData(options)
            }
        }
    }

    WithDatasourceLifeCycle.contextType = DataSourceContext

    // FIXME
    return WithDatasourceLifeCycle as never
}

const mapStateToProps = (state: State, { datasource }: { datasource: string }) => {
    const dataSourceModel = getModelByPrefixAndNameSelector(ModelPrefix.source, datasource)(state)

    return { datasourceModelLength: dataSourceModel?.length || 0 }
}

const mapDispatchToProps = (dispatch: Dispatch) => ({ dispatch })

const WithSource = <P extends object>(Component: ComponentType<P>) => DataSourceHOC(
    connect(mapStateToProps, mapDispatchToProps)(WithDatasourceLifeCycle(Component) as never),
)

export const WithDataSource = <P extends object>(Component: ComponentType<P>) => {
    const WithDataSource = WithSource(Component) as ComponentType<P>

    return (props: P & { datasource?: string; widget?: boolean }) => {
        const { datasource, widget = true } = props

        if (datasource) { return <WithDataSource {...props} /> }

        const models = {
            datasource: EMPTY_ARRAY,
            resolve: EMPTY_OBJECT,
            multi: EMPTY_ARRAY,
            edit: EMPTY_OBJECT,
            filter: EMPTY_OBJECT,
        }

        const extraProps = widget ? { page: 1, count: 0, size: 0 } : {}

        // without datasource
        return <Component loading={false} {...props} {...extraProps} models={models} {...METHODS} />
    }
}
