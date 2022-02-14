import React, { Component } from 'react'
import PropTypes from 'prop-types'
import { ReactReduxContext } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import isFunction from 'lodash/isFunction'
import map from 'lodash/map'

import observeStore from '../../utils/observeStore'
import { setWatchDependency } from '../../components/widgets/Form/utils'
import { DEPENDENCY_TYPES } from '../dependencyTypes'

export default config => (WrappedComponent) => {
    class ReRenderComponent extends Component {
        constructor(props) {
            super(props)

            this.observers = []

            this.observeState = this.observeState.bind(this)
            this.setComponentRef = this.setComponentRef.bind(this)
            this.fetchDependencyAction = this.fetchDependencyAction.bind(this)
            this.reRenderDependencyAction = this.reRenderDependencyAction.bind(this)
            this.setObserveState = this.setObserveState.bind(this)
            this.unsubscribe = this.unsubscribe.bind(this)
            this.setReRenderRef = this.setReRenderRef.bind(this)
        }

        componentDidMount() {
            this.dependencyActions = {
                fetch: this.fetchDependencyAction,
                reRender: this.reRenderDependencyAction,
            }

            this.setObserveState()
        }

        componentDidUpdate(prevProps) {
            const { disabled } = this.props

            if (prevProps.disabled !== disabled) {
                config.onChange.call(this.componentRef, this.props)
            }
        }

        componentWillUnmount() {
            if (!isEmpty(this.observers)) {
                this.unsubscribe()
            }
        }

        fetchDependencyAction() {
            config.onChange.apply(this.componentRef, [
                this.props,
                DEPENDENCY_TYPES.fetch,
            ])
        }

        reRenderDependencyAction() {
            if (this.reRenderRef) {
                this.reRenderRef.forceUpdate()
            }
            if (isFunction(config.onChange)) {
                config.onChange.apply(this.componentRef, [this.props])
            }
        }

        setComponentRef(el) {
            this.componentRef = el
        }

        setReRenderRef(el) {
            this.reRenderRef = el
        }

        observeState(dependencyType, dependencyAction) {
            const { store } = this.context
            const { dependencySelector } = this.props

            return observeStore(
                store,
                state => dependencySelector(state, this.props, dependencyType),
                dependencyAction,
            )
        }

        setObserveState() {
            const { dependency } = this.props

            map(dependency, (d) => {
                if (this.dependencyActions[d.type]) {
                    const observer = this.observeState(
                        d.type,
                        this.dependencyActions[d.type],
                    )

                    this.observers.push(observer)
                }
            })
        }

        unsubscribe() {
            map(this.observers, o => o())
        }

        render() {
            return (
                <WrappedComponent
                    {...this.props}
                    setReRenderRef={this.setReRenderRef}
                    ref={this.setComponentRef}
                />
            )
        }
    }

    ReRenderComponent.propTypes = {
        dependencySelector: PropTypes.func,
        dependency: PropTypes.any,
        disabled: PropTypes.bool,
    }

    ReRenderComponent.defaultProps = {
        dependencySelector: setWatchDependency,
    }

    ReRenderComponent.contextType = ReactReduxContext

    return ReRenderComponent
}
