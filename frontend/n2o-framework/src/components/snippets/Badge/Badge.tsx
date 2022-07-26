import React from 'react'
import type { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'
import { Badge } from 'reactstrap'

import { Position, Shape } from './enums'
import { isBadgeLeftPosition, isBadgeRightPosition } from './utils'

export type BadgeOptions = Partial<{
    children: ReactNode;
    text: string | number | ReactNode;
    color: string;
    position: Position;
    shape: Shape;
    image: string;
    imagePosition: Position;
    imageShape: Shape;
    hasMargin: boolean;
    className: string;
    style: CSSProperties;
}>

export const renderBadge = (options: BadgeOptions) => {
    const {
        children,
        text,
        color = 'light',
        position = Position.Right,
        shape = Shape.Circle,
        image,
        imagePosition = Position.Left,
        imageShape = Shape.Circle,
        hasMargin = true,
        className,
        style,
    } = options

    const badgeContainerClassNames = classNames('n2o-badge-container', {
        'flex-row-reverse': isBadgeLeftPosition(position),
    })

    const badgeClassNames = classNames('n2o-badge', className, {
        [isBadgeRightPosition(position) ? 'ml-1' : 'mr-1']: hasMargin,
        'rounded-pill': shape === Shape.Rounded,
        'n2o-badge_circle rounded-pill': shape === Shape.Circle,
    })

    const badgeImageClassNames = classNames('n2o-badge-image', {
        'n2o-badge-image_right': imagePosition === Position.Right,
        'rounded-pill': imageShape === Shape.Circle || imageShape === Shape.Rounded,
    })

    const BadgeComponent = () => (
        <Badge
            color={color}
            className={badgeClassNames}
            style={style}
        >
            {image && (
                <img
                    src={image}
                    alt="not found"
                    className={badgeImageClassNames}
                />
            )}
            {text}
        </Badge>
    )

    if (!children) {
        return <BadgeComponent />
    }

    return (
        <div className={badgeContainerClassNames}>
            {children}
            <BadgeComponent />
        </div>
    )
}

export const renderSquareBadge = (options: BadgeOptions) => renderBadge({
    ...options,
    shape: options.shape || Shape.Square,
    hasMargin: false,
})
