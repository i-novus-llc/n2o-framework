import React, { useState, useMemo, useCallback, lazy, Suspense } from 'react'
import PropTypes from 'prop-types'
import isObject from 'lodash/isObject'
import isArray from 'lodash/isArray'
import isString from 'lodash/isString'
import values from 'lodash/values'

import { withErrorBoundary } from '../error/withErrorBoundary'

import factoryConfigShape from './factoryConfigShape'
import { NotFoundFactory } from './NotFoundFactory'
import { FactoryContext } from './context'

const ignoreList = ['dataProvider', 'action', 'actions']

export const FactoryProvider = ({ config, children }) => {
    const [factories] = useState(config)
    const componentCache = useMemo(() => new WeakMap(), [])

    const withBoundary = useCallback((Component = null) => {
        if (!Component) { return null }

        if (componentCache.has(Component)) {
            return componentCache.get(Component)
        }

        const WithErrorBoundary = withErrorBoundary(Component)

        componentCache.set(Component, WithErrorBoundary)

        return WithErrorBoundary
    }, [componentCache])

    const getComponent = useCallback((src, level) => {
        let module = null

        if (level && factories[level]?.[src]) {
            module = factories[level][src]
        } else {
            const factoryGroups = Object.values(factories)

            for (let i = 0; i < factoryGroups.length; i += 1) {
                const group = factoryGroups[i]

                if (group?.[src]) {
                    module = group[src]

                    break
                }
            }
        }

        if (!module) { return null }

        return withBoundary(module)
    }, [factories, withBoundary])

    const resolveProps = useCallback((
        props,
        defaultComponent = NotFoundFactory,
        paramName = 'component',
    ) => {
        const obj = {}

        if (isObject(props)) {
            Object.keys(props).forEach((key) => {
                if (isObject(props[key]) && !ignoreList.includes(key)) {
                    obj[key] = resolveProps(props[key], defaultComponent, paramName)
                } else if (key === 'src') {
                    obj[paramName] = getComponent(props[key], null) || defaultComponent
                } else {
                    obj[key] = props[key]
                }
            })

            return isArray(props) ? values(obj) : obj
        }

        if (isString(props)) {
            return getComponent(props) || defaultComponent
        }

        return props
    }, [getComponent])

    const contextMethods = useMemo(() => ({
        factories,
        getComponent,
        resolveProps,
    }), [factories, getComponent, resolveProps])

    return (
        <FactoryContext.Provider value={contextMethods}>
            {children}
        </FactoryContext.Provider>
    )
}

FactoryProvider.propTypes = {
    config: factoryConfigShape.isRequired,
    children: PropTypes.element.isRequired,
}
