import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'
import { compose, pure } from 'recompose'
import { createStructuredSelector } from 'reselect'

import propsResolver from '../../../utils/propsResolver'
import { registerColumn } from '../../../ducks/columns/store'
import SecurityCheck from '../../../core/auth/SecurityCheck'
import {
    isInitSelector,
    isVisibleSelector,
    isDisabledSelector,
} from '../../../ducks/columns/selectors'

/**
 * колонка-контейнер
 */
const withColumn = (WrappedComponent) => {
    class ColumnContainer extends React.Component {
        constructor(props) {
            super(props)
            this.getPassProps = this.getPassProps.bind(this)
            this.initIfNeeded()
        }

        /**
     * Диспатч экшена регистрации виджета
     */
        initIfNeeded() {
            const {
                columnId,
                widgetId,
                label,
                columnIsInit,
                columnVisible,
                columnDisabled,
                dispatch,
                conditions,
            } = this.props

            // eslint-disable-next-line no-unused-expressions
            !columnIsInit &&
        dispatch(
            registerColumn(
                widgetId,
                columnId,
                label,
                columnVisible,
                columnDisabled,
                conditions,
            ),
        )
        }

        getPassProps() {
            return omit(this.props, [
                'columnIsInit',
                'columnVisible',
                'columnDisabled',
                'security',
            ])
        }

        /**
     *Базовый рендер
     */
        render() {
            const { columnVisible, columnDisabled, security, model } = this.props
            const cellEl = (
                <WrappedComponent
                    disabled={columnDisabled}
                    {...propsResolver(this.getPassProps(), model, ['toolbar'])}
                />
            )

            return (columnVisible || null) && isEmpty(security) ? (
                cellEl
            ) : (
                <SecurityCheck
                    config={security}
                    render={({ permissions }) => (permissions ? cellEl : null)}
                />
            )
        }
    }

    const mapStateToProps = createStructuredSelector({
        columnIsInit: (state, props) => isInitSelector(props.widgetId, props.columnId)(state),
        columnVisible: (state, props) => isVisibleSelector(props.widgetId, props.columnId)(state),
        columnDisabled: (state, props) => isDisabledSelector(props.widgetId, props.columnId)(state),
    })

    ColumnContainer.propTypes = {
        columnId: PropTypes.string,
        widgetId: PropTypes.string,
        label: PropTypes.string,
        columnIsInit: PropTypes.any,
        columnVisible: PropTypes.bool,
        columnDisabled: PropTypes.bool,
        dispatch: PropTypes.func,
        conditions: PropTypes.object,
        security: PropTypes.object,
        model: PropTypes.object,
    }

    ColumnContainer.defaultProps = {
        columnVisible: true,
        columnDisabled: false,
    }

    return compose(
        connect(mapStateToProps),
        pure,
    )(ColumnContainer)
}

export default withColumn
