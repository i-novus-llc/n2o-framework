import React from 'react'

import { Action } from '../Action'

import { RegisterButtonProps } from './Toolbar'

export interface ToolbarCommon {
    key: string
    buttonId: string
}

export interface ChangeButtonVisibilityPayload extends ToolbarCommon {
    visible: boolean
}

export interface ChangeButtonTitlePayload extends ToolbarCommon {
    title: string
}

export interface ChangeButtonCountPayload extends ToolbarCommon {
    count: number
}

export interface ChangeButtonSizePayload extends ToolbarCommon {
    size: string
}

export interface ChangeButtonColorPayload extends ToolbarCommon {
    color: string
}

export interface ChangeButtonHintPayload extends ToolbarCommon {
    hint: string
}

export interface ChangeButtonMessagePayload extends ToolbarCommon {
    message: string
}

export interface ChangeButtonIconPayload extends ToolbarCommon {
    icon: string
}

export interface ChangeButtonClassPayload extends ToolbarCommon {
    className: string
}

export interface ChangeButtonStylePayload extends ToolbarCommon {
    style: React.CSSProperties
}

export interface ChangeButtonStyleDisabledPayload extends ToolbarCommon {
    disabled: boolean
}

export interface PrintPayload {
    url: string
    pathMapping: string
    queryMapping: string
    printable: boolean
    type: string
    keepIndent: boolean
    documentTitle: string
    loader: boolean
    loaderText: string
    base64: boolean
}

export type ChangeButtonVisibility = Action<string, ChangeButtonVisibilityPayload>
export type ChangeButtonTitle = Action<string, ChangeButtonTitlePayload>
export type ChangeButtonCount = Action<string, ChangeButtonCountPayload>
export type ChangeButtonSize = Action<string, ChangeButtonSizePayload>
export type ChangeButtonColor = Action<string, ChangeButtonColorPayload>
export type ChangeButtonHint = Action<string, ChangeButtonHintPayload>
export type ChangeButtonMessage = Action<string, ChangeButtonMessagePayload>
export type ChangeButtonIcon = Action<string, ChangeButtonIconPayload>
export type ChangeButtonClass = Action<string, ChangeButtonClassPayload>
export type ChangeButtonStyle = Action<string, ChangeButtonStylePayload>
export type ChangeButtonDisabled = Action<string, ChangeButtonStyleDisabledPayload>
export type ToggleButtonParam = Action<string, ToolbarCommon>
export type RegisterButton = Action<string, RegisterButtonProps & ToolbarCommon>
export type Print = Action<string, PrintPayload>
