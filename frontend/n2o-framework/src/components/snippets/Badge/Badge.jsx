import React from 'react'
import cx from 'classnames'

export const renderBadge = ({
    badge,
    badgeColor,
    icon,
    title,
}) => (
    badge && (title
        ? (
            <span
                className={
                    cx(
                        `badge badge-${badgeColor}`,
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
