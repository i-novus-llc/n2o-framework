import React, { RefObject, useCallback } from 'react'
import classNames from 'classnames'

export type Id = number | string

export interface TagProps {
    id: Id
    value: string
    title?: string
    onRemove?(id: Id): void
    className?: string
    ref?: RefObject<HTMLDivElement>
}

export const Tag = ({
    id,
    value,
    onRemove,
    className,
    ref,
    title,
}: TagProps) => {
    const handleRemove = useCallback(() => { onRemove?.(id) }, [id, onRemove])

    return (
        <div
            tabIndex={-1}
            title={title}
            className={classNames('tag', className)}
            data-tag-id={id}
            ref={ref}
        >
            <span className="tag-value">{value}</span>
            {onRemove && <button type="button" className="tag-remove" onClick={handleRemove}>×</button>}
        </div>
    )
}
