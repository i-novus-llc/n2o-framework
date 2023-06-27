import React from 'react'
import classNames from 'classnames'
import { useDispatch } from 'react-redux'

import { AlertTypes } from '../../../../snippets/Alerts/AlertsTypes'
import { DefaultAlert } from '../../../../snippets/Alerts/DefaultAlert'
import { setFieldVisible } from '../../../../../ducks/form/store'
import { useFormContext } from '../../../../core/FormProvider/hooks/useFormContext'

/**
 * Компонент - AlertField формы
 * @reactProps {string} title - заголовок алерта
 * @reactProps {string} text - текст алерта
 * @reactProps {object} style - стили алерта
 * @reactProps {string} className - css-класс для поля
 * @reactProps {string} severity - цвет поля
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
    severity,
    closeButton,
    href,
    id,
    visible = true,
}) {
    const dispatch = useDispatch()
    const { formName } = useFormContext()
    const onClose = () => dispatch(setFieldVisible(formName, id, false))

    if (!visible) {
        return null
    }

    return (
        <DefaultAlert
            title={title}
            text={text}
            style={style}
            className={classNames('n2o-snippet n2o-alert-field', className)}
            severity={severity || color}
            onClose={closeButton && onClose}
            closeButton={closeButton}
            href={href}
            isField
        />
    )
}

AlertField.propTypes = AlertTypes

export default AlertField
