import React from 'react';
import PropTypes from 'prop-types';
import TextareaAutosize from 'react-textarea-autosize';
import cx from 'classnames';

/**
 * Компонент TextAreat
 * @param {string} className
 * @param {object} style
 * @param {boolean} disabled
 * @param {boolean} disabled
 * @param {string} placeholder
 * @param {number} rows
 * @param {number} maxRows
 * @param {string|number} value
 * @param {function} onChange
 * @returns {*}
 * @constructor
 */
function TextArea({
  className,
  style,
  disabled,
  placeholder,
  rows,
  maxRows,
  value,
  onChange,
}) {
  const inputClass = `form-control ${className}`;
  return (
    <TextareaAutosize
      className={cx('n2o-text-area', inputClass)}
      style={style}
      disabled={disabled}
      placeholder={placeholder}
      minRows={rows}
      maxRows={maxRows}
      value={value || ''}
      onChange={onChange}
    />
  );
}

TextArea.propTypes = {
  /**
   * Значение
   */
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  /**
   * Флаг активности
   */
  disabled: PropTypes.bool,
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
   * Минимальное количество строк
   */
  rows: PropTypes.number,
  /**
   * Максимальное количество строк
   */
  maxRows: PropTypes.number,
};

TextArea.defaultProps = {
  onChange: () => {},
  className: '',
  disabled: false,
  rows: 3,
  maxRows: 3,
};

export default TextArea;
