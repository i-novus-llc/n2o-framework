import React, { ComponentType } from 'react'
import { connect } from 'react-redux'
import { Dispatch } from 'redux'

import { WithDataSource as DataSourceHOC } from '../datasource/WithDataSource'
import { EMPTY_ARRAY, EMPTY_OBJECT } from '../../utils/emptyTypes'
import { dataSourceModelByPrefixSelector } from '../../ducks/datasource/selectors'
import { updatePaging } from '../../ducks/datasource/store'
import { ModelPrefix } from '../datasource/const'
import { State } from '../../ducks/State'
import { type State as ModelsState } from '../../ducks/models/Models'

import { FETCH_TYPE } from './const'
import { DataSourceContext, METHODS } from './context'

export type PropsFromComponent<P> = P extends ComponentType<infer U> ? U : never

export interface BaseProps {
    visible: boolean
    dispatch: Dispatch
    paging?: { showLast?: boolean }
    datasource: string
    updatePaging(datasource: string, options: { withCount: boolean }): void
}

export interface LifecycleProps {
    isInit: boolean
    fetchOnInit: boolean
    fetchOnVisibility: boolean
    register(): void
    unregister(): void
    fetchData(options?: Record<string, unknown>, force?: boolean): void
    datasourceModelLength: number
    fetch: 'always' | 'lazy' | 'never'
}

export type CombinedProps<P extends object> = PropsFromComponent<P> & BaseProps & LifecycleProps

export const WithDatasourceLifeCycle = <P extends object>(Component: ComponentType<P>) => {
    class WithDatasourceLifeCycle extends React.Component<CombinedProps<P>> {
        componentDidMount() {
            const { visible, dispatch, paging = {}, datasource } = this.props

            this.switchRegistration(visible)

            if (paging.showLast === false) {
                // @ts-ignore FIXME updatePaging TS2554: Expected 1 arguments, but got 2
                dispatch(updatePaging(datasource, { withCount: false }))
            }
        }

        componentDidUpdate({ visible: prevVisible, isInit: prevInit }: { visible: boolean, isInit: boolean }) {
            const { visible, isInit, fetchOnInit, fetchOnVisibility } = this.props

            if (isInit !== prevInit) {
                this.switchRegistration(visible)

                if (fetchOnInit) {
                    this.fetchData()
                }
            } else if (visible !== prevVisible) {
                this.switchRegistration(visible)
                if (fetchOnVisibility) {
                    // TableAT.fetchOnVisibilityTest
                    // https://sandbox-dev.i-novus.ru/editor/3tghM/?stand=https://next-n2o.i-novus.ru/tests/
                    // не выполняется fetch из за указанного fetchOnVisibility = false
                    this.fetchData()
                }
            }
        }

        componentWillUnmount() { this.switchRegistration(false) }

        render() {
            const methods = this.context
            const patchedMethods = { ...methods, fetchData: this.fetchData }

            return (
                <DataSourceContext.Provider value={patchedMethods}>
                    <Component {...this.props} {...patchedMethods} />
                </DataSourceContext.Provider>
            )
        }

        switchRegistration = (connected: boolean) => {
            const { register, unregister } = this.props

            if (connected) {
                register()
            } else {
                unregister()
            }
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
    const dataSourceModel = dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)(state) as ModelsState['datasource']

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
