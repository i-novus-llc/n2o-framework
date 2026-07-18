import React, { forwardRef } from 'react'
import classNames from 'classnames'

import { Icon } from '../../display/Icon'

export interface SelectControlsProps {
    onClear?(): void
    onToggle?(): void
    clear?: string | null
    toggle?: string | null
    className?: string
}

export const SelectControls = forwardRef<HTMLDivElement, SelectControlsProps>(
    ({ className, clear, toggle, onClear, onToggle }, ref) => {
        return (
            <div className={classNames('select-controls', className)} ref={ref}>
                <Icon name={clear} className="clear" onClick={onClear} />
                <Icon name={toggle} className="toggle" onClick={onToggle} />
            </div>
        )
    },
)

SelectControls.displayName = 'SelectControls'
