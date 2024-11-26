import React, { CSSProperties, ReactNode } from 'react'

import StandardButton from '../../../../buttons/StandardButton/StandardButton'
import { FieldAlignmentBlock } from '../FieldAlignmentBlock'

interface Props {
    className?: string
    style?: CSSProperties
    visible: boolean
    noLabelBlock?: boolean
    children: ReactNode
    labelPosition?: 'top' | 'top-right' | 'top-left' | 'bottom'
}

export function ButtonField(props: Props) {
    const { visible = true } = props

    if (!visible) { return null }

    const { noLabelBlock, children, labelPosition, ...rest } = props
    const isTopAlign = !noLabelBlock && (labelPosition === 'top' || labelPosition === 'top-right' || labelPosition === 'top-left')

    return (
        <>
            <FieldAlignmentBlock visible={isTopAlign} />
            <div className="n2o-button-field n2o-form-group">
                {children ||
                    <StandardButton {...rest} labelPosition={labelPosition} />
                }
                <div className="n2o-validation-message" />
            </div>
        </>
    )
}

export default ButtonField
