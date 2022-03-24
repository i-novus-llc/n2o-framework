import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Нав Таба
 * @reactProps {string} className - css-класс
 * @reactProps {object} style - стили
 * @reactProps {node} children - элемент потомок компонента TabNav
 */
export function TabNav({ className, style, children }) {
    return (
        <ul className={classNames('nav', 'nav-tabs', className)} style={{ ...style }}>
            {children}
        </ul>
    )
}

TabNav.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    children: PropTypes.node,
}

export default TabNav
