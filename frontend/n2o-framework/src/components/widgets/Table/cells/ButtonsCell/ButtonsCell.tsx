import React, { ComponentType, useContext, useMemo } from 'react'
import get from 'lodash/get'
import flowRight from 'lodash/flowRight'
import classNames from 'classnames'

import { useResolved } from '../../../../../core/Expression/useResolver'
import { replaceIndex } from '../../../../../core/datasource/ArrayField/replaceIndex'
import { ArrayFieldContext } from '../../../../../core/datasource/ArrayField/Context'
import { Toolbar } from '../../../../buttons/Toolbar'
import { WithCell } from '../../withCell'
import { DefaultCell } from '../DefaultCell'
import { TooltipHocProps } from '../../../../snippets/Tooltip/TooltipHOC'

import { type ButtonCellProps } from './types'

function ButtonsCellBody({
    id,
    className,
    model,
    toolbar,
    onResolve,
    tooltipFieldId,
    visible = true,
    disabled = false,
    placement = 'bottom',
}: ButtonCellProps) {
    const key = `${id || 'buttonCell'}_${get(model, 'id', 1)}`
    const context = useContext(ArrayFieldContext)
    const resolvedToolbar = useResolved(toolbar, model)
    const withIndex = useMemo(() => (resolvedToolbar.map(toolbar => ({
        ...toolbar,
        buttons: toolbar.buttons.map(button => ({
            ...button,
            conditions: button.conditions && replaceIndex(button.conditions, context),
        })),
    }))), [resolvedToolbar, context])

    if (!visible) { return null }

    const hint = get(model, tooltipFieldId, null) as TooltipHocProps['hint']

    return (
        <DefaultCell disabled={disabled} className={classNames('d-inline-flex', className)}>
            <Toolbar
                className="n2o-buttons-cell"
                entityKey={key}
                toolbar={withIndex}
                onClick={onResolve}
                placement={placement}
                hint={hint}
            />
        </DefaultCell>
    )
}

type ExtendedWithTooltip = ComponentType<ButtonCellProps & { isControlledTooltip: boolean }>

function WithOnControlledTooltip(Component: ComponentType<ButtonCellProps>): ExtendedWithTooltip {
    return function Wrapper(props) {
        return <Component {...props} isControlledTooltip />
    }
}

type ExtendedWithOnResolve = ComponentType<ButtonCellProps & { onResolve(): void }>

function WithOnResolve(Component: ComponentType<ButtonCellProps>): ExtendedWithOnResolve {
    return function Wrapper(props) {
        const { callAction, model } = props

        const onResolve = () => {
            if (callAction && model) {
                callAction(model)
            }
        }

        return <Component {...props} onResolve={onResolve} />
    }
}

export const ButtonsCell = flowRight(
    WithOnControlledTooltip,
    WithCell,
    WithOnResolve,
)(ButtonsCellBody)

export default ButtonsCell
