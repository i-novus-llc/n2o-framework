import React, { useState, useCallback } from 'react'
import { withTranslation } from 'react-i18next'

import { LoaderAlert } from './LoaderAlert'
import { DefaultAlert } from './DefaultAlert'
import { AlertProps, AnimationDirection, Severity } from './types'

const noop = () => {}

function AlertComponent({
    loader,
    title = '',
    text = '',
    severity = Severity.danger,
    href,
    timestamp,
    closeButton = false,
    className,
    style,
    animate = false,
    t,
    stacktrace,
    onDismiss = noop,
    stopRemoving = noop,
    placement = 'top',
    visible = true,
}: AlertProps) {
    const [stacktraceVisible, setStacktraceVisible] = useState(false)

    const togglingStacktrace = useCallback(() => {
        setStacktraceVisible(!stacktraceVisible)
        if (stopRemoving) {
            stopRemoving()
        }
    }, [stacktraceVisible, stopRemoving])

    const formattingDetails = useCallback((stacktrace: AlertProps['stacktrace']) => {
        if (!stacktrace) {
            return null
        }

        if (Array.isArray(stacktrace)) {
            return stacktrace.join('\r\n')
        }

        return stacktrace
    }, [])

    if (!visible) {
        return null
    }

    if (loader) {
        return (
            <LoaderAlert
                text={String(text)}
                severity={severity}
                className={className}
                style={style}
                animate={animate}
                t={t}
            />
        )
    }

    return (
        <DefaultAlert
            title={String(title)}
            text={String(text)}
            severity={severity}
            href={href}
            timestamp={timestamp}
            closeButton={closeButton}
            className={className}
            style={style}
            stacktrace={formattingDetails(stacktrace)}
            onDismiss={onDismiss}
            animate={animate}
            t={t}
            stacktraceVisible={stacktraceVisible}
            togglingStacktrace={togglingStacktrace}
            animationDirection={(placement || '').startsWith('bottom') ? AnimationDirection.reversed : AnimationDirection.default}
        />
    )
}

export const Alert = withTranslation()(AlertComponent)
