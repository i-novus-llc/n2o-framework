import React from 'react'
import classNames from 'classnames'

interface Props {
    tag?: string
    className?: string
    disabled?: boolean
    forwardedRef?: React.Ref<HTMLDivElement>
    children?: React.ReactNode
}

export function DefaultCell({ className, forwardedRef, tag = 'div', disabled = false, ...rest }: Props) {
    const classnames = classNames('default-cell', className, { 'n2o-disabled': disabled })

    return React.createElement(tag, { ...rest, className: classnames, ref: forwardedRef })
}
