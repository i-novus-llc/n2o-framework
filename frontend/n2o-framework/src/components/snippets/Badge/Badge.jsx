import React from 'react'
import cx from 'classnames'

export const renderBadge = ({
    badge,
    badgeColor = 'light',
    icon,
    title,
}) => (
    (badge || typeof badge === 'number') && (title
        ? (
            <span
                className={
                    cx(
                        `ml-1 rounded-pill badge badge-${badgeColor}`,
                    )}
            >
                {badge}
            </span>
        )
        : icon && (
            <span
                className={
                    cx(
                        `n2o-counter badge badge-${badgeColor}`,
                        badge !== ' ' ? 'n2o-badge-counter' : 'n2o-badge-dot',
                    )}
            >
                {badge}
            </span>
        )))
