import React, { ComponentProps, isValidElement, ReactNode, cloneElement, ReactElement } from 'react'
import classNames from 'classnames'

type Props = ComponentProps<'i'> & {
    name?: ReactNode | string
    disabled?: boolean
}

export function Icon({ name, className, disabled = false, ...props }: Props) {
    if (!name) { return null }

    if (isValidElement(name)) {
        const element = name as ReactElement

        return cloneElement(element, {
            className: classNames(element.props.className, className, { disabled }),
        })
    }

    return (
        <i
            className={classNames(
                'n2o-icon',
                name,
                className,
                { disabled },
            )}
            {...props}
        />
    )
}
