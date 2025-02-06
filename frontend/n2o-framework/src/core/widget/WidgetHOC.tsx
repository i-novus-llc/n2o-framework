import React, { useEffect, ComponentType } from 'react'

import { registerDependency } from '../../actions/dependency'
import { type Widget } from '../../ducks/widgets/Widgets'

import { WithDataSource } from './WithDataSource'
import { withRedux } from './withRedux'
import { FETCH_TYPE } from './const'

/**
 * ХОК для получения параметров в виджет из DataSource и методов взаимодействия с ним
 */
export const WidgetHOC = <P extends Widget>(WidgetComponent: ComponentType<P>) => {
    const Widget = (props: P) => {
        const {
            id,
            dependency,
            dispatch,
            visible = true,
            fetchOnInit = true,
            fetchOnVisibility = true,
            fetch = FETCH_TYPE.always,
            isInit = false,
        } = props

        useEffect(() => {
            dispatch?.(registerDependency(id, dependency))
        }, [id, dispatch, dependency])

        if (!visible) { return null }

        return (
            <WidgetComponent
                {...props}
                id={id}
                dependency={dependency}
                dispatch={dispatch}
                fetchOnInit={fetchOnInit}
                fetchOnVisibility={fetchOnVisibility}
                fetch={fetch}
                isInit={isInit}
            />
        )
    }

    const WithDataSourceWidget = WithDataSource<P>(Widget) as ComponentType<Widget>

    return withRedux(WithDataSourceWidget)
}
