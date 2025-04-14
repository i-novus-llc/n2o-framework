import React from 'react'
import classNames from 'classnames'

import { AlertWrapperProps, AnimationDirection } from './types'

export function AlertWrapper({
    children,
    className,
    severity,
    animate,
    stacktrace,
    href,
    style,
    animationDirection = AnimationDirection.default,
}: AlertWrapperProps) {
    return (
        <div
            className={classNames(
                'alert n2o-alert',
                className,
                {
                    [`alert-${severity}`]: severity,
                    'n2o-alert--animated': animate,
                    'n2o-alert--animated_default': animationDirection === AnimationDirection.default,
                    'n2o-alert--animated_reversed': animationDirection === AnimationDirection.reversed,
                    'with-details': stacktrace,
                    'with-link': href,
                },
            )}
            style={style}
        >
            <a
                href={href}
                className={classNames(
                    {
                        [`alert-${severity}`]: severity,
                        'n2o-alert__with-link': href,
                    },
                )}
            >
                {children}
            </a>
        </div>
    )
}
