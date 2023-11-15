import { Condition } from '../../sagas/conditions'

interface Conditions {
    visible?: Condition[]
}

export interface Column {
    isInit: boolean
    visible: boolean
    visibleState: boolean
    disabled: boolean
    frozen: boolean
    key: string
    columnId: string
    label?: string
    conditions?: Conditions
}

export type Columns = Record<string, Column>
export type State = Record<string, Columns>
