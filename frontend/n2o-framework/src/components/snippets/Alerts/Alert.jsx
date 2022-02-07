import React, { useState, useCallback } from 'react'
import { withTranslation } from 'react-i18next'

import { LoaderAlert } from './LoaderAlert'
import { DefaultAlert } from './DefaultAlert'
import { AlertTypes } from './AlertsTypes'

/**
 * Компонент сообщения-алерта
 * @reactProps {string} title - лейбл алерта
 * @reactProps {string} text - текст алерта
 * @reactProps {string} color - тип алерта: 'info', 'danger', 'warning' или 'success'.
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
        title,
        text,
        color,
        href,
        timestamp,
        closeButton,
        className,
        style,
        animate,
        t,
        stacktrace,
        onDismiss,
    } = props

    const [stacktraceVisible, setStacktraceVisible] = useState(false)
    const togglingStacktrace = useCallback(() => setStacktraceVisible(!stacktraceVisible), [stacktraceVisible])

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

    if (loader) {
        return (
            <LoaderAlert
                text={text}
                color={color}
                className={className}
                style={style}
                animate={animate}
                t={t}
            />
        )
    }

    return (
        <DefaultAlert
            title={title}
            text={text}
            color={color}
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
        />
    )
}
Alert.defaultProps = {
    text: '',
    title: '',
    color: 'danger',
    details: '',
    closeButton: true,
    visible: true,
    onDismiss: () => {
    },
    position: 'relative',
    animate: false,
    t: () => {
    },
}

Alert.propTypes = AlertTypes

export default withTranslation()(Alert)
