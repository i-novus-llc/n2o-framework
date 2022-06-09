import React from 'react'
import classNames from 'classnames'
import PropTypes from 'prop-types'

export function AlertWrapper({
    children,
    className,
    severity,
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
                    [`alert-${severity}`]: severity,
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

AlertWrapper.propTypes = {
    children: PropTypes.node,
    className: PropTypes.string,
    severity: PropTypes.string,
    animate: PropTypes.bool,
    stacktrace: PropTypes.bool,
    href: PropTypes.string,
    style: PropTypes.object,
}
