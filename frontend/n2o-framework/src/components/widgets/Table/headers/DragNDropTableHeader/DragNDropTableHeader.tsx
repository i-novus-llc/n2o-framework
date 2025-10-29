import React from 'react'
import omit from 'lodash/omit'
import classNames from 'classnames'

import { TableHeaderCell } from '../../../../Table/components/header-cell'
import { type ChildrenTableHeaderProps } from '../../../../Table/types/props'
import { useTableActions } from '../../../../Table'
import { DIRECTION, useDragAndDrop } from '../../useDragAndDrop'
import { type HeaderCell, MOVE_MODE } from '../../../../../ducks/table/Table'

export function DragNDropTableHeader(props: ChildrenTableHeaderProps) {
    const {
        children = [],
        sorting,
        validateFilterField,
        filterErrors,
        id: tableId,
        widgetId,
        moveMode,
        rowSpan,
    } = props

    const { onHeaderDrop } = useTableActions()
    const isDraggable = moveMode === MOVE_MODE.TABLE || moveMode === MOVE_MODE.ALL

    const {
        onMouseDown,
        draggingId,
        draggableAttributes,
    } = useDragAndDrop({
        onDrop: (sourceId, targetId) => onHeaderDrop(tableId, sourceId, targetId),
        direction: DIRECTION.HORIZONTAL,
        targetSelector: 'th',
        ghostContainerId: widgetId, // TODO: Переделать без привязки к n2o
        dropIndicator: true,
    })

    const getDraggableStyles = (cellId: string) => ({
        ...draggableAttributes?.style,
        opacity: isDraggable && draggingId === cellId ? 0.5 : 1,
    })

    const getMergedAttributes = (child: HeaderCell) => {
        const baseAttributes = {
            ...child.elementAttributes,
            className: classNames(child.elementAttributes?.className, 'table-header-drag-handle'),
            id: child.id,
            style: {
                ...child.elementAttributes?.style,
                ...(isDraggable ? getDraggableStyles(child.id) : {}),
            },
        }

        if (!isDraggable) { return baseAttributes }

        return {
            ...baseAttributes,
            ...draggableAttributes,
            onMouseDown: (e: React.MouseEvent<HTMLElement>) => onMouseDown(e as never, child.id),
            componentClassName: draggableAttributes?.dragTitleSelector,
        }
    }

    const renderHeaderCell = (child: HeaderCell) => (
        <TableHeaderCell
            key={child.id}
            id={child.id}
            sortingDirection={child.sortingParam ? sorting[child.sortingParam] : undefined}
            validateFilterField={validateFilterField}
            filterError={filterErrors?.[child.id]}
            elementAttributes={{
                id: child.id,
                ...child.elementAttributes,
                ...(isDraggable ? omit(draggableAttributes, 'style') : {}),
            }}
            dragAttributes={isDraggable ? getMergedAttributes(child) : null}
            rowSpan={rowSpan}
            {...omit(child, ['elementAttributes', 'id'])}
        />
    )

    return children.map(renderHeaderCell)
}
