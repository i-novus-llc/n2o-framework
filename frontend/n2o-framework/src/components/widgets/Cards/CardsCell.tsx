import React, { CSSProperties } from 'react'
import get from 'lodash/get'
import omit from 'lodash/omit'
import classNames from 'classnames'
import { Dispatch } from 'redux'

import { Factory } from '../../../core/factory/Factory'
import { CELLS } from '../../../core/factory/factoryLevels'
import { useResolved } from '../../../core/Expression/useResolver'

export interface Props {
    component?: { src: string }
    model: { id: string }
    dispatch: Dispatch
    onResolve(): void
    className?: string
    style?: CSSProperties
    index: number
    widgetId?: string
    datasource: string
    id?: string
    key?: string | number
}

/**
 * Строка Cards
 */
export function CardsCell(props: Props) {
    const { component, model, dispatch, onResolve } = props
    const resolvedProps = useResolved({ ...omit(props, ['model', 'dispatch', 'onResolve']), ...omit(component, ['src']) }, model)

    return (
        <Factory
            src={get(component, 'src')}
            level={CELLS}
            {...resolvedProps}
            model={model}
            dispatch={dispatch}
            onResolve={onResolve}
            className={classNames('n2o-cards__cell', resolvedProps.className)}
        />
    )
}

export default CardsCell
