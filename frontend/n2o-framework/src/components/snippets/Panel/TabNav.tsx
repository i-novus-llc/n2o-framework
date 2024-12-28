import React from 'react'
import classNames from 'classnames'

import { type TabNavProps } from './types'

/**
 * Нав Таба
 * @reactProps {string} className - css-класс
 * @reactProps {object} style - стили
 * @reactProps {node} children - элемент потомок компонента TabNav
 */
export function TabNav({ className, style, children }: TabNavProps) {
    return <ul className={classNames('nav', 'nav-tabs', className)} style={style}>{children}</ul>
}

export default TabNav
