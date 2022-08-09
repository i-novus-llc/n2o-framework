import React from 'react'

import { WithDataSourceTypes } from '../datasource/propTypes'
import { WithDataSource as DataSourceHOC } from '../datasource/WithDataSource'

import { FETCH_TYPE } from './const'
import { DataSourceContext, METHODS } from './context'

export const WithDatasourceLifeCycle = (Component) => {
    class WithDatasourceLifeCycle extends React.Component {
        componentDidUpdate({ visible: prevVisible, isInit: prevInit }) {
            const { visible, isInit, fetchOnInit } = this.props

            if (isInit !== prevInit) {
                this.switchRegistration(visible)

                if (fetchOnInit) {
                    this.fetchData()
                }
            } else if (visible !== prevVisible) {
                this.switchRegistration(visible)
                this.fetchData()
            }
        }

        componentWillUnmount() {
            this.switchRegistration(false)
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

        switchRegistration = (connected) => {
            const { register, unregister } = this.props

            if (connected) {
                register()
            } else {
                unregister()
            }
        }

        fetchData = (options, force) => {
            const { fetchData, visible, models, fetch } = this.props
            const isEmptyData = !models.datasource.length

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

    WithDatasourceLifeCycle.propTypes = WithDataSourceTypes

    WithDatasourceLifeCycle.contextType = DataSourceContext

    return WithDatasourceLifeCycle
}

const WithSource = Component => DataSourceHOC(WithDatasourceLifeCycle(Component))

export const WithDataSource = (Component) => {
    const WithDataSource = WithSource(Component)

    return (props) => {
        // eslint-disable-next-line react/prop-types
        const { datasource } = props

        if (datasource) {
            return <WithDataSource {...props} />
        }

        const models = { datasource: [], resolve: {}, multi: [], edit: {}, filter: {} }

        // without datasource
        return <Component {...props} loading={false} page={1} count={0} size={0} models={models} {...METHODS} />
    }
}
