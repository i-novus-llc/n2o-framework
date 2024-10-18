import React, { Component } from 'react'
import { ReactReduxContext, connect } from 'react-redux'

import { makeFieldParam } from '../../ducks/form/selectors'
import { DEPENDENCY_TYPES } from '../dependencyTypes'

import { FETCH_TRIGGER } from './constants'

// FIXME временное решение для fieldDependency type fetch, вызывает _fetchData компонента
//  config формируется в файле ReduxField
//  FETCH_TRIGGER подкладывает saga fieldDependency
/**
 * @type {Function}
 */
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

    FetchDependency.contextType = ReactReduxContext

    return connect((state, props) => {
        const { form, name } = props

        return {
            fetchTrigger: makeFieldParam(form, name, FETCH_TRIGGER)(state),
        }
    })(FetchDependency)
}
