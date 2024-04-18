import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'

import { StatusText } from '../../../../snippets/StatusText/StatusText'
import withTooltip from '../../withTooltip'

/**
 * Ячейка таблицы типа статус
 * @reactProps {string} id
 * @reactProps {object} model - модель данных
 * @reactProps {string} fieldKey - ключ модели для этой ячейки
 * @reactProps {string} color - цветовая схема бейджа(["primary", "secondary", "success", "danger", "warning", "info", "light", "dark", "white"])
 * @example
 * <StatusCell model={model} filedKey={'name'} color="info"/>
 */

function StatusCell(props) {
    const {
        id,
        className,
        visible,
        color,
        model,
        fieldKey,
        textPosition,
        forwardedRef,
    } = props

    if (!visible) { return null }

    const statusText = get(model, fieldKey || id)

    return (
        <div ref={forwardedRef} className="d-inline-flex">
            <StatusText
                text={statusText}
                textPosition={textPosition}
                color={color}
                className={className}
            />
        </div>
    )
}

StatusCell.propTypes = {
    /**
     * ID ячейки
     */
    id: PropTypes.string,
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Ключ значения в данных
     */
    fieldKey: PropTypes.string,
    /**
     * Модель данных
   */
    model: PropTypes.object,
    /**
   * Цвет стаутуса
   */
    color: PropTypes.oneOf([
        'primary',
        'secondary',
        'success',
        'danger',
        'warning',
        'info',
        'light',
        'dark',
        'white',
    ]),
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
    textPosition: PropTypes.string,
}

StatusCell.defaultProps = {
    visible: true,
    model: {},
    color: '',
}

export default withTooltip(StatusCell)
