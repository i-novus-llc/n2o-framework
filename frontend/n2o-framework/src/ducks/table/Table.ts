export interface Table {
    textWrap?: boolean
    columns: Record<string, { visible: boolean }>
}

export type State = Record<string, Table>
