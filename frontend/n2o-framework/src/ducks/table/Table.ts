export interface Table {
    textWrap?: boolean
    columns: Record<string, { visible: boolean }>
    isDefaultColumns: boolean
}

export type State = Record<string, Table>
