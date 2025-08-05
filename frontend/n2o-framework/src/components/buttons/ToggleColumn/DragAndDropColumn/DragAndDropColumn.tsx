import React from 'react'
import omit from 'lodash/omit'
import classNames from 'classnames'

import { type HeaderCell, MOVE_MODE } from '../../../../ducks/table/Table'
import { useTableActions } from '../../../Table'
import { DIRECTION, useDragAndDrop } from '../../../widgets/Table/useDragAndDrop'
import { Column } from '../Column'
import { type DragAndDropMenuProps } from '../types'

import { DragHandle } from './DragHandle'

export const DRAG_WRAPPER_CLASS = 'menu-drag-wrapper'

export function DragAndDropColumn({ items, id, onClick, moveMode, ghostContainerId }: DragAndDropMenuProps) {
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
                <Column
                    item={item}
                    onClick={onClick}
                    elementAttributes={{ id: item.id, ...omit(draggableAttributes, 'style') }}
                />
            </section>
        )
    }

    return <>{items.map(renderDropdownItem)}</>
}
