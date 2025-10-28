import React from 'react'

import { ButtonField } from '../../ButtonField/ButtonField'
import { FactoryStandardButton, LabelPosition, Props } from '../../../../../buttons/FactoryStandardButton'

export function FilterButton(props: Props) {
    const {
        label, style, className, onClick, color, noLabelBlock, disabled = false, visible = true,
        id, modelPrefix, datasource,
    } = props
    const button = { id, model: modelPrefix, datasource, label, style, className, disabled, visible, onClick, color }

    if (noLabelBlock === undefined) { return <FactoryStandardButton {...button} /> }

    return <ButtonField labelPosition={LabelPosition.top} {...props}><FactoryStandardButton {...button} /></ButtonField>
}
