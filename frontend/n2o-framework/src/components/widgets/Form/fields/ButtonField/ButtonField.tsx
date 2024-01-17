import React, { CSSProperties, ReactNode } from 'react'
import classNames from 'classnames'

// @ts-ignore import from js file
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

export function ButtonField({ className, style, noLabelBlock, children, visible = true, ...rest }: Props) {
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

export default ButtonField
