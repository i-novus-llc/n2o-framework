import React, { useState, useCallback } from 'react'
import { withTranslation } from 'react-i18next'

import { LoaderAlert } from './LoaderAlert'
import { DefaultAlert } from './DefaultAlert'
import { AlertTypes } from './AlertsTypes'

/**
 * Компонент сообщения-алерта
 * @reactProps {string} title - лейбл алерта
 * @reactProps {string} text - текст алерта
 * @reactProps {string} severity - тип алерта: 'info', 'danger', 'warning' или 'success'.
 * @reactProps {string} stacktrace - подробности, находятся под текстом, показываются (скрываются) по клику на выделенный текст
 * @reactProps {boolean} closeButton - отображать кнопку скрытия алерта или нет
 * @reactProps {boolean} onDismiss - выполняется при скрытии алерта
 * @reactProps {boolean} className - css-класс
 * @reactProps {number} style -  объект css стилей
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {boolean} animate - флаг включения анимации при появлении
 * @example <Alert onDismiss={this.onDismiss} title='Сообщение' text={this.text} />
 */

function Alert(props) {
    const {
        loader,
        title: propsTitle,
        text: propsText,
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
        stopRemoving()
    }, [stacktraceVisible, stopRemoving])

    const formattingDetails = useCallback((stacktrace) => {
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

    const text = propsText === 0 ? String(propsText) : propsText

    if (loader) {
        return (
            <LoaderAlert
                text={text}
                severity={severity}
                className={className}
                style={style}
                animate={animate}
                t={t}
            />
        )
    }

    const title = propsTitle === 0 ? String(propsTitle) : propsTitle

    return (
        <DefaultAlert
            title={title}
            text={text}
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
            animationDirection={placement.startsWith('bottom') ? 'reversed' : 'default'}
        />
    )
}
Alert.defaultProps = {
    text: '',
    title: '',
    severity: 'danger',
    details: '',
    closeButton: false,
    visible: true,
    onDismiss: () => {},
    stopRemoving: () => {},
    position: 'relative',
    animate: false,
    t: () => {
    },
    placement: 'top',
}

Alert.propTypes = AlertTypes

export default withTranslation()(Alert)
