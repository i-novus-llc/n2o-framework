import React from 'react'
import { compose, withState, withHandlers, setDisplayName } from 'recompose'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import Button from 'reactstrap/lib/Button'

import Input from '../Input/Input'

/**
 * Контрол Input для паролей
 * @reactProps {boolean} length - максимальная длина значения
 * @reactProps {string} value - значение поля
 * @reactProps {string} placeholder - плэйсхолдер
 * @reactProps {boolean} disabled - флаг неактивности поля
 * @reactProps {boolean} disabled - флаг только для чтения
 * @reactProps {function} onFocus - callback на фокус
 * @reactProps {function} onPaste - callback при вставке в инпут
 * @reactProps {function} onBlur - callback при блюре инпута
 * @reactProps {function} onChange - callback при изменение инпута
 * @reactProps {function} onKeyDown - callback при нажатии на кнопку клавиатуры
 * @reactProps {boolean} autoFocus - флаг автофокуса
 * @reactProps {string} className - css-класс
 * @reactProps {object} style - объект стилей
 * @reactProps {string} type - тип поля
 * @reactProps {boolean} showPasswordBtn - кнопка показа пароля
 */

function PasswordInput({
    className,
    length,
    style,
    autoFocus,
    showPasswordBtn,
    value,
    placeholder,
    disabled,
    onPaste,
    onFocus,
    onBlur,
    onKeyDown,
    onChange,
    onToggleShowPass,
    showPass,
}) {
    return (
        <div className="n2o-input-password">
            <Input
                maxLength={length}
                className={classNames(['form-control', { [className]: className }])}
                type={showPass && showPasswordBtn ? 'text' : 'password'}
                style={style}
                autoFocus={autoFocus}
                value={value == null ? '' : value}
                placeholder={placeholder}
                disabled={disabled}
                onPaste={onPaste}
                onFocus={onFocus}
                onBlur={onBlur}
                onKeyDown={onKeyDown}
                onChange={onChange}
            />
            {showPasswordBtn ? (
                <Button
                    className="n2o-input-password-toggler"
                    onClick={onToggleShowPass}
                    size="sm"
                    color="link"
                >
                    <i className={classNames('fa', showPass ? 'fa-eye-slash' : 'fa-eye')} />
                </Button>
            ) : null}
        </div>
    )
}

PasswordInput.propTypes = {
    /**
     * Значение
     */
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
     * Флаг активности
     */
    disabled: PropTypes.bool,
    /**
     * Callback фокуса
     */
    onFocus: PropTypes.func,
    /**
     * Callback вставки значения
     */
    onPaste: PropTypes.func,
    /**
     * Callback потери фокуса
     */
    onBlur: PropTypes.func,
    /**
     * Callback нажатия кнопок
     */
    onKeyDown: PropTypes.func,
    /**
     * Класс
     */
    className: PropTypes.string,
    /**
     * Стили
     */
    style: PropTypes.object,
    /**
     * Placeholder контрола
     */
    placeholder: PropTypes.string,
    /**
     * Callback на изменение
     */
    onChange: PropTypes.func,
    /**
     * Максимальная длина
     */
    length: PropTypes.string,
    /**
     * Авто фокусировка на контрол
     */
    autoFocus: PropTypes.bool,
    /**
     * Флаг показа кнопки, которая показывает введенный пароль
     */
    showPasswordBtn: PropTypes.bool,
    onToggleShowPass: PropTypes.func,
    showPass: PropTypes.bool,
}

PasswordInput.defaultProps = {
    onChange: () => {},
    className: '',
    disabled: false,
    autoFocus: false,
    showPasswordBtn: true,
}

export { PasswordInput }
export default compose(
    setDisplayName('InputPassword'),
    withState('showPass', 'setShowPass', false),
    withHandlers({
        onToggleShowPass: ({ showPass, setShowPass }) => () => setShowPass(!showPass),
    }),
)(PasswordInput)
