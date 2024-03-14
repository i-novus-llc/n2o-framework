import React, { ReactNode } from 'react'
import classNames from 'classnames'

export enum ICON_POSITIONS {
    LEFT = 'left',
    RIGHT = 'right',
}

function getContainerClass(className?: string, icon?: string, iconPosition?: ICON_POSITIONS) {
    const mainClassName = classNames('n2o-icon-container', className)

    if (!icon) { return mainClassName }

    return classNames(mainClassName, {
        'left-icon': icon && iconPosition === ICON_POSITIONS.LEFT,
        'right-icon': icon && iconPosition === ICON_POSITIONS.RIGHT,
    })
}

interface Props {
    className?: string
    icon?: string
    iconPosition?: ICON_POSITIONS
    children: ReactNode
}

export function IconContainer({ className, icon, iconPosition, children }: Props) {
    return <section className={getContainerClass(className, icon, iconPosition)}>{children}</section>
}
