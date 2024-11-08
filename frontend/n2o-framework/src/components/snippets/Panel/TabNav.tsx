import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'

export interface Props {
    className: string
    style: CSSProperties
    children: ReactNode
}

/**
 * Нав Таба
 * @reactProps {string} className - css-класс
 * @reactProps {object} style - стили
 * @reactProps {node} children - элемент потомок компонента TabNav
 */
export function TabNav({ className, style, children }: Props) {
    return <ul className={classNames('nav', 'nav-tabs', className)} style={{ ...style }}>{children}</ul>
}

export default TabNav
