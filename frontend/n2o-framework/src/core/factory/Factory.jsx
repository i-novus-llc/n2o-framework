import React, { useContext } from 'react'

import { FactoryContext } from './context'

export function Factory(props) {
    const { src, level, security, children, ...rest } = props
    let { component = null } = props

    const { getComponent } = useContext(FactoryContext)

    if (!component) { component = getComponent(src, level) }

    if (component) { return React.createElement(component, rest, children) }

    if (!src) { return null }

    console.error(`Фабрике не удалось найти компонент: ${src} в ${level}`)

    return null
}

export default Factory
