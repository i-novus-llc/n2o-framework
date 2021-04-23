import React from 'react'
import Button from 'reactstrap/lib/Button'

function DropdownCustomItem({ color, ...rest }) {
    return <Button {...rest} className="dropdown-item-btn" />
}

export default DropdownCustomItem
