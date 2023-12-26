import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import StandardButton from '../../../../buttons/StandardButton/StandardButton'
import { useFormContext } from '../../../../core/FormProvider'
import { FieldAlignmentBlock } from '../FieldAlignmentBlock'

function ButtonField({ className, style, visible, noLabelBlock, ...rest }) {
    const { labelPosition } = rest
    const { prefix, formName } = useFormContext()

    const isTopLabelPosition = labelPosition === 'top' || labelPosition === 'top-right' || labelPosition === 'top-left'
    const isTopAlign = !noLabelBlock && isTopLabelPosition

    return (
        visible && (
            <>
                <FieldAlignmentBlock visible={isTopAlign} />
                <div
                    className={classNames('n2o-button-field n2o-form-group', className)}
                >
                    <StandardButton
                        {...rest}
                        style={style}
                        className={className}
                        entityKey={`${formName}/${prefix}`}
                    />
                    <div className="n2o-validation-message" />
                </div>
            </>
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
