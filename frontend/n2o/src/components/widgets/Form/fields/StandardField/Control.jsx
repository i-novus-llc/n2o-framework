import React from 'react';
import PropTypes from 'prop-types';

import Factory from '../../../../../core/factory/Factory';
import { CONTROLS } from '../../../../../core/factory/factoryLevels';

/**
 * Контрол поля формы
 * @param {object} props - пропсы
 * @param {component|string} component - компонент контрола
 * @param {component|string} className - css стили
 * @example
 * <Control component={Input} {...props}/>
 */
const Control = ({ component, className, ...props }) => {
  return typeof component !== 'string' ? (
    React.createElement(component, { ...props, className })
  ) : (
    <Factory level={CONTROLS} src={component} {...props} className={`form-control ${className}`} />
  );
};

Control.propTypes = {
  component: PropTypes.node,
  style: PropTypes.object,
  className: PropTypes.string
};

export default Control;
