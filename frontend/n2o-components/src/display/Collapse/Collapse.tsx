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

export interface Props {
    className?: string
    children: ReactNode
    collapsible?: boolean
    defaultActiveKey?: string | null
    destroyInactivePanel?: boolean
    accordion?: boolean
    dataKey?: string
    isVisible?: boolean
    onChange?(): void
}

export const Collapse = ({
    className = '',
    children,
    collapsible = true,
    destroyInactivePanel = false,
    accordion = false,
    dataKey = 'items',
    isVisible = true,
    onChange = () => {},
    ...rest
}: Props) => {
    return (
        <CollapseBody
            className={classNames('n2o-collapse', className)}
            expandIcon={(props: { isActive: boolean }) => renderIcon(props, collapsible)}
            destroyInactivePanel={destroyInactivePanel}
            accordion={accordion}
            dataKey={dataKey}
            isVisible={isVisible}
            onChange={onChange}
            {...rest}
        >
            {children}
        </CollapseBody>
    )
}

Collapse.displayName = '@n2o-components/display/Collapse'

export default Collapse
