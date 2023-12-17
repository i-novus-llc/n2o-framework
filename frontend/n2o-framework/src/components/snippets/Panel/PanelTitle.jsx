import React from 'react'
import PropTypes from 'prop-types'

import { Icon } from '../Icon/Icon'

/**
 * Компонент заголовка для {@link Panel}
 * @reactProps (string) icon - класс для иконки
 * @reactProps {node} children - элемент вставляемый в PanelTitle
 * @reactProps {function} toggleCollapse - открывает/закрывает панель
 */

function PanelTitle({ icon, children, toggleCollapse }) {
    return (
        <a className="n2o-panel-region__panel-title" onClick={toggleCollapse}>
            {icon && <Icon name={icon} className="p-0 mr-2" />}
            {children}
        </a>
    )
}

PanelTitle.propTypes = {
    icon: PropTypes.string,
    children: PropTypes.node,
}

export default PanelTitle
