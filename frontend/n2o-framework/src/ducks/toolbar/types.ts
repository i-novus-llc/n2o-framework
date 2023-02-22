/* eslint-disable @typescript-eslint/no-explicit-any */
export type TToolbarState = Record<string, TToolbarButtonContainer>

export type TToolbarButtonContainer = Record<string, TToolbarButton>

// TODO: Дописать тип
export type TToolbarButton = {
    isInit: boolean
    visible: boolean
    size: unknown
    color: string
    title: string
    hint: string
    message: unknown
    icon: string
    loading: boolean
    error: unknown
    conditions: string
    key: string
    buttonId: string
    disabled: boolean
    count: number
    hintPosition: string
    style: unknown
    className: string
}
