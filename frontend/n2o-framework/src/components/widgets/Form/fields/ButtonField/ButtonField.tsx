import React, { useContext } from 'react'
import { useSelector } from 'react-redux'

import { FieldAlignmentBlock } from '../FieldAlignmentBlock'
import { FactoryStandardButton, type Props } from '../../../../buttons/FactoryStandardButton'
import { FormContext } from '../../../../core/FormProvider/provider'
import { buttonSelector } from '../../../../../ducks/toolbar/selectors'
import { State } from '../../../../../ducks/State'

export function ButtonField(props: Props) {
    const { visible = true, id } = props
    const { formName } = useContext(FormContext) || {}
    const {
        isInit,
        disabled: buttonDisabled,
    } = useSelector((state: State) => buttonSelector(state, formName as string, id as string))

    if (!visible) { return null }

    const { noLabelBlock, children, labelPosition, disabled: fieldDisabled, ...rest } = props
    const isTopAlign = !noLabelBlock && (
        labelPosition === 'top' || labelPosition === 'top-right' || labelPosition === 'top-left'
    )

    return (
        <>
            <FieldAlignmentBlock visible={isTopAlign} />
            <div className="n2o-button-field n2o-form-group">
                {children || (
                    <FactoryStandardButton
                        {...rest}
                        entityKey={formName}
                        disabled={isInit && (buttonDisabled || fieldDisabled)}
                        labelPosition={labelPosition}
                    />
                )}
                <div className="n2o-validation-message" />
            </div>
        </>
    )
}

export default ButtonField
