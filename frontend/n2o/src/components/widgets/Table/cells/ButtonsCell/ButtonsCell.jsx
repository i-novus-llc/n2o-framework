import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
import get from 'lodash/get';
import Actions from '../../../../actions/Actions';

/**
 *
 * @param id
 * @param className
 * @param callActionImpl
 * @param visible
 * @param actions
 * @param toolbar
 * @param model
 * @param other
 * @returns {*}
 * @constructor
 */
function ButtonsCell({
  id,
  className,
  visible,
  model,
  style,
  toolbar,
  actions,
  ...other
}) {
  const key = `${id || 'buttonCell'}_${get(model, 'id', 1)}`;
  return visible ? (
    <Actions
      className={cx('n2o-buttons-cell', className)}
      style={style}
      toolbar={toolbar}
      actions={actions}
      containerKey={key}
    />
  ) : null;
}

ButtonsCell.propTypes = {
  /**
   * Кдасс
   */
  className: PropTypes.string,
  /**
   * Стили
   */
  style: PropTypes.object,
  /**
   * ID ячейки
   */
  id: PropTypes.string,
  /**
   * Флаг видимости
   */
  visible: PropTypes.bool,
};

ButtonsCell.defaultProps = {
  visible: true,
};

export { ButtonsCell };
export default ButtonsCell;
