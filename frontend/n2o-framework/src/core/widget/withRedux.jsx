import React, { Component } from 'react'
import { connect } from 'react-redux'

import {
    registerWidget,
    removeWidget,
    setActive,
} from '../../ducks/widgets/store'
import {
    makeWidgetByIdSelector,
} from '../../ducks/widgets/selectors'
import { removeAllAlerts } from '../../ducks/alerts/store'
import { getModelSelector } from '../../ducks/models/selectors'
import { ModelPrefix } from '../datasource/const'

import { reduxTypes } from './propTypes'

/**
 * ХОК для подключения виджета к redux
 */
/**
 * @type {Function}
 */
export const withRedux = (WidgetComponent) => {
    /**
     * @type ConnectedWidget
     */
    class ConnectedWidget extends Component {
        componentDidMount() {
            const { dispatch, ...props } = this.props
            const { isInit, id, dependency, visible: propsVisible } = props
            const visible = typeof propsVisible === 'undefined' ? true : propsVisible

            if (!isInit) {
                dispatch(registerWidget(id, {
                    ...props,
                    visible: dependency?.visible?.length ? false : visible,
                }))
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

            return (
                <WidgetComponent
                    {...props}
                    onFocus={setActive}
                />
            )
        }
    }

    ConnectedWidget.propTypes = reduxTypes

    return connect(
        /**
         * @param {object} state
         * @param {WidgetInitialTypes} props
         * @return {WidgetReduxTypes}
         */
        (state, props) => {
            const { parent, dependency, datasource } = props
            const reduxProps = makeWidgetByIdSelector(props.id)(state)
            const model = getModelSelector(`models.${ModelPrefix.source}.${datasource}`)(state) || []
            const datasourceModelLength = model.length

            /* FIXME костыль для табов, нужно пересмотреть */
            if (!parent || dependency) { return { ...props, ...reduxProps, datasourceModelLength } }

            const { fetch } = props

            return { ...props, ...reduxProps, fetch }
        },
        (dispatch, props) => ({
            dispatch,
            setActive() {
                dispatch(setActive(props.id))
            },
        }),
    )(ConnectedWidget)
}
