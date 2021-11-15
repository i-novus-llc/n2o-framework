import React from 'react'
import cx from 'classnames'

export const renderBadge = item => (
    item.title && item.badge
        ? (
            <span
                className={
                    cx(
                        `badge badge-${item.badgeColor}`,
                    )}
            >
                {item.badge}
            </span>
        )
        : item.icon && (
            <span
                className={
                    cx(
                        `n2o-counter badge badge-${item.badgeColor}`,
                        item.badge !== ' ' ? 'n2o-badge-counter' : 'n2o-badge-dot',
                    )}
            >
                {item.badge}
            </span>
        ))
