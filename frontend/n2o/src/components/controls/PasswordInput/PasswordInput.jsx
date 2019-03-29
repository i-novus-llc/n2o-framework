import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import Input from '../Input/Input';

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
 */

class PasswordInput extends React.Component {
  /**
   * Базовый рендер
   */
  render() {
    const { className, length, ...rest } = this.props;
    return (
      <Input
        maxLength={length}
        className={cn(['form-control', { [className]: className }])}
        type="password"
        {...rest}
      />
    );
  }
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
  autoFocus: PropTypes.bool
};

PasswordInput.defaultProps = {
  onChange: () => {},
  className: '',
  disabled: false,
  autoFocus: false
};

export default PasswordInput;
