import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
import get from 'lodash/get';
import Toolbar from "../../../../buttons/Toolbar";

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
 * @return {null}
 */
function ButtonsCell({
  id,
  className,
  visible,
  model,
  style,
  toolbar,
  actions,
  widgetId,
  ...other
}) {
  const key = `${id || 'buttonCell'}_${get(model, 'id', 1)}`;

  return visible ? (
    <Toolbar
      className={cx('n2o-buttons-cell', className)}
      entityKey={key}
      toolbar={toolbar}
    />
  ) : null;
}

ButtonsCell.propTypes = {
  /**
   * Класс
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
