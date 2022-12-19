import isEmpty from 'lodash/isEmpty'
import { Action, Dispatch } from 'redux'
import React from 'react'

import evalExpression from '../../../../../utils/evalExpression'

type modelValue = unknown

export interface IModel {
    [key: string]: modelValue
}

export interface IActions {
    [actionId: string]: Action
}

export type IMarkdownFieldMappers = Record<string, React.FunctionComponent<IReactMarkdownExtendedProps>>

export interface IReactMarkdownProps {
    id: string,
    text: string,
    visible: boolean,
    enable: boolean,
}

export interface IMappedComponentExtraProps {
    actions: IActions,
    dispatch: Dispatch,
    model: IModel,
    fieldDisabled: boolean,
}

export type IReactMarkdownExtendedProps =
    IReactMarkdownProps &
    IMappedComponentExtraProps &
    Partial<{
        onClick(): void,
        disabled: boolean,
        action: Action,
        className: string,
        placement: 'top'|'right'|'bottom'|'left'
    }>

/* для MarkdownField выражение вычисляем с помощью {}, а не `` как в остальных случаях */
export function hasExpression(value: modelValue): boolean {
    if (typeof value !== 'string') {
        return false
    }

    return value.startsWith('{') && value.endsWith('}')
}

export function parseExpression(value: string): string {
    return value.substring(1, value.length - 1)
}

export function resolveExpressions<TProps extends Record<string, unknown>>(props: TProps, model: object): TProps {
    const entries = Object.entries(props)
    const newEntries = entries.map(([key, value]) => {
        if (hasExpression(value)) {
            const expression = parseExpression(value as string)

            return [key, evalExpression(expression, model)]
        }

        if (key === 'enable') {
            const { fieldDisabled } = props
            /* вычисление из за того что бэкэнд не пришлет условие !_.isEmpty(this)  */
            const disableOnEmptyModel = props['disable-on-empty-model'] === 'true'

            const newValue = disableOnEmptyModel
                ? evalExpression(value as string, model) && !fieldDisabled && !isEmpty(model)
                : evalExpression(value as string, model) && !fieldDisabled

            return [key, newValue]
        }

        if (key === 'visible') {
            return [key, evalExpression(value as string, model)]
        }

        return [key, value]
    })

    return Object.fromEntries(newEntries)
}
