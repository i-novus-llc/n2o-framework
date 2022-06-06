import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

export function AlertWrapper({
    children,
    className,
    color,
    animate,
    stacktrace,
    href,
    style,
}) {
    return (
        <div
            className={classNames(
                'alert n2o-alert',
                className, {
                    [`alert-${color}`]: color,
                    'n2o-alert--animated': animate,
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
                        [`alert-${color}`]: color,
                        'n2o-alert__with-link': href,
                    },
                )}
            >
                {children}
            </a>
        </div>
    )
}

AlertWrapper.propTypes = {
    children: PropTypes.node,
    className: PropTypes.string,
    color: PropTypes.string,
    animate: PropTypes.bool,
    stacktrace: PropTypes.bool,
    href: PropTypes.string,
    style: PropTypes.object,
}
