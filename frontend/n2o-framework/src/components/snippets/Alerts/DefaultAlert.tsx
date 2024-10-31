import React from 'react'
import omit from 'lodash/omit'

import { AlertWrapper } from './AlertWrapper'
import { AlertSection } from './AlertSection'
import { CommonAlertProps } from './types'

interface Props extends CommonAlertProps {
    severity: string
    href?: string
    animate?: boolean
    onDismiss?(): void
    togglingStacktrace?(): void
    onClose?(): void
    isField?: boolean
    animationDirection?: 'default' | 'reversed'
}

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
}: Props) => {
    const severity = propsSeverity || 'secondary'

    const batchedActionToClose = (e: React.MouseEvent<HTMLElement>) => {
        e.preventDefault()

        if (onClose) {
            /* custom onClose this is used id AlertField */
            onClose?.()
        } else {
            onDismiss?.()
        }
    }

    const batchedActionToToggling = (e: React.MouseEvent<HTMLElement>) => {
        e.preventDefault()

        if (togglingStacktrace) { togglingStacktrace() }
    }

    const currentTitle = isField ? (title || text) : title
    const currentText = (isField && !title) ? null : text

    const titleSegmentClassName = title ? 'n2o-alert-segment__title' : 'n2o-alert-segment__text'

    const needToDivide = (currentTitle && currentText) || !!(currentText && (timestamp || closeButton))

    /* this is necessary for custom text colors or font-sizes */
    const getSectionStyle = (style: CommonAlertProps['style']) => {
        if (!style) { return null }

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
        stacktrace: Boolean(stacktrace),
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
                    t={t}
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
                t={t}
            />
            {
                needToDivide && <hr className="w-100 n2o-alert__divider" />
            }
            <AlertSection text={currentText} textClassName="n2o-alert-segment__text" t={t} />
            <AlertSection
                onClick={batchedActionToToggling}
                stacktraceVisible={stacktraceVisible}
                stacktrace={stacktrace}
                t={t}
            />
        </AlertWrapper>
    )
}
