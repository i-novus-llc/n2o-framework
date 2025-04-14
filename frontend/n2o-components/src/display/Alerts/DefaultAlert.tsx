import React, { SyntheticEvent } from 'react'
import omit from 'lodash/omit'

import { AlertWrapper } from './AlertWrapper'
import { AlertSection } from './AlertSection'
import { DefaultAlertProps } from './types'

export const DefaultAlert = ({
    title,
    text,
    severity: propsSeverity,
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
    onClose,
    isField = false,
    animationDirection,
}: DefaultAlertProps) => {
    const severity = propsSeverity || 'secondary'

    const batchedActionToClose = (e: SyntheticEvent) => {
        e.preventDefault()

        if (onClose) {
            /* custom onClose this is used id AlertField */
            onClose()
        } else {
            onDismiss?.()
        }
    }

    const batchedActionToToggling = (e: SyntheticEvent) => {
        e.preventDefault()
        togglingStacktrace()
    }

    const currentTitle = isField ? (title || text) : title
    const currentText = (isField && !title) ? null : text

    const titleSegmentClassName = title ? 'n2o-alert-segment__title' : 'n2o-alert-segment__text'

    const needToDivide = (currentTitle && currentText) || !!(currentText && (timestamp || closeButton))

    /* this is necessary for custom text colors or font-sizes */
    const getSectionStyle = (style: DefaultAlertProps['style']) => {
        if (!style) {
            return null
        }

        return omit(style, ['width', 'height', 'padding', 'margin', 'backgroundColor'])
    }

    const checkSimpleAlert = () => {
        if (closeButton && !stacktrace && !timestamp) {
            return (!title && text) || (title && !text)
        }

        return false
    }

    const isSimple = checkSimpleAlert()

    const wrapperProps = {
        className,
        severity,
        animate,
        stacktrace,
        href,
        style,
        animationDirection,
    }

    /* simple one section alert without extra features */
    if (isSimple) {
        return (
            <AlertWrapper {...wrapperProps}>
                <AlertSection
                    text={currentTitle || currentText}
                    timestamp={timestamp}
                    closeButton={closeButton}
                    onClick={batchedActionToClose}
                    textClassName={titleSegmentClassName}
                    style={getSectionStyle(style)}
                    isSimple
                />
            </AlertWrapper>
        )
    }

    return (
        <AlertWrapper {...wrapperProps}>
            <AlertSection
                text={currentTitle}
                timestamp={timestamp}
                closeButton={closeButton}
                onClick={batchedActionToClose}
                textClassName={titleSegmentClassName}
                style={getSectionStyle(style)}
            />
            {
                needToDivide && <hr className="w-100 n2o-alert__divider" />
            }
            <AlertSection text={currentText} textClassName="n2o-alert-segment__text" />
            <AlertSection
                onClick={batchedActionToToggling}
                stacktraceVisible={stacktraceVisible}
                stacktrace={stacktrace}
                t={t}
            />
        </AlertWrapper>
    )
}
