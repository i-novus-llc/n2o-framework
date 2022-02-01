import React, { useEffect } from 'react'
import { connect } from 'react-redux'

import { dataSourceByIdSelector, dataSourceModelsSelector } from '../../ducks/datasource/selectors'
import { registerDependency } from '../../actions/dependency'
import { WithDataSource } from '../datasource/WithDataSource'

import { widgetPropTypes } from './propTypes'
import { withRedux } from './withRedux'
import { FETCH_TYPE } from './const'

/**
 * ХОК для получения параметров в виджет из DataSource и методов взаимодействия с ним
 */
export const WidgetHOC = (WidgetComponent) => {
    /**
     * Обёртка, осуществляющая запрос за данными при смене видимости виджета
     * и прокидыванием методов взаимодействия с DataSource в контекст
     */
    const Widget = (props) => {
        const {
            id,
            dependency,
            dispatch,
        } = props

        useEffect(() => {
            dispatch(registerDependency(id, dependency))
        }, [id, dispatch, dependency])

        return <WidgetComponent {...props} />
    }

    Widget.propTypes = widgetPropTypes

    Widget.defaultProps = {
        fetch: FETCH_TYPE.always,
    }

    /**
     * Получение данных из DataSource, необходимых для виджета
     * @param {object} state
     * @param {WidgetInitialTypes} props
     * @return {WidgetDatasourceTypes}
     */
    const withDataSourceProps = (state, props) => {
        const {
            loading,
            sorting,
            page,
            count,
            size,
        } = dataSourceByIdSelector(props.datasource)(state)

        const models = dataSourceModelsSelector((props.datasource))(state)

        return {
            size,
            models,
            loading,
            sorting,
            page,
            count,
        }
    }

    const WithDataSourceWidget = WithDataSource(Widget)

    return withRedux(connect(withDataSourceProps)(WithDataSourceWidget))
}
