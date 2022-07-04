import React from 'react'
import cn from 'classnames'
import PropTypes from 'prop-types'

function DefaultCell({ tag, className, disabled, ...rest }) {
    const classnames = cn('default-cell', className, { 'n2o-disabled': disabled })

    return React.createElement(tag, { ...rest, className: classnames })
}

DefaultCell.propTypes = {
    tag: PropTypes.string,
    className: PropTypes.string,
    disabled: PropTypes.bool,
    children: PropTypes.node,
}

DefaultCell.defaultProps = {
    tag: 'div',
    disabled: false,
}

export default DefaultCell
