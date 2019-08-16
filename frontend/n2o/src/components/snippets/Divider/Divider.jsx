import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

const dividerClass = props => {
  const { className, dashed, position, type, children } = props;
  return cx(className, {
    [`divider-h divider-h_with-title divider-h_${position} divider-h_dashed`]:
      type === 'horizontal' && children && dashed,
    [`divider-h divider-h_with-title divider-h_${position}`]:
      type === 'horizontal' && children && !dashed,
    'divider-h divider_horizontal divider-h_dashed':
      type === 'horizontal' && dashed && !children,
    'divider-h divider_horizontal':
      type === 'horizontal' && !dashed && !children,
    'divider-v divider_vertical divider-v_dashed':
      type === 'vertical' && dashed,
    'divider-v divider_vertical': type === 'vertical' && !dashed,
  });
};

/**
 * Компонент Divider
 * @param {string} className - Пользовательский className css (default : '');
 * @param {object} style - css стили Divider. Необходимые параметры width-для горизонтального, height-для вертикального (default : none);
 * @param {boolean} dashed - вкл/откл путктирной линии на Divider (default : false);
 * @param {string} position - Позиционирование заголовка. Работает только на горизонтальном Divider и при наличии заголовка (default : 'left');
 * @param {string} type - Тип Divider horizontal или vertical (default : 'horizontal');
 * @param {string} children - Заголовок для горизонтального типа. Указывается между тегами компонента: <Divider>Заголовок</Divider> (default : none);
 * **/

export const Divider = props => {
  const { style, children } = props;
  return (
    <div className={dividerClass({ ...props })} style={style}>
      {children && <span>{children}</span>}
    </div>
  );
};

Divider.propTypes = {
  className: PropTypes.string,
  style: PropTypes.object,
  dashed: PropTypes.bool,
  position: PropTypes.string,
  type: PropTypes.string,
  children: PropTypes.string,
};

Divider.defaultProps = {
  dashed: false,
  position: 'left',
  type: 'horizontal',
};
