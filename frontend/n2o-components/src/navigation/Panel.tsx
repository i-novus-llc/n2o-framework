import React from 'react'
import classNames from 'classnames'

import '../styles/components/Panel.scss'
import { type PanelProps, Direction } from './types'
import { mapWithClassName } from './helpers'

export function Panel({
    className,
    style,
    children,
    direction = Direction.ROW,
}: PanelProps) {
    if (!children) { return null }

    return (
        <section
            className={classNames(className, 'direction-panel', {
                'direction-row': direction === Direction.ROW,
                'direction-col': direction === Direction.COLUMN,
            })}
            style={style}
        >
            {mapWithClassName(children)}
        </section>
    )
}

Panel.displayName = '@n2o-components/navigation/Panel'
