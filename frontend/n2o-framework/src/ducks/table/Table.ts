export interface Table {
    textWrap?: boolean
    columns: Record<string, { visible: boolean, visibleState?: boolean, columnId?: string }>
    isDefaultColumns: boolean
}

export type State = Record<string, Table>
