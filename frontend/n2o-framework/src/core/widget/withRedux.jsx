import React, { Component } from 'react'
import { connect } from 'react-redux'
import { batchActions } from 'redux-batched-actions'

import {
    registerWidget,
    setTableSelectedId,
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
                dispatch(batchActions([
                    registerWidget(id, props),
                    // addWidget(datasource, id), // FIXME раскомментировать после того как бек начнёт присылать datasource отдельной сущностью
                ]))
            }
        }

        componentWillUnmount() {
            const { id, dispatch } = this.props

            dispatch(batchActions([
                removeAllAlerts(id),
                setTableSelectedId(id, null),
            ]))
        }

        render() {
            return (
                <WidgetComponent
                    {...this.props}
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
        dispatch => ({ dispatch }),
    )(ConnectedWidget)
}
