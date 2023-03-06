import React from 'react'
import { connect } from 'react-redux'

import { WithDataSourceTypes } from '../datasource/propTypes'
import { WithDataSource as DataSourceHOC } from '../datasource/WithDataSource'
import { EMPTY_ARRAY, EMPTY_OBJECT } from '../../utils/emptyTypes'
import { dataSourceModelByPrefixSelector } from '../../ducks/datasource/selectors'
import { ModelPrefix } from '../datasource/const'

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

    WithDatasourceLifeCycle.propTypes = WithDataSourceTypes

    WithDatasourceLifeCycle.contextType = DataSourceContext

    return WithDatasourceLifeCycle
}

const mapStateToProps = (state, { datasource }) => ({
    datasourceModelLength:
        dataSourceModelByPrefixSelector(datasource, ModelPrefix.source)(state)?.length || 0,
})

const WithSource = Component => DataSourceHOC(WithDatasourceLifeCycle(connect(mapStateToProps)(Component)))

export const WithDataSource = (Component) => {
    const WithDataSource = WithSource(Component)

    return (props) => {
        // eslint-disable-next-line react/prop-types
        const { datasource, widget = true } = props

        if (datasource) {
            return <WithDataSource {...props} />
        }

        const models = {
            datasource: EMPTY_ARRAY,
            resolve: EMPTY_OBJECT,
            multi: EMPTY_ARRAY,
            edit: EMPTY_OBJECT,
            filter: EMPTY_OBJECT,
        }

        const extraProps = widget ? {
            page: 1,
            count: 0,
            size: 0,
        } : {}

        // without datasource
        return <Component loading={false} {...props} {...extraProps} models={models} {...METHODS} />
    }
}
