import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import StandardButton from '../../../../buttons/StandardButton/StandardButton'
import { FieldAlignmentBlock } from '../FieldAlignmentBlock'

function ButtonField({ className, style, noLabelBlock, children, visible = true, ...rest }) {
    if (!visible) { return null }

    const { labelPosition } = rest

    const isTopLabelPosition = labelPosition === 'top' || labelPosition === 'top-right' || labelPosition === 'top-left'
    const isTopAlign = !noLabelBlock && isTopLabelPosition

    return (
        <>
            <FieldAlignmentBlock visible={isTopAlign} />
            <div style={style} className={classNames('n2o-button-field n2o-form-group', className)}>
                {children || <StandardButton {...rest} className={className} />}
                <div className="n2o-validation-message" />
            </div>
        </>
    )
}

ButtonField.propTypes = {
    visible: PropTypes.bool,
}

ButtonField.defaultProps = {
    visible: true,
}

export default ButtonField
