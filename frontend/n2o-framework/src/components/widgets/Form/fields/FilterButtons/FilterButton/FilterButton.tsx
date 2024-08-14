import React, { CSSProperties, useContext } from 'react'
import isUndefined from 'lodash/isUndefined'

import { FactoryContext } from '../../../../../../core/factory/context'
import { FactoryLevels } from '../../../../../../core/factory/factoryLevels'
import { ModelPrefix } from '../../../../../../core/datasource/const'
import { ButtonField } from '../../ButtonField/ButtonField'

enum Color {
    danger = 'danger', dark = 'dark', info = 'info',
    light = 'light', primary = 'primary', secondary = 'secondary',
    success = 'success', warning = 'warning', white = 'white',
}

export interface Props {
    label: string
    style?: CSSProperties
    className?: string
    onClick(): void
    color: Color
    form?: string
    disabled?: boolean
    visible: boolean
    noLabelBlock?: boolean
    labelPosition?: 'top' | 'top-right' | 'top-left' | 'bottom'
    id: string
    datasource: string
    modelPrefix: ModelPrefix
}

export function FilterButton(props: Props) {
    const { getComponent } = useContext(FactoryContext)
    const Button: React.FunctionComponent<object> | void = getComponent('StandardButton', FactoryLevels.BUTTONS)

    if (!Button) { return null }

    const {
        label, style, className, onClick, color, noLabelBlock, disabled = false, visible = true,
        id, modelPrefix, datasource,
    } = props
    const button = { id, model: modelPrefix, datasource, label, style, className, disabled, visible, onClick, color }

    if (isUndefined(noLabelBlock)) { return <Button {...button} /> }

    return <ButtonField labelPosition="top" {...props}><Button {...button} /></ButtonField>
}
