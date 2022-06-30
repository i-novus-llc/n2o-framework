import React from 'react'
import { Button } from 'reactstrap'
import omit from 'lodash/omit'

export function DropdownCustomItem(props) {
    return <Button {...omit(props, ['color'])} className="dropdown-item-btn" />
}

export default DropdownCustomItem
