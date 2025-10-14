import React from 'react'

import { FieldAlignmentBlock } from '../FieldAlignmentBlock'
import { FactoryStandardButton, type Props } from '../../../../buttons/FactoryStandardButton'

export function ButtonField(props: Props) {
    const { visible = true } = props

    if (!visible) { return null }

    const { noLabelBlock, children, labelPosition, ...rest } = props
    const isTopAlign = !noLabelBlock && (labelPosition === 'top' || labelPosition === 'top-right' || labelPosition === 'top-left')

    return (
        <>
            <FieldAlignmentBlock visible={isTopAlign} />
            <div className="n2o-button-field n2o-form-group">
                {children || <FactoryStandardButton {...rest} labelPosition={labelPosition} />}
                <div className="n2o-validation-message" />
            </div>
        </>
    )
}

export default ButtonField
