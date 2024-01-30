import React from 'react'
import PropTypes from 'prop-types'

export function FieldAlignmentBlock(props) {
    const { visible = true } = props

    if (!visible) {
        return null
    }

    return <div className="n2o-field-alignment" />
}

FieldAlignmentBlock.propTypes = {
    visible: PropTypes.bool,
}
