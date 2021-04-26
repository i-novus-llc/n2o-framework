import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'

const InputIcon = ({ hoverable, clickable, onClick, children }) => (
    <span
        className={cn('n2o-input-icon', { hoverable, clickable })}
        onClick={onClick}
    >
        {children}
    </span>
)

InputIcon.propTypes = {
    hoverable: PropTypes.bool,
    clickable: PropTypes.bool,
    onClick: PropTypes.func,
}

InputIcon.defaultProps = {
    hoverable: false,
    clickable: false,
}

export default InputIcon
