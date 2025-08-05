import React, { MouseEvent } from 'react'
import { DropdownItem as Component } from 'reactstrap'
import classNames from 'classnames'

import { NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { getLabel } from './helpers'
import { type DropdownItemProps } from './types'
import { ToggleColumnCheckbox } from './ToggleColumnCheckbox'

export const Column = ({ item, onClick, onMouseDown = NOOP_FUNCTION, elementAttributes }: DropdownItemProps) => {
    const { columnId, label, parentId, icon, visibleState, enabled } = item

    const handleClick = (e: MouseEvent) => {
        if (!onClick) {
            return
        }

        e.stopPropagation()
        e.preventDefault()
        onClick?.(columnId, visibleState, parentId)
    }

    return (
        <Component
            key={columnId}
            toggle={false}
            onClick={handleClick}
            disabled={!enabled}
            onMouseDown={onMouseDown}
            {...elementAttributes}
        >
            <section className="n2o-toggle-column__column">
                <ToggleColumnCheckbox
                    checked={visibleState}
                    label=''
                    enabled={enabled}
                />
                {icon && <i className={classNames(icon, 'n2o-toggle-column__column-icon')} />}
                <span>{getLabel(columnId, label, icon)}</span>
            </section>
        </Component>
    )
}
