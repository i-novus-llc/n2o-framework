import React from 'react'
import classNames from 'classnames'

import '../styles/components/Panel.scss'
import { type PanelProps, Direction } from './types'
import { GroupChildren } from './helpers'

export function Panel({
    className,
    style,
    children,
    childrenClassName = 'direction-panel__children',
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
            <GroupChildren rootClassName={childrenClassName}>{children}</GroupChildren>
        </section>
    )
}

Panel.displayName = '@n2o-components/navigation/Panel'
