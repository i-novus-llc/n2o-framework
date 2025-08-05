import React, { useCallback } from 'react'
import classNames from 'classnames'

import { type HeaderCell } from '../../../../ducks/table/Table'
import { getVisibilityInfo, callbackRecursive } from '../helpers'
import { type MultiColumnProps } from '../types'

import { ParentColumn, ChildrenColumn } from './Stubs'

export function MultiColumn({ items, onClick, className, label, enabled, level = 1 }: MultiColumnProps) {
    const { checked, indeterminate } = getVisibilityInfo(items)

    const onChildrenClick = useCallback(
        (item: HeaderCell) => onClick?.(item.columnId, checked, item.parentId),
        [checked, onClick],
    )

    const onParentClick = useCallback(
        () => {
            if (!enabled) { return }

            callbackRecursive(items, onChildrenClick)
        },
        [enabled, items, onChildrenClick],
    )

    return (
        <section className={classNames('n2o-multi-toggle-column', `level-${level}`, className)}>
            <ParentColumn
                level={level}
                onClick={onParentClick}
                label={label}
                checked={checked}
                indeterminate={indeterminate}
                enabled={enabled}
            />
            <div className={classNames('n2o-multi-toggle-column-items', `level-${level}`)}>
                {items.map(item => (
                    item.children ? (
                        <MultiColumn
                            key={item.columnId}
                            items={item.children}
                            onClick={onClick}
                            label={item.label}
                            level={level + 1}
                            enabled={item.enabled}
                            className={className}
                        />
                    ) : <ChildrenColumn item={item} onClick={onClick} level={level} />
                ))}
            </div>
        </section>
    )
}
