import React, { Component, Children } from 'react'
import PropTypes from 'prop-types'
import first from 'lodash/first'
import each from 'lodash/each'
import isObject from 'lodash/isObject'
import isArray from 'lodash/isArray'
import isString from 'lodash/isString'
import values from 'lodash/values'

import { withErrorBoundary } from '../error/withErrorBoundary'

import factoryConfigShape from './factoryConfigShape'
import { NotFoundFactory } from './NotFoundFactory'
import { FactoryContext } from './context'

const ignoreList = ['dataProvider', 'action', 'actions']

export class FactoryProvider extends Component {
    getChildContext() {
        const { contextMethods } = this.state

        return contextMethods
    }

    constructor(props, context) {
        super(props, context)
        this.factories = props.config
        this.getComponent = this.getComponent.bind(this)
        this.resolveProps = this.resolveProps.bind(this)
        this.componentCache = new WeakMap()
        this.state = {
            contextMethods: {
                factories: this.factories,
                getComponent: this.getComponent,
                resolveProps: this.resolveProps,
            },
        }
    }

    withBoundary = (component = null) => {
        if (this.componentCache.has(component)) {
            return this.componentCache.get(component)
        }

        const WithErrorBoundary = withErrorBoundary(component)

        this.componentCache.set(component, WithErrorBoundary)

        return WithErrorBoundary
    }

    getComponent(src, level) {
        if (level && this.factories[level]?.[src]) {
            return this.withBoundary(this.factories[level][src])
        }
        const factories = []

        each(this.factories, (group) => {
            if (group && group[src]) {
                const comp = this.withBoundary(group[src])

                factories.push(comp)
            }
        })

        return first(factories)
    }

    resolveProps(
        props,
        defaultComponent = NotFoundFactory,
        paramName = 'component',
    ) {
        const obj = {}

        if (isObject(props)) {
            Object.keys(props).forEach((key) => {
                if (isObject(props[key]) && !ignoreList.includes(key)) {
                    obj[key] = this.resolveProps(props[key], defaultComponent, paramName)
                } else if (key === 'src') {
                    obj[paramName] = this.getComponent(props[key], null) || defaultComponent
                } else {
                    obj[key] = props[key]
                }
            })

            return isArray(props) ? values(obj) : obj
        }

        if (isString(props)) {
            return this.getComponent(props) || defaultComponent
        }

        return props
    }

    render() {
        const { children } = this.props
        const { contextMethods } = this.state

        return (
            <FactoryContext.Provider value={contextMethods}>
                {Children.only(children)}
            </FactoryContext.Provider>
        )
    }
}

FactoryProvider.propTypes = {
    config: factoryConfigShape.isRequired,
    children: PropTypes.element.isRequired,
}

FactoryProvider.childContextTypes = {
    factories: factoryConfigShape.isRequired,
    getComponent: PropTypes.func,
    resolveProps: PropTypes.func,
}

export default FactoryProvider
