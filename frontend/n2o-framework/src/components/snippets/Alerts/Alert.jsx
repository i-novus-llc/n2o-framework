import React, { useState } from 'react'
import { withTranslation } from 'react-i18next'

import { LoaderAlert } from './LoaderAlert'
import { DefaultAlert } from './DefaultAlert'
import { AlertTypes } from './AlertsTypes'

/**
 * Компонент сообщения-алерта
 * @reactProps {string} title - лейбл алерта
 * @reactProps {string} text - текст алерта
 * @reactProps {string} color - тип алерта: 'info', 'danger', 'warning' или 'success'.
 * @reactProps {string} details - подробности, находятся под текстом, показываются (скрываются) по клику на выделенный текст
 * @reactProps {boolean} closeButton - отображать кнопку скрытия алерта или нет
 * @reactProps {boolean} onDismiss - выполняется при скрытии алерта
 * @reactProps {boolean} className - css-класс
 * @reactProps {number} style -  объект css стилей
 * @reactProps {number} icon - css-класс для иконки(иконка находится перед лейбелом )
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {string} position - настройка позиционирования
 * @reactProps {string} help - текст для Popover
 * @reactProps {boolean} animate - флаг включения анимации при появлении
 * @example <Alert onDismiss={this.onDismiss} title='Сообщение' text={this.text} />
 */

function Alert(props) {
    const [detailsVisible, setDetailsVisible] = useState(false)

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
        icon,
        stacktrace,
        onDismiss,
        help,
    } = props

    const togglingDetails = () => setDetailsVisible(!detailsVisible)

    const formattingDetails = (stacktrace) => {
        if (Array.isArray(stacktrace)) {
            return stacktrace.map(detail => (
                <>
                    {detail}
                    <br />
                </>
            ))
        }

        return stacktrace
    }

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
            icon={icon}
            stacktrace={stacktrace}
            onDismiss={onDismiss}
            help={help}
            animate={animate}
            t={t}
            detailsVisible={detailsVisible}
            togglingDetails={togglingDetails}
            formattingDetails={formattingDetails}
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
