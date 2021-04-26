import React, { Component } from 'react'
import PropTypes from 'prop-types'
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

            this._observers = []

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

        componentDidUpdate(prevProps, prevState, snapshot) {
            if (prevProps.disabled !== this.props.disabled) {
                config.onChange.call(this._componentRef, this.props)
            }
        }

        componentWillUnmount() {
            if (!isEmpty(this._observers)) {
                this.unsubscribe()
            }
        }

        fetchDependencyAction() {
            config.onChange.apply(this._componentRef, [
                this.props,
                DEPENDENCY_TYPES.fetch,
            ])
        }

        reRenderDependencyAction() {
            this._reRenderRef && this._reRenderRef.forceUpdate()
            if (isFunction(config.onChange)) {
                config.onChange.apply(this._componentRef, [this.props])
            }
        }

        setComponentRef(el) {
            this._componentRef = el
        }

        setReRenderRef(el) {
            this._reRenderRef = el
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
                    this._observers.push(observer)
                }
            })
        }

        unsubscribe() {
            map(this._observers, o => o())
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
    }

    ReRenderComponent.defaultProps = {
        dependencySelector: setWatchDependency,
    }

    ReRenderComponent.contextTypes = {
        store: PropTypes.object,
    }

    return ReRenderComponent
}
