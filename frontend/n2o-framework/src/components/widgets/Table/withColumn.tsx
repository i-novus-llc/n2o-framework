import React, { useEffect, ComponentType, memo, useCallback, useMemo } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import classNames from 'classnames'

import propsResolver from '../../../utils/propsResolver'
import { registerColumn } from '../../../ducks/columns/store'
import { isInitSelector, isVisibleSelector, isDisabledSelector } from '../../../ducks/columns/selectors'
import { State } from '../../../ducks/State'

export interface Props {
    columnId: string
    widgetId: string
    label: string
    conditions?: Record<string, unknown>
    model?: Record<string, unknown>
    className?: string
    disabled?: boolean
}

function withColumn<P>(WrappedComponent: ComponentType<P>) {
    const ColumnContainer: ComponentType<Props> = (props) => {
        const dispatch = useDispatch()

        const { widgetId, columnId } = props

        const columnIsInit = useSelector((state: State) => isInitSelector(widgetId, columnId)(state))
        const columnVisible = useSelector((state: State) => {
            const visible = isVisibleSelector(widgetId, columnId)(state)

            return visible === undefined ? true : visible
        })

        const columnDisabled = useSelector((state: State) => {
            const disabled = isDisabledSelector(widgetId, columnId)(state)

            return disabled === undefined ? false : disabled
        })

        const registerColumnHandler = useCallback(() => {
            if (!columnIsInit) {
                const { widgetId, columnId, label, conditions } = props

                dispatch(registerColumn(widgetId, columnId, label, columnVisible, columnDisabled, conditions))
            }
        }, [
            columnIsInit,
            columnVisible,
            columnDisabled,
            widgetId,
            columnId,
        ])

        useEffect(registerColumnHandler, [registerColumnHandler])

        const { model } = props
        const resolvedProps = useMemo(() => propsResolver(props, model, ['toolbar']), [props, model]) as P & Props

        if (!columnVisible) { return null }

        return (
            <WrappedComponent
                disabled={columnDisabled}
                {...resolvedProps}
                className={classNames('n2o-widget-list-cell', resolvedProps.className)}
            />
        )
    }

    return memo(ColumnContainer)
}

export default withColumn
