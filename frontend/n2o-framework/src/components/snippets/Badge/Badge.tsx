import React from 'react'
// @ts-ignore
import classNames from 'classnames'
import { Badge } from 'reactstrap'

import { Position, Shape } from './enums'

type Options = {
    badge: string | number;
    badgeColor: string;
    badgePosition: Position;
    badgeShape: Shape;
    image?: string | null;
    imagePosition: Position;
    imageShape: Shape;
    hasMargin: boolean;
    className: string;
}

export const renderBadge = (options: Options) => {
    const {
        badge,
        badgeColor = 'light',
        badgePosition = Position.Right,
        badgeShape = Shape.Circle,
        image,
        imagePosition = Position.Left,
        imageShape = Shape.Circle,
        hasMargin = true,
        className,
    } = options

    const badgeClassNames = classNames('n2o-badge', className, {
        [badgePosition === Position.Right ? 'ml-1' : 'mr-1']: hasMargin,
        'rounded-pill': badgeShape === Shape.Rounded,
        'n2o-badge_circle rounded-pill': badgeShape === Shape.Circle,
    })

    const badgeImageClassNames = classNames('n2o-badge-image', {
        'n2o-badge-image_right': imagePosition === Position.Right,
        'rounded-pill': imageShape === Shape.Circle || imageShape === Shape.Rounded,
    })

    return (
        <Badge
            color={badgeColor}
            className={badgeClassNames}
        >
            {image && (
                <img
                    src={image}
                    alt="not found"
                    className={badgeImageClassNames}
                />
            )}
            {badge}
        </Badge>
    )
}

export const renderSquareBadge = (options: Options) => renderBadge({
    ...options,
    badgeShape: options.badgeShape || Shape.Square,
    hasMargin: false,
})
