import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import Input from '../Input/Input';

/**
 * Контрол Input
 * @reactProps {boolean} length - максимальная длина значения
 * @reactProps {string} value - значение поля
 * @reactProps {string} placeholder - плэйсхолдер
 * @reactProps {any} inputRef
 * @reactProps {boolean} disabled - флаг неактивности поля
 * @reactProps {boolean} disabled - флаг только для чтения
 * @reactProps {boolean} autoFocus - автофокус
 * @reactProps {function} onFocus - callback на фокус
 * @reactProps {function} onPaste - callback при вставке в инпут
 * @reactProps {function} onBlur - callback при блюре инпута
 * @reactProps {function} onChange - callback при изменение инпута
 * @reactProps {function} onKeyDown - callback при нажатии на кнопку клавиатуры
 * @reactProps {string} className - css-класс
 * @reactProps {object} style - объект стилей
 */
class InputText extends React.Component {
  constructor(props) {
    super(props);

    this.onChange = this.onChange.bind(this);
  }
  /**
   * Рендер
   */
  onChange(e) {
    const { onChange } = this.props;
    if (typeof onChange !== 'undefined') {
      onChange(e.target.value);
    }
  }

  render() {
    const {
      value,
      placeholder,
      length,
      disabled,
      inputRef,
      onChange,
      onPaste,
      onFocus,
      onBlur,
      onKeyDown,
      autoFocus,
      className,
      style,
    } = this.props;
    const inputClass = `form-control ${className}`;
    return (
      <Input
        type="text"
        className={cn('n2o-input-text', inputClass)}
        inputRef={inputRef}
        style={style}
        autoFocus={autoFocus}
        maxLength={length}
        value={value == null ? '' : value}
        placeholder={placeholder}
        disabled={disabled}
        onPaste={onPaste}
        onFocus={onFocus}
        onBlur={onBlur}
        onKeyDown={onKeyDown}
        onChange={this.onChange}
      />
    );
  }
}

Input.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  disabled: PropTypes.bool,
  onFocus: PropTypes.func,
  onPaste: PropTypes.func,
  onBlur: PropTypes.func,
  onKeyDown: PropTypes.func,
  className: PropTypes.string,
  style: PropTypes.object,
  placeholder: PropTypes.string,
  onChange: PropTypes.func,
  autoFocus: PropTypes.bool,
  length: PropTypes.string,
  inputRef: PropTypes.any,
};

Input.defaultProps = {
  onChange: () => {},
  className: '',
  disabled: false,
  autoFocus: false,
};

export default InputText;
