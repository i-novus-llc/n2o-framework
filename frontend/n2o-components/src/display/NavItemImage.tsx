import React from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'

export type ImageShape = 'circle' | 'rounded' | 'square' | ''

export type Props = TBaseProps & {
    imageShape?: ImageShape,
    imageSrc?: string,
    title?: string,
}

const shapeClasses = {
    circle: 'rounded-circle',
    rounded: 'rounded',
    square: 'square',
} as const

export const NavItemImage = ({
    imageSrc,
    imageShape = '',
    title,
}: Props) => {
    if (!imageSrc) { return null }

    const className = classNames(
        'mr-2 n2o-nav-image',
        imageShape && shapeClasses[imageShape],
    )

    return (
        <img
            className={className}
            src={imageSrc}
            alt={title}
            width="18"
        />
    )
}
