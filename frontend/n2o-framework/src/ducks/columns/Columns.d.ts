interface IConditions {
    visible?: boolean
}

export interface IColumn {
    isInit: boolean
    visible: boolean
    disabled: boolean
    frozen: boolean
    key: string
    columnId: string
    label?: string
    conditions?: IConditions
}

export type Columns = Record<string, IColumn>
export type State = Record<string, Columns>
