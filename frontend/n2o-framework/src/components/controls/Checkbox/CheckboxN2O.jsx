import React from 'react'
import PropTypes from 'prop-types'
import isNil from 'lodash/isNil'
import uniqueId from 'lodash/uniqueId'
import cx from 'classnames'
import { compose, lifecycle, setDisplayName } from 'recompose'

import Input from '../Input/Input'

/**
 * Альтернативный чекбокс
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled - только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {function} onClick - событие клика по чекбоксу,
 * @reactProps {string} label - лейбл
 * @reactProps {string} className - класс копонента CheckboxN2O
 * @reactProps {boolean} inline - в ряд
 */

function CheckboxN2O({
    className,
    label,
    disabled,
    value,
    onChange,
    inline,
    checked,
    onClick,
    onFocus,
    onBlur,
    elementId,
}) {
    return (
        <div
            className={cx(
                'custom-control',
                'custom-checkbox',
                'n2o-checkbox',
                className,
                {
                    'custom-control-inline': inline,
                },
            )}
            onClick={onClick}
        >
            <Input
                id={elementId}
                className="custom-control-input"
                disabled={disabled}
                type="checkbox"
                value={value}
                checked={isNil(checked) ? !!value : checked}
                onChange={onChange}
                onFocus={onFocus}
                onBlur={onBlur}
            />
            <label className="custom-control-label" htmlFor={elementId}>
                {label}
            </label>
        </div>
    )
}

CheckboxN2O.propTypes = {
    /**
   * Значение контрола
   */
    value: PropTypes.any,
    /**
   * Callback изменения
   */
    onChange: PropTypes.func,
    /**
   * Callback клика
   */
    onClick: PropTypes.func,
    /**
   * Флаг активности
   */
    disabled: PropTypes.bool,
    /**
   * Label контрола
   */
    label: PropTypes.string,
    /**
   * Флаг рендера label в одну строку с контролом
   */
    inline: PropTypes.bool,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Checked контрола
   */
    checked: PropTypes.oneOfType([PropTypes.string, PropTypes.bool]),
    /**
   * Стили
   */
    style: PropTypes.object,
    id: PropTypes.string,
    /**
   * Callback фокуса
   */
    onFocus: PropTypes.func,
    /**
   * Callback потери фокуса
   */
    onBlur: PropTypes.func,
}

CheckboxN2O.defaultProps = {
    disabled: false,
    inline: false,
    onFocus: () => {},
    onBlur: () => {},
    onChange: () => {},
    onClick: () => {},
}

export { CheckboxN2O }
export default compose(
    setDisplayName('CheckboxN2O'),
    lifecycle({
        componentDidMount() {
            this.setState({ elementId: uniqueId('checkbox-') })
        },
    }),
)(CheckboxN2O)
