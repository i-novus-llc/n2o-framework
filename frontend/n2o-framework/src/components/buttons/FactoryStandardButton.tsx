import React, { CSSProperties, FunctionComponent, ReactNode, useContext } from 'react'

import { type ModelPrefix } from '../../core/datasource/const'
import { FactoryContext } from '../../core/factory/context'
import { FactoryLevels } from '../../core/factory/factoryLevels'

export enum Color {
    danger = 'danger', dark = 'dark', info = 'info',
    light = 'light', primary = 'primary', secondary = 'secondary',
    success = 'success', warning = 'warning', white = 'white', link = 'link',
}

export enum LabelPosition {
    top = 'top',
    topRight = 'top-right',
    topLeft = 'top-left',
    bottom = 'bottom',
}

export interface Props {
    label?: string
    style?: CSSProperties
    className?: string
    onClick?(): void
    color?: Color
    noLabelBlock?: boolean
    id?: string
    modelPrefix?: ModelPrefix
    datasource?: string
    disabled?: boolean
    visible?: boolean
    labelPosition?: LabelPosition
    children?: ReactNode
    entityKey?: string
    icon?: string | null | ReactNode
}

export function FactoryStandardButton({
    modelPrefix,
    ...rest
}: Props) {
    const { getComponent } = useContext(FactoryContext)
    const Button: FunctionComponent<object> | void = getComponent('StandardButton', FactoryLevels.BUTTONS)

    if (!Button) { return null }

    const button = { model: modelPrefix }

    return <Button {...button} {...rest} />
}
