import React, { forwardRef, TableHTMLAttributes } from 'react'
import classNames from 'classnames'

type TableHeaderProps = TableHTMLAttributes<HTMLTableSectionElement>

export const TableHeader = forwardRef<HTMLTableSectionElement, TableHeaderProps>(({
    children,
    className,
    ...props
}, ref) => {
    const classes = classNames('header', className)

    return (
        <thead
            ref={ref}
            className={classes}
            {...props}
        >
            {children}
        </thead>
    )
})

TableHeader.displayName = 'TableHeader'
