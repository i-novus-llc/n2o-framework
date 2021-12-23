import React, { Component } from 'react'
import { connect } from 'react-redux'

import {
    registerWidget,
    setActive,
} from '../../ducks/widgets/store'
import {
    makeWidgetByIdSelector,
} from '../../ducks/widgets/selectors'
import { removeAllAlerts } from '../../ducks/alerts/store'

import { reduxTypes } from './propTypes'

/**
 * ХОК для подключения виджета к redux
 */
export const withRedux = (WidgetComponent) => {
    class ConnectedWidget extends Component {
        componentDidMount() {
            const { dispatch, ...props } = this.props
            const { isInit, id } = props

            if (!isInit) {
                dispatch(registerWidget(id, props))
            }
        }

        componentWillUnmount() {
            const { id, dispatch } = this.props

            dispatch(removeAllAlerts(id))
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
        (state, props) => ({
            ...props,
            ...makeWidgetByIdSelector(props.id)(state),
        }),
        (dispatch, props) => ({
            dispatch,
            setActive() {
                dispatch(setActive(props.id))
            },
        }),
    )(ConnectedWidget)
}
