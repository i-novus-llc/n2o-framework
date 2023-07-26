import React from 'react'

import { Action } from '../Action'

import { IRegisterButtonProps } from './Toolbar'

export interface IToolbarCommon {
    key: string
    buttonId: string
}

export interface IChangeButtonVisibilityPayload extends IToolbarCommon {
    visible: boolean
}

export interface IChangeButtonTitlePayload extends IToolbarCommon {
    title: string
}

export interface IChangeButtonCountPayload extends IToolbarCommon {
    count: number
}

export interface IChangeButtonSizePayload extends IToolbarCommon {
    size: string
}

export interface IChangeButtonColorPayload extends IToolbarCommon {
    color: string
}

export interface IChangeButtonHintPayload extends IToolbarCommon {
    hint: string
}

export interface IChangeButtonMessagePayload extends IToolbarCommon {
    message: string
}

export interface IChangeButtonIconPayload extends IToolbarCommon {
    icon: string
}

export interface IChangeButtonClassPayload extends IToolbarCommon {
    className: string
}

export interface IChangeButtonStylePayload extends IToolbarCommon {
    style: React.CSSProperties
}

export interface IChangeButtonStyleDisabledPayload extends IToolbarCommon {
    disabled: boolean
}

export interface IPrintPayload {
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

export type ChangeButtonVisibility = Action<string, IChangeButtonVisibilityPayload>
export type ChangeButtonTitle = Action<string, IChangeButtonTitlePayload>
export type ChangeButtonCount = Action<string, IChangeButtonCountPayload>
export type ChangeButtonSize = Action<string, IChangeButtonSizePayload>
export type ChangeButtonColor = Action<string, IChangeButtonColorPayload>
export type ChangeButtonHint = Action<string, IChangeButtonHintPayload>
export type ChangeButtonMessage = Action<string, IChangeButtonMessagePayload>
export type ChangeButtonIcon = Action<string, IChangeButtonIconPayload>
export type ChangeButtonClass = Action<string, IChangeButtonClassPayload>
export type ChangeButtonStyle = Action<string, IChangeButtonStylePayload>
export type ChangeButtonDisabled = Action<string, IChangeButtonStyleDisabledPayload>
export type ToggleButtonParam = Action<string, IToolbarCommon>
export type RegisterButton = Action<string, IRegisterButtonProps & IToolbarCommon>
export type Print = Action<string, IPrintPayload>
