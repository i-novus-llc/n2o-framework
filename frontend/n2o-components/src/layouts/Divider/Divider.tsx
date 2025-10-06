import React, { CSSProperties } from 'react'
import classNames from 'classnames'

import { ROOT_CLASS_NAME_PARAM } from '../../navigation/helpers'

export enum TYPE {
    HORIZONTAL = 'horizontal',
    VERTICAL = 'vertical',
}

export enum VARIANT {
    SOLID = 'solid',
    DASHED = 'dashed',
    DOTTED = 'dotted',
}

export interface DividerProps {
    className?: string
    style?: CSSProperties
    type?: TYPE
    color?: string
    thickness?: number | string
    variant?: VARIANT
    gap?: string | null
    rootClassName?: string
}

function getThickness(thickness: number | string): string {
    return typeof thickness === 'number' ? `${thickness}px` : thickness
}

/** @INFO Контейнер должен быть d-flex **/
export function Divider({
    className,
    style,
    color,
    gap = null,
    thickness = 1,
    variant = VARIANT.SOLID,
    type = TYPE.HORIZONTAL,
    [ROOT_CLASS_NAME_PARAM]: rootClassName,
}: DividerProps) {
    const computedStyle: CSSProperties = {
        flexShrink: 0,
        ...(gap && { margin: type === TYPE.HORIZONTAL ? `${gap} 0` : `0 ${gap}` }),
        ...(type === TYPE.HORIZONTAL
            ? {
                width: '100%',
                borderTop: `${getThickness(thickness)} ${variant} ${color || 'currentColor'}`,
            }
            : {
                height: '100%',
                borderLeft: `${getThickness(thickness)} ${variant} ${color || 'currentColor'}`,
            }),
        ...style,
    }

    return <div className={classNames('divider', className, rootClassName)} style={computedStyle} />
}
