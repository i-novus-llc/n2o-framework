import React from 'react';
import PropTypes from 'prop-types';
import { map, get } from 'lodash';
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
  ...other
}) {
  const handlerClick = (e, action) => {
    e.stopPropagation();
    callActionImpl(e, { action });
  };

  const createGroupItems = ({ subMenu, ...rest }) =>
    subMenu ? (
      <HintDropDown
        positionFixed={positionFixed}
        modifiers={modifiers}
        menu={subMenu}
        onClick={handlerClick}
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
        {...other}
      >
        {map(buttons, createGroupItems)}
      </ButtonGroup>
    )
  );
}

ButtonsCell.propTypes = {
  size: PropTypes.string,
  className: PropTypes.string,
  style: PropTypes.object,
  id: PropTypes.string,
  visible: PropTypes.bool,
};

ButtonsCell.defaultProps = {
  size: 'sm',
  visible: true,
};

export default withCell(ButtonsCell);
