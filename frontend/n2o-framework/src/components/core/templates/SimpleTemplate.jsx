import React from 'react'
import PropTypes from 'prop-types'

export default function SimpleTemplate({ children }) {
    return (
        <div className="application">
            <div className="application-body container-fluid">{children}</div>
        </div>
    )
}

SimpleTemplate.propTypes = {
    children: PropTypes.oneOfType([PropTypes.node, PropTypes.func]),
}
