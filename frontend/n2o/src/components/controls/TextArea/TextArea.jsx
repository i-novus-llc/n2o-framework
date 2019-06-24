import React from 'react';
import PropTypes from 'prop-types';
import TextareaAutosize from 'react-textarea-autosize';

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
      className={inputClass}
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
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  disabled: PropTypes.bool,
  className: PropTypes.string,
  style: PropTypes.object,
  placeholder: PropTypes.string,
  onChange: PropTypes.func,
  rows: PropTypes.number,
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
