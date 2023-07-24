import React, { useState, useCallback } from 'react'
import { withTranslation } from 'react-i18next'

import { LoaderAlert } from './LoaderAlert'
import { DefaultAlert } from './DefaultAlert'
import { AlertProps, AnimationDirection } from './types'

function AlertComponent(props: AlertProps) {
    const {
        loader,
        title,
        text,
        severity,
        href,
        timestamp,
        closeButton,
        className,
        style,
        animate,
        t,
        stacktrace,
        onDismiss,
        stopRemoving,
        placement,
    } = props

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

    const { visible } = props

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

AlertComponent.defaultProps = {
    text: '',
    title: '',
    severity: 'danger',
    closeButton: false,
    visible: true,
    position: 'relative',
    animate: false,
    placement: 'top',
    onDismiss: () => {},
    stopRemoving: () => {},
    t: () => '',
} as AlertProps

export const Alert = withTranslation()(AlertComponent)
