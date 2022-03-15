import React from 'react'
import PropTypes from 'prop-types'
import isNil from 'lodash/isNil'
import uniqueId from 'lodash/uniqueId'
import classNames from 'classnames'
import { compose, lifecycle, setDisplayName } from 'recompose'

import HelpPopover from '../../widgets/Form/fields/StandardField/HelpPopover'
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
 * @reactProps {string} help - подсказка в popover
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
    help,
    tabIndex,
}) {
    return (
        <div
            className={classNames(
                'custom-control',
                'custom-checkbox',
                'n2o-checkbox',
                className,
                {
                    'custom-control-inline': inline,
                    'd-flex': help,
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
                tabIndex={tabIndex}
            />
            <label className="custom-control-label" htmlFor={elementId}>
                {label}
            </label>
            {help && <HelpPopover help={help} />}
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
   * Callback фокуса
   */
    onFocus: PropTypes.func,
    /**
   * Callback потери фокуса
   */
    onBlur: PropTypes.func,
    elementId: PropTypes.string,
    /**
     * Подсказка в popover
     */
    help: PropTypes.string,
    tabIndex: PropTypes.oneOfType(PropTypes.string, PropTypes.number),
}

CheckboxN2O.defaultProps = {
    disabled: false,
    inline: false,
    tabIndex: 0,
    onFocus: () => {},
    onBlur: () => {},
    onChange: () => {},
    onClick: () => {},
}

const CheckboxN2OWrapped = compose(
    setDisplayName('CheckboxN2O'),
    lifecycle({
        componentDidMount() {
            this.setState({ elementId: uniqueId('checkbox-') })
        },
    }),
)(CheckboxN2O)

export { CheckboxN2O }
export { CheckboxN2OWrapped }

export default CheckboxN2OWrapped
