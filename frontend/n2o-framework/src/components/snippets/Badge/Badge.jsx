import React from 'react'
import classNames from 'classnames'
import { Badge } from 'reactstrap'

import { BadgePropTypes } from './propTypes'

export const renderBadge = (options) => {
    const {
        badge,
        badgeColor = 'light',
        badgePosition = 'right',
        badgeShape = 'circle',
        image,
        imagePosition = 'left',
        imageShape = 'circle',
        hasMargin = true,
        className,
    } = options

    const badgeClassNames = classNames('n2o-badge', className, {
        [badgePosition === 'right' ? 'ml-1' : 'mr-1']: hasMargin,
        'rounded-pill': badgeShape === 'rounded',
        'n2o-badge_circle rounded-pill': badgeShape === 'circle',
    })

    const badgeImageClassNames = classNames('n2o-badge-image', {
        'n2o-badge-image_right': imagePosition === 'right',
        'rounded-pill': imageShape === 'circle' || imageShape === 'rounded',
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

export const renderSquareBadge = options => renderBadge({
    ...options,
    badgeShape: options.badgeShape || 'square',
    hasMargin: false,
})

renderBadge.propTypes = BadgePropTypes
renderSquareBadge.propTypes = BadgePropTypes
