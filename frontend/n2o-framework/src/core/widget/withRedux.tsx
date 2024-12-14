import React, { Component, ComponentType } from 'react'
import { connect } from 'react-redux'
import { Dispatch } from 'redux'

import { registerWidget, removeWidget, setActive } from '../../ducks/widgets/store'
import { makeWidgetByIdSelector } from '../../ducks/widgets/selectors'
import { removeAllAlerts } from '../../ducks/alerts/store'
import { getModelSelector } from '../../ducks/models/selectors'
import { ModelPrefix } from '../datasource/const'
import { Widget } from '../../ducks/widgets/Widgets'
import { State } from '../../ducks/State'

export interface ConnectedWidgetProps extends Widget {
    dispatch: Dispatch
    datasourceModelLength: number
    setActive(): void
}

/**
 * ХОК для подключения виджета к redux
 */
export const withRedux = (WidgetComponent: ComponentType<Widget & { onFocus(): void }>) => {
    class ConnectedWidget extends Component<ConnectedWidgetProps> {
        componentDidMount() {
            const { dispatch, ...props } = this.props
            const { isInit, id, dependency, visible: propsVisible } = props
            const visible = typeof propsVisible === 'undefined' ? true : propsVisible

            if (!isInit) {
                // @ts-ignore registerWidget TS2554: Expected 3 arguments, but got 2
                dispatch(registerWidget(id, { ...props, visible: dependency?.visible?.length ? false : visible }))
            }
        }

        componentWillUnmount() {
            const { id, dispatch, datasourceModelLength } = this.props

            dispatch(removeAllAlerts(id))

            const savedProps = datasourceModelLength > 0 ? { isInit: false, fetchOnInit: true } : null

            dispatch(removeWidget(id, savedProps))
        }

        render() {
            const { setActive, ...props } = this.props

            return <WidgetComponent {...props} onFocus={setActive} />
        }
    }

    return connect(
        (state: State, props: Widget) => {
            const { parent, dependency, datasource } = props
            // @ts-ignore FIXME makeWidgetByIdSelector type
            const reduxProps = makeWidgetByIdSelector(props.id)(state)
            const model = getModelSelector(`models.${ModelPrefix.source}.${datasource}`)(state) || []
            const datasourceModelLength = model.length

            /* FIXME костыль для табов, нужно пересмотреть */
            if (!parent || dependency) {
                return { ...props, ...reduxProps, datasourceModelLength }
            }

            const { fetch } = props

            return { ...props, ...reduxProps, fetch }
        },
        (dispatch: Dispatch, props: Widget) => ({
            dispatch,
            setActive() {
                dispatch(setActive(props.id))
            },
        }),
    )(ConnectedWidget as never)
}
