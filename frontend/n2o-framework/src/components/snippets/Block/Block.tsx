import React from 'react'
import cn from 'classnames'

export interface IBlockProps<TTag = HTMLDivElement> extends React.HTMLProps<TTag> {
    tag?: keyof React.ReactHTML
    disabled?: boolean
}

// TODO сюда можно вынести повторяющуюся логику (visible, disable и тд)
export function Block<TTag>({
    tag = 'div',
    className,
    disabled,
    ...props
}: IBlockProps<TTag>) {
    const classes = cn('block', className, {
        'n2o-disabled': disabled,
    })

    return React.createElement(tag, { className: classes, ...props })
}
