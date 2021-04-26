import React from 'react'
import PropTypes from 'prop-types'

function BlankFieldset({ render, rows }) {
    return <div className="blank-fieldset">{render(rows)}</div>
}

BlankFieldset.propTypes = {
    rows: PropTypes.array,
    render: PropTypes.func,
}

BlankFieldset.defaultProps = {
    rows: [],
    render: () => {},
}

export default BlankFieldset
