import React from 'react'
import PropTypes from 'prop-types'
import BaseCollapse from 'rc-collapse'
import classNames from 'classnames'
import map from 'lodash/map'

import { Icon } from '../Icon/Icon'

import { Panel } from './Panel'

// eslint-disable-next-line react/prop-types
const expandIcon = ({ isActive }, collapsible) => (collapsible ? (
    <div className="n2o-collapse-icon-wrapper">
        <Icon
            className={classNames('n2o-collapse-icon', { isActive })}
            name="fa fa-angle-right"
        />
    </div>
) : null)

/**
 * Компонент Collapse
 * @param {string | array} activeKey - активный ключ панели (При совпадении с ключами Panel происходит открытие последней)
 * @param {string | array} defaultActiveKey - активный ключ по умолчанию
 * @param {boolean} destroyInactivePanel - при закрытии панели удалить внутреннее содержимое.
 * @param {boolean} accordion - включить режим accordion (При открытии панели захлопнуть предыдущую панель)
 * @param {boolean} collapsible - флаг выключения возможности сворачивания
 * @returns {*}
 * @constructor
 */

export const Collapse = ({ className, children, dataKey, collapsible, ...rest }) => {
    // eslint-disable-next-line react/prop-types
    const renderPanels = ({ text, ...panelProps }) => (
        <Panel {...panelProps}>{text}</Panel>
    )

    return (
        <BaseCollapse
            className={classNames('n2o-collapse', className)}
            expandIcon={props => expandIcon(props, collapsible)}
            {...rest}
        >
            {children || map(rest[dataKey], renderPanels)}
        </BaseCollapse>
    )
}

Collapse.propTypes = {
    /**
     * Массив ключей открытых панелей
     */
    activeKey: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
    /**
     * Массив ключей открытых по дефолту панелей
     */
    defaultActiveKey: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
    destroyInactivePanel: PropTypes.bool,
    /**
     * Флаг включения режима 'Аккордион' (При открытии панели захлопнуть предыдущую панель)
     */
    accordion: PropTypes.bool,
    children: PropTypes.node,
    className: PropTypes.string,
    /**
     * Callback на открытие/закрытие панелей
     */
    onChange: PropTypes.func,
    /**
     * Ключ для рендера панелей
     */
    dataKey: PropTypes.string,
    /**
     * Флаг выключения возможности сворачивания
     */
    collapsible: PropTypes.bool,
}

Collapse.defaultProps = {
    destroyInactivePanel: false,
    accordion: false,
    dataKey: 'items',
    collapsible: true,
    // eslint-disable-next-line react/default-props-match-prop-types
    isVisible: true,
}

export { Panel }
export default Collapse
