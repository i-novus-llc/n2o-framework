import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { ReactReduxContext, connect } from 'react-redux'

import { makeFieldParam } from '../../ducks/form/selectors'
import { DEPENDENCY_TYPES } from '../dependencyTypes'

export const FETCH_TRIGGER = 'fetchTrigger'

// FIXME временное решение для fieldDependency type fetch, вызывает _fetchData компонента
//  config формируется в файле ReduxField
//  FETCH_TRIGGER подкладывает saga fieldDependency
export default config => (WrappedComponent) => {
    class FetchDependency extends Component {
        setComponentRef = (el) => {
            this.componentRef = el
        }

        componentDidUpdate(prevProps) {
            const { disabled, fetchTrigger } = this.props

            if (prevProps.fetchTrigger !== fetchTrigger) {
                config.onChange.apply(this.componentRef, [this.props, DEPENDENCY_TYPES.fetch])
            }

            if (prevProps.disabled !== disabled) {
                config.onChange.call(this.componentRef, this.props)
            }
        }

        render() {
            return <WrappedComponent {...this.props} ref={this.setComponentRef} />
        }
    }

    FetchDependency.propTypes = {
        disabled: PropTypes.bool,
        fetchTrigger: PropTypes.number,
    }

    FetchDependency.contextType = ReactReduxContext

    return connect((state, props) => {
        const { form, name } = props

        return {
            fetchTrigger: makeFieldParam(form, name, FETCH_TRIGGER)(state),
        }
    })(FetchDependency)
}
