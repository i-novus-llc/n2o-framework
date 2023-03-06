export type TColumnsState = Record<string, TColumnContainer>

export type TColumnContainer = Record<string, TColumn>

export type TColumn = {
    isInit: boolean
    visible: boolean
    disabled: boolean
    frozen: boolean
    key: string
    columnId: string
    label: string
    conditions: object
}
