import React, { ReactNode, CSSProperties } from 'react'
import classNames from 'classnames'

interface Props {
    children: ReactNode
    className?: string
    severity?: string
    animate?: boolean
    stacktrace?: boolean
    href?: string
    style?: CSSProperties
    animationDirection?: 'default' | 'reversed'
}

export const AlertWrapper = ({
    children,
    className,
    severity,
    animate,
    stacktrace,
    href,
    style,
    animationDirection = 'default',
}: Props) => {
    return (
        <div
            className={classNames(
                'alert n2o-alert',
                className,
                {
                    [`alert-${severity}`]: severity,
                    'n2o-alert--animated': animate,
                    'n2o-alert--animated_default': animationDirection === 'default',
                    'n2o-alert--animated_reversed': animationDirection === 'reversed',
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
