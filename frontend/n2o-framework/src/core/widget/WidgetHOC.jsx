import React, { useEffect } from 'react'

import { registerDependency } from '../../actions/dependency'

import { WithDataSource } from './WithDataSource'
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
            visible = true,
        } = props

        useEffect(() => {
            dispatch(registerDependency(id, dependency))
        }, [id, dispatch, dependency])

        if (!visible) {
            return null
        }

        return <WidgetComponent {...props} />
    }

    Widget.propTypes = widgetPropTypes

    const WithDataSourceWidget = WithDataSource(Widget)

    WithDataSourceWidget.defaultProps = {
        fetchOnInit: true,
        fetchOnVisibility: true,
        fetch: FETCH_TYPE.always,
        visible: true,
        isInit: false,
    }

    return withRedux(WithDataSourceWidget)
}
