import React from 'react'

import type { DropdownItemProps } from '../types'
import { Column } from '../Column'

import { MultiColumnLabel } from './MultiColumnLabel'

export function Stubs({ count }: { count: number }) {
    return (
        <>
            {Array.from({ length: count }, (_, index) => (
                <div key={index} className="n2o-multi-toggle-column__stub" />
            ))}
        </>
    )
}

type ChildrenColumnProps = DropdownItemProps & { level: number }

export function ChildrenColumn({ item, level, onClick }: ChildrenColumnProps) {
    const { columnId, visibleState, parentId } = item

    const handleClick = () => {
        onClick?.(columnId, visibleState, parentId)
    }

    return (
        <section key={item.columnId} className="d-flex n2o-multi-toggle-column__item" onClick={handleClick}>
            <Stubs count={level} />
            <Column item={item} />
        </section>
    )
}

export interface ParentColumnProps {
    onClick(): void
    label?: string
    level: number
    checked: boolean
    indeterminate: boolean
    enabled: boolean
}

export function ParentColumn({
    onClick, level, label, checked, indeterminate, enabled,
}: ParentColumnProps) {
    return (
        <section className="d-flex n2o-multi-toggle-column__parent-item" onClick={onClick}>
            <Stubs count={level - 1} />
            <MultiColumnLabel
                label={label}
                level={level}
                checked={checked}
                indeterminate={indeterminate}
                enabled={enabled}
                open
            />
        </section>
    )
}
