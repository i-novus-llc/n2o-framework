interface ColumnProps {
    columnId: string
    label: string
    icon: string
    visible: boolean
    visibleState: string
}

type ColumnState = ColumnProps[]

// TODO таблица не типизирована, нужно взять от туда, после рефакторинга
export interface TableContextProps {
    columnsState: ColumnState
    changeColumnParam(widgetId: string, columnId: string, visibleState: ColumnProps['visibleState'], visible: ColumnProps['visible']): void
    switchTableParam(widgetId: string, param: string): void
}

export interface Props {
    // eslint-disable-next-line react/no-unused-prop-types
    id: string
    icon?: string
    label?: string
    entityKey: string
    defaultColumns: string
    nested: string
}
