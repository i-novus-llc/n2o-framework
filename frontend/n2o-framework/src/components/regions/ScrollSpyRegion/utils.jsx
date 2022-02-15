import React from 'react'
import PropTypes from 'prop-types'

export function Title({
    id,
    title,
    className,
    visible = true,
}) {
    if (!title || !visible) {
        return null
    }

    return <div id={id} className={className}>{title}</div>
}

Title.propTypes = {
    id: PropTypes.string,
    title: PropTypes.string,
    className: PropTypes.string,
    visible: PropTypes.bool,
}
