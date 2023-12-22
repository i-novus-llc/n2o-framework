import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import StandardButton from '../../../../buttons/StandardButton/StandardButton'

function ButtonField({ className, style, visible, ...rest }) {
    return (
        visible && (
            <div className={classNames('n2o-button-field n2o-form-group', className)}>
                <StandardButton {...rest} style={style} className={className} />
            </div>
        )
    )
}

ButtonField.propTypes = {
    visible: PropTypes.bool,
}

ButtonField.defaultProps = {
    visible: true,
}

export default ButtonField
