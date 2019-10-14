import React from 'react';
import PropTypes from 'prop-types';
import { map, get, omit } from 'lodash';
import { ButtonGroup } from 'reactstrap';
import HintButton from './HintButton';
import HintDropDown from './HintDropDown';
import cx from 'classnames';
import withCell from '../../withCell';
import Actions from '../../../../actions/Actions';

/**
 *
 * @param id
 * @param className
 * @param callActionImpl
 * @param buttons
 * @param visible
 * @param positionFixed
 * @param modifiers
 * @param resolveWidget
 * @param model
 * @param other
 * @returns {*}
 * @constructor
 */
function ButtonsCell({
  id,
  className,
  callActionImpl,
  buttons,
  visible,
  positionFixed,
  modifiers,
  resolveWidget,
  model,
  style,
  toolbar,
  actions,
  ...other
}) {
  return visible ? (
    <Actions
      className={cx('n2o-buttons-cell', className)}
      style={style}
      toolbar={toolbar}
      actions={actions}
      containerKey={`${id}_${model.id}`}
    />
  ) : null;
}

ButtonsCell.propTypes = {
  /**
   * Размер кнопок
   */
  size: PropTypes.string,
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
  resolveWidget: PropTypes.func,
};

ButtonsCell.defaultProps = {
  size: 'sm',
  visible: true,
  resolveWidget: () => {},
};

export { ButtonsCell };
export default withCell(ButtonsCell);
