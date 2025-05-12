import React from 'react'
import omit from 'lodash/omit'
import classNames from 'classnames'

import { type HeaderCell, MOVE_MODE } from '../../../ducks/table/Table'
import { useTableActions } from '../../Table'
import { DIRECTION, useDragAndDrop } from '../../widgets/Table/useDragAndDrop'

import { DropdownItem } from './DropdownItem'
import { DragHandle } from './DragHandle'

export const DRAG_WRAPPER_CLASS = 'menu-drag-wrapper'

export interface DragAndDropMenuProps {
    items: HeaderCell[]
    onClick(columnId: HeaderCell['columnId'], visibleState: HeaderCell['visibleState'], parentId: HeaderCell['parentId']): void
    lockedColumns: string[]
    id: string
    moveMode?: MOVE_MODE
    ghostContainerId: string
}

export function DragAndDropMenu({ items, id, onClick, lockedColumns, moveMode, ghostContainerId }: DragAndDropMenuProps) {
    const { onHeaderDrop } = useTableActions()

    const { onMouseDown, draggingId, draggableAttributes } = useDragAndDrop({
        onDrop: (sourceId, targetId) => onHeaderDrop(id, sourceId, targetId),
        direction: DIRECTION.VERTICAL,
        targetSelector: 'button',
        ghostContainerId,
    })

    const draggable = moveMode === MOVE_MODE.ALL || moveMode === MOVE_MODE.SETTINGS

    const renderDropdownItem = (item: HeaderCell) => {
        const elementAttributes = {
            id: item.columnId,
            ...(draggable ? {
                ...draggableAttributes,
                onMouseDown: (e: React.MouseEvent) => {
                    e.stopPropagation()
                    onMouseDown(e as never, item.columnId)
                },
                style: {
                    ...draggableAttributes.style,
                    opacity: draggingId === item.columnId ? 0.5 : 1,
                },
            }
                : {}),
        }

        return (
            <section className={classNames(DRAG_WRAPPER_CLASS)} key={item.id}>
                <DragHandle label={item.label} {...elementAttributes} empty={!draggable} />
                <DropdownItem
                    item={item}
                    onClick={onClick}
                    lockedColumns={lockedColumns}
                    elementAttributes={{ id: item.id, ...omit(draggableAttributes, 'style') }}
                />
            </section>
        )
    }

    return <>{items.map(renderDropdownItem)}</>
}
