import React from 'react'

import { WithDataSourceTypes } from '../datasource/propTypes'
import { WithDataSource as DataSourceHOC } from '../datasource/WithDataSource'

import { FETCH_TYPE } from './const'
import { DataSourceContext } from './context'

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

    return WithDatasourceLifeCycle
}

export const WithDataSource = Component => DataSourceHOC(WithDatasourceLifeCycle(Component))
