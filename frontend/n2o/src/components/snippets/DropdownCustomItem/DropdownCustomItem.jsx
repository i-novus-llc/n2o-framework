import React from 'react';
import { Button } from 'reactstrap';

function DropdownCustomItem({ color, ...rest }) {
  return <Button {...rest} className="dropdown-item-btn" />;
}

export default DropdownCustomItem;
