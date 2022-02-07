import React from 'react'
import classNames from 'classnames'

import { AlertTypes } from './AlertsTypes'
import { AlertSection } from './AlertSection'

export const DefaultAlert = ({
    title,
    text,
    color,
    href,
    timestamp,
    closeButton,
    className,
    style,
    stacktrace = null,
    animate,
    onDismiss,
    t,
    stacktraceVisible,
    togglingStacktrace,
    onClose = null,
}) => {
    const batchedActionToClose = (e) => {
        e.preventDefault()

        if (onClose) {
            /* custom onClose this is used id AlertField */
            onClose()
        } else {
            onDismiss()
        }
    }

    const batchedActionToToggling = (e) => {
        e.preventDefault()
        togglingStacktrace()
    }

    const needToDivide = (title && text) || !!(text && (timestamp || closeButton))

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
                <AlertSection
                    text={title}
                    timestamp={timestamp}
                    closeButton={closeButton}
                    onClick={batchedActionToClose}
                    textClassName="n2o-alert-segment__title"
                />
                {
                    needToDivide && <hr className="w-100 n2o-alert__divider" />
                }
                <AlertSection text={text} textClassName="n2o-alert-segment__text" />
                <AlertSection
                    onClick={batchedActionToToggling}
                    stacktraceVisible={stacktraceVisible}
                    stacktrace={stacktrace}
                    t={t}
                />
            </a>
        </div>
    )
}

DefaultAlert.propTypes = AlertTypes
