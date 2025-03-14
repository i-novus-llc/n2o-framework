import React, { ReactNode } from 'react'
// @ts-ignore import from js file
import CollapseBody from 'rc-collapse'
import classNames from 'classnames'

import { Icon } from '../Icon'

const renderIcon = ({ isActive }: { isActive: boolean }, collapsible: boolean) => {
    if (!collapsible) { return null }

    return (
        <div className="n2o-collapse-icon-wrapper">
            <Icon
                className={classNames('n2o-collapse-icon', { isActive })}
                name="fa fa-angle-right"
            />
        </div>
    )
}

/**
 * Компонент Collapse
 * @param {string | array} activeKey - активный ключ панели (При совпадении с ключами Panel происходит открытие последней)
 * @param {boolean} collapsible - флаг выключения возможности сворачивания
 * @returns {*}
 * @constructor
 */

export type Props = {
    className: string
    children: ReactNode
    collapsible: boolean
    defaultActiveKey: string | null
}

export const Collapse = ({ className, children, collapsible, ...rest }: Props) => {
    return (
        <CollapseBody
            className={classNames('n2o-collapse', className)}
            expandIcon={(props: { isActive: boolean }) => renderIcon(props, collapsible)}
            {...rest}
        >
            {children}
        </CollapseBody>
    )
}

Collapse.defaultProps = {
    destroyInactivePanel: false,
    accordion: false,
    dataKey: 'items',
    collapsible: true,
    isVisible: true,
    onChange: () => {},
}

Collapse.displayName = '@n2o-components/display/Collapse'

export default Collapse
