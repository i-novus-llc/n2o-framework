import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'

import SecurityCheck from '../auth/SecurityCheck'

export function Factory(props, context) {
    const { src, level, security, children, ...rest } = props
    let { component = null } = props

    if (!component) {
        component = context.getComponent(src, level)
    }

    if (component) {
        return isEmpty(security) ? (
            React.createElement(component, rest, children)
        ) : (
            <SecurityCheck
                config={security}
                render={({ permissions }) => (permissions
                    ? React.createElement(component, rest, children)
                    : null)}
            />
        )
    }
    // eslint-disable-next-line no-console
    console.error(`Фабрике не удалось найти компонент: ${src} в ${level}`)

    return null
}

Factory.propTypes = {
    src: PropTypes.string,
    level: PropTypes.string,
    security: PropTypes.object,
    children: PropTypes.any,
    component: PropTypes.any,
}

Factory.contextTypes = {
    getComponent: PropTypes.func,
}

export default Factory
