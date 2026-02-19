import React from 'react'
import classNames from 'classnames'

import { type HeaderProps } from './types'

export const Header = ({
    header,
    headerClass,
    showArrow,
    expandIcon,
    extra,
    hasSeparator,
    isActive,
    isDisabled,
    onClick,
    collapsible,
}: HeaderProps) => (
    <div
        className={classNames('collapse-panel-header', headerClass)}
        onClick={onClick}
        role="button"
        tabIndex={isDisabled ? -1 : 0}
        aria-expanded={isActive}
        aria-disabled={isDisabled}
    >
        {showArrow && expandIcon?.(isActive, collapsible)}
        <div className="n2o-panel-header-text collapse-panel-header-text">{header}</div>
        {extra && <div className="collapse-panel-extra">{extra}</div>}
        {hasSeparator && <div className="divider" />}
    </div>
)
