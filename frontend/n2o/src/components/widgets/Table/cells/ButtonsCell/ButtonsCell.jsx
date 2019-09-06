import React from 'react';
import PropTypes from 'prop-types';
import { map, get, omit } from 'lodash';
import { ButtonGroup } from 'reactstrap';
import HintButton from './HintButton';
import HintDropDown from './HintDropDown';
import cx from 'classnames';
import withCell from '../../withCell';

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
  ...other
}) {
  const handlerClick = (e, action) => {
    e.stopPropagation();
    callActionImpl(e, { action });
  };

  const createGroupItems = ({ subMenu, ...rest }, i) =>
    subMenu ? (
      <HintDropDown
        key={i}
        positionFixed={positionFixed}
        modifiers={modifiers}
        menu={subMenu}
        onClick={handlerClick}
        resolveWidget={resolveWidget}
        model={model}
        {...rest}
      />
    ) : (
      <HintButton onClick={handlerClick} {...rest} />
    );

  return (
    visible && (
      <ButtonGroup
        key={id}
        className={cx('n2o-buttons-cell', className)}
        {...omit(other, [
          'columnId',
          'dispatch',
          'fieldKey',
          'updateFieldInModel',
          'callInvoke',
        ])}
      >
        {map(buttons, createGroupItems)}
      </ButtonGroup>
    )
  );
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
