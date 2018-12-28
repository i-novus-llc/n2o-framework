import React from 'react';
import PropTypes from 'prop-types';
import { Label as BootstrapLabel } from 'reactstrap';

import Popover from '../../../../snippets/Popover/Popover';

/**
 * Лейбел поля
 * @param {string} id - уникальный индефикатор
 * @param {string|element} value - текст/элемент лейбела
 * @param {boolean} required - поле обязательное(к лейбелу добавляется звездочка) или нет
 * @param {string} className - дополнительный класс стиля
 * @param {string} style - дополнительный dom стиль
 * @param {string} help - подсказка
 * @param {object} props - остальные пропсы
 * @example
 * <Label value='Телефон'/ required={true} />
 */

const Label = ({ id, value, required, className, style, help, ...props }) => {
  const starStyle = { verticalAlign: 'top', marginLeft: 3, color: '#e53935', fontWeight: 900 };
  return React.isValidElement(value) ? (
    <div>
      {React.cloneElement(value, {
        className: `col-form-label ${className}`,
        style: { display: 'inline-block', ...style }
      })}
      {required ? <span style={starStyle}>*</span> : ''}
      {help && <Popover id={id} help={help} />}
    </div>
  ) : (
    <BootstrapLabel className={className} style={style}>
      {value}
      {required ? <span style={starStyle}>*</span> : null}
      {help && <Popover id={id} help={help} />}
    </BootstrapLabel>
  );
};

Label.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  required: PropTypes.bool,
  className: PropTypes.string,
  style: PropTypes.object
};

Label.defaultProps = {
  className: '',
  style: {}
};

export default Label;
