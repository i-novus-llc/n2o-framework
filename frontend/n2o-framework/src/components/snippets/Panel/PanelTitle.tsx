import React, { ReactNode } from 'react'

import { Icon } from '../Icon/Icon'

export interface Props {
    icon: string
    children: ReactNode
    toggleCollapse(): void
}

/**
 * Компонент заголовка для {@link Panel}
 * @reactProps (string) icon - класс для иконки
 * @reactProps {node} children - элемент вставляемый в PanelTitle
 * @reactProps {function} toggleCollapse - открывает/закрывает панель
 */

function PanelTitle({ icon, children, toggleCollapse }: Props) {
    return (
        <a className="n2o-panel-region__panel-title" onClick={toggleCollapse}>
            {icon && <Icon name={icon} className="p-0 mr-2" />}
            {children}
        </a>
    )
}

export default PanelTitle
