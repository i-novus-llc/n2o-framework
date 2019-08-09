import React from 'react';
import PropTypes from 'prop-types';
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
        className={inputClass}
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

InputText.propTypes = {
  /**
   * Значение
   */
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  /**
   * Флаг активности
   */
  disabled: PropTypes.bool,
  /**
   * Callback на фокус
   */
  onFocus: PropTypes.func,
  /**
   * Callback на вставку значения
   */
  onPaste: PropTypes.func,
  /**
   * Callback на потерю фокуса
   */
  onBlur: PropTypes.func,
  /**
   * Callback на нажатие кнопки
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
   * Авто фокусировка на контроле
   */
  autoFocus: PropTypes.bool,
  /**
   * Максимальная длина
   */
  length: PropTypes.string,
  /**
   * Функция получения ref
   */
  inputRef: PropTypes.any,
};

InputText.defaultProps = {
  onChange: () => {},
  className: '',
  disabled: false,
  autoFocus: false,
};

export default InputText;
