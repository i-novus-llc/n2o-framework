import React from 'react';
import { compose, withState, withHandlers } from 'recompose';
import PropTypes from 'prop-types';
import cn from 'classnames';
import Input from '../Input/Input';
import { Button } from 'reactstrap';

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
        className={cn(['form-control', { [className]: className }])}
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
          <i className={cn('fa', showPass ? 'fa-eye-slash' : 'fa-eye')} />
        </Button>
      ) : null}
    </div>
  );
}

PasswordInput.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  disabled: PropTypes.bool,
  onFocus: PropTypes.func,
  onPaste: PropTypes.func,
  obBlur: PropTypes.func,
  onKeyDown: PropTypes.func,
  className: PropTypes.string,
  style: PropTypes.object,
  placeholder: PropTypes.string,
  onChange: PropTypes.func,
  length: PropTypes.string,
  type: PropTypes.string,
  autoFocus: PropTypes.bool,
  showPasswordBtn: PropTypes.bool,
};

PasswordInput.defaultProps = {
  onChange: () => {},
  className: '',
  disabled: false,
  autoFocus: false,
  showPasswordBtn: true,
};

export default compose(
  withState('showPass', 'setShowPass', false),
  withHandlers({
    onToggleShowPass: ({ showPass, setShowPass }) => () =>
      setShowPass(!showPass),
  })
)(PasswordInput);
