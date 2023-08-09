import React from 'react'

import { Condition } from '../../sagas/conditions'

export interface Conditions {
    enabled: Condition[]
    visible: Condition[]
}

export interface RegisterButtonProps {
    count: number | null
    resolveEnabled: boolean
    visible: boolean
    disabled: boolean
    containerKey: string
    conditions: Conditions
    hintPosition: string
}

export type IButton = {
    isInit: boolean
    visible: boolean
    size: string | null
    color: string | null
    title: string | null
    hint: string | null
    icon: string | null
    disabled: boolean
    loading: boolean
    error: string | null
    conditions?: Conditions
    key: string
    buttonId: string
    count?: number
    message?: string
    className?: string
    hintPosition?: string
    style?: React.CSSProperties
    resolveEnabled?: {
        modelLink: string
        on: string[]
    }
}

export type ButtonContainer = Record<string, IButton>

export type State = Record<string, ButtonContainer>
