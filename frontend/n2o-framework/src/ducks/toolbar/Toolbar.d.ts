import React from 'react'

export type Condition = {
    expression: string
    modelLink: string
    message?: string
    visible: Condition[]
}
export interface Conditions {
    enabled: Condition[]
    visible: Condition[]
}

export interface RegisterButtonProps {
    buttonId: string
    key: string
    initialState: Omit<ButtonState, 'buttonId' | 'key' | 'operations'>
}

export type ButtonState = {
    isInit: boolean
    visible: boolean
    label?: string
    size?: string
    color?: string
    title?: string
    hint?: string
    icon?: string
    disabled: boolean
    loading: boolean
    error?: string
    conditions?: Conditions
    key: string
    buttonId: string
    count?: number
    message?: string
    className?: string
    hintPosition?: string
    style?: React.CSSProperties
    /*
     * Храним список выполняемых кнопкой операций в моменте, для её блокировки
     * Нужно чтобы при завершении откатиться к состоянию, заданному в зависимостях
     */
    operations: Array<{
        operationId: string
        loading?: boolean
        message?: string
    }>
}

export type ButtonContainer = Record<string, ButtonState>

export type State = Record<string, ButtonContainer>
