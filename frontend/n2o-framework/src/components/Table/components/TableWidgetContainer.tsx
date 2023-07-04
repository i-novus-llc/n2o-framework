import React, { VFC } from 'react'

import { TableWidgetContainerProps } from '../models/props'
import { TableActionsProvider } from '../provider/TableActions'

import { TableWidget } from './TableWidget'

export const TableWidgetContainer: VFC<TableWidgetContainerProps> = ({
    id,
    tableConfig,
    data,
    sorting,
    cells,
    hasSecurityAccess,
    isTextWrap,
    focusedRowValue,
    expandedRows,
    selectedRows,
    actionListener,
}) => (
    <TableActionsProvider
        actionListener={actionListener}
        widgetId={id}
    >
        <div
            data-text-wrap={isTextWrap}
            className="table-container"
            style={{
                width: tableConfig.width,
                height: tableConfig.height,
            }}
        >
            <TableWidget
                hasSecurityAccess={hasSecurityAccess}
                focusedRowValue={focusedRowValue}
                rowRenderFieldKey="id"
                selectedKey="id"
                treeDataKey="children"
                selection={tableConfig.rowSelection}
                tableId={id}
                bodyRow={tableConfig.body.row}
                bodyCell={cells.body}
                headerRow={tableConfig.header.row}
                headerCell={cells.header}
                sorting={sorting}
                expandedRows={expandedRows}
                selectedRows={selectedRows}
                data={data}
            />
        </div>
    </TableActionsProvider>
)

TableWidgetContainer.defaultProps = {
    data: [],
    hasSecurityAccess: true,
    focusedRowValue: null,
    expandedRows: [],
    selectedRows: [],
    actionListener: () => {},
}

TableWidgetContainer.displayName = 'TableWidgetContainer'
