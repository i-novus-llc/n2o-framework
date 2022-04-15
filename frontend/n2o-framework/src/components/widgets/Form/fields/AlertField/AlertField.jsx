import React from 'react'
import classNames from 'classnames'

import { AlertTypes } from '../../../../snippets/Alerts/AlertsTypes'
import { DefaultAlert } from '../../../../snippets/Alerts/DefaultAlert'
import { hideField } from '../../../../../ducks/form/store'

/**
 * Компонент - AlertField формы
 * @reactProps {string} title - заголовок алерта
 * @reactProps {string} text - текст алерта
 * @reactProps {object} style - стили алерта
 * @reactProps {string} className - css-класс для поля
 * @reactProps {string} color - цвет поля
 * @reactProps {boolean} closeButton - отобразить кнопку закрытия
 * @reactProps {string} href - href onClick по Alert
 * @reactProps {string} form - form id
 * @reactProps {string} id  - alert id / field id in form
 * @reactProps {function} dispatch - redux dispatch
 * @reactProps {boolean} visible - флаг видимости
 * @return {node|null}
 */

export function AlertField({
    title,
    text,
    style,
    className,
    color = 'secondary',
    closeButton,
    href,
    form,
    id,
    dispatch,
    visible = true,
}) {
    const onClose = () => dispatch(hideField(form, id))

    if (!visible) {
        return null
    }

    return (
        <DefaultAlert
            title={title}
            text={text}
            style={style}
            className={classNames('n2o-alert-field', className)}
            color={color}
            onClose={closeButton && onClose}
            closeButton={closeButton}
            href={href}
            isField
        />
    )
}

AlertField.propTypes = AlertTypes

export default AlertField
