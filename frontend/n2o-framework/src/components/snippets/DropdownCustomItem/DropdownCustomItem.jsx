import React from 'react'
import PropTypes from 'prop-types'
import Button from 'reactstrap/lib/Button'

export function DropdownCustomItem({ color, ...rest }) {
    return <Button {...rest} className="dropdown-item-btn" />
}

DropdownCustomItem.propTypes = {
    color: PropTypes.any,
}

export default DropdownCustomItem
