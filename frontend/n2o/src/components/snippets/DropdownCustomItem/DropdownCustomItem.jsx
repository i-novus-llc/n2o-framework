import React from 'react';
import { Button, DropdownItem } from 'reactstrap';
import { BTN_COLORS } from './constants';

function DropdownCustomItem({ color, ...rest }) {
  return color && BTN_COLORS.includes(color) ? (
    <Button {...rest} color={color} className="dropdown-item-btn" />
  ) : (
    <DropdownItem {...rest} />
  );
}

export default DropdownCustomItem;
