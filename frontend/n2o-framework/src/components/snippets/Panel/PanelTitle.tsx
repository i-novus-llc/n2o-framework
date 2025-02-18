import React from 'react'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { type PanelTitleProps } from './types'

/**
 * Компонент заголовка для {@link Panel}
 * @reactProps (string) icon - класс для иконки
 * @reactProps {node} children - элемент вставляемый в PanelTitle
 * @reactProps {function} toggleCollapse - открывает/закрывает панель
 */

export function PanelTitle({ icon, children, toggleCollapse }: PanelTitleProps) {
    return (
        <a className="n2o-panel-region__panel-title" onClick={toggleCollapse}>
            {icon && <Icon name={icon} className="p-0 mr-2" />}
            {children}
        </a>
    )
}

export default PanelTitle
