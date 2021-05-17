import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

export const InputIcon = ({ hoverable, clickable, onClick, children }) => (
    <span
        className={classNames('n2o-input-icon', { hoverable, clickable })}
        onClick={onClick}
    >
        {children}
    </span>
)

InputIcon.propTypes = {
    hoverable: PropTypes.bool,
    clickable: PropTypes.bool,
    onClick: PropTypes.func,
    children: PropTypes.any,
}

InputIcon.defaultProps = {
    hoverable: false,
    clickable: false,
}

export default InputIcon
