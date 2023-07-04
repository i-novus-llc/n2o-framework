/* eslint-disable @typescript-eslint/no-explicit-any */
export type ExpandedRows = string[]
export type SelectedRows = string[]
export type DataItem = {
    id: string
    children?: DataItem[]
    [x: string]: any
}

export type Data = DataItem[]
