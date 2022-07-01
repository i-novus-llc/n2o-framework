import React from 'react'
import cn from 'classnames'
import PropTypes from 'prop-types'

function DefaultFieldset({ className, disabled, children, ...rest }) {
    const classnames = cn('default-fieldset', className, { 'n2o-disabled': disabled })

    return (
        <div {...rest} className={classnames}>{children}</div>
    )
}

DefaultFieldset.propTypes = {
    className: PropTypes.string,
    disabled: PropTypes.bool,
    children: PropTypes.node,
}

DefaultFieldset.defaultProps = {
    disabled: false,
}

export default DefaultFieldset
