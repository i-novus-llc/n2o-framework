import React from 'react'
import PropTypes from 'prop-types'

import { withFieldsetHeader } from './withFieldsetHeader'
import DefaultFieldset from './DefaultFieldset'

function BlankFieldset({ render, rows, disabled }) {
    return <DefaultFieldset disabled={disabled} className="blank-fieldset">{render(rows)}</DefaultFieldset>
}

BlankFieldset.propTypes = {
    rows: PropTypes.array,
    render: PropTypes.func,
    disabled: PropTypes.bool,
}

BlankFieldset.defaultProps = {
    rows: [],
    render: () => {},
    disabled: false,
}

export default withFieldsetHeader(BlankFieldset)
