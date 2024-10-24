import React from 'react'
import PropTypes from 'prop-types'

export function Factory(props, context) {
    const { src, level, security, children, ...rest } = props
    let { component = null } = props

    if (!component) { component = context.getComponent(src, level) }

    if (component) { return React.createElement(component, rest, children) }

    if (!src) { return null }

    console.error(`Фабрике не удалось найти компонент: ${src} в ${level}`)

    return null
}

Factory.contextTypes = { getComponent: PropTypes.func }

export default Factory
