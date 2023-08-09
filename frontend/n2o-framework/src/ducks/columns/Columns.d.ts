import { ICondition } from '../../sagas/conditions'

interface Conditions {
    visible?: ICondition[]
}

export interface Column {
    isInit: boolean
    visible: boolean
    disabled: boolean
    frozen: boolean
    key: string
    columnId: string
    label?: string
    conditions?: Conditions
}

export type Columns = Record<string, Column>
export type State = Record<string, Columns>
