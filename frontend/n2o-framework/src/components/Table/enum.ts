export const enum Action {
    SelectSingleRow = 'SelectSingleRow',
    SelectRow = 'SelectRow',
    DeselectRow = 'DeselectRow',
    SelectAllRow = 'SelectAllRow',
    DeselectAllRow = 'DeselectAllRow',
    UpdateSortDirection = 'UpdateSortDirection',
    UpdateTreeExpanded = 'UpdateTreeExpanded',
    RowClick = 'RowClick',
}

export const enum Selection {
    Checkbox = 'checkbox',
    Radio = 'radio',
    None = 'none',
    Row = 'active',
}

export const enum ChildrenToggleState {
    Expand = 'expand',
    Collapse = 'collapse',
}

export const enum TableActions {
    toggleExpandRow = 'toggleExpandRow',
    selectRows = 'selectRows',
    deselectRows = 'deselectRows',
    selectSingleRow = 'selectSingleRow',
    setFocusOnRow = 'setFocusOnRow',
    onRowClick = 'onRowClick',
    onChangeFilter = 'onChangeFilter',
}
