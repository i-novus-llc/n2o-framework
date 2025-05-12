import React, { useEffect, useMemo } from 'react'
import { useStore } from 'react-redux'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu } from 'reactstrap'
import classNames from 'classnames'
import get from 'lodash/get'

import { useTableWidget } from '../../widgets/AdvancedTable'
import { getTableParam, makeTableByIdSelector } from '../../../ducks/table/selectors'
import { VISIBLE_STATE, IS_DEFAULT_COLUMNS } from '../../../ducks/table/constants'
import { useReduxButton } from '../useReduxButton'
import { getAllValuesByKey } from '../../Table/utils'
import { type HeaderCell } from '../../../ducks/table/Table'
import { TableActionsProvider } from '../../Table/provider/TableActions'

import { type Props } from './types'
import { DRAG_WRAPPER_CLASS, DragAndDropMenu } from './DragAndDropMenu'
import { parseIds } from './helpers'
import { DropdownItem } from './DropdownItem'
import { DragHandle } from './DragHandle'

export const ToggleColumn = (props: Props) => {
    const { icon, label, entityKey: widgetId, defaultColumns = '', locked = '', nested = false } = props
    const { changeColumnParam, switchTableParam, actionListener } = useTableWidget()

    const { getState } = useStore()
    const { header } = makeTableByIdSelector(widgetId)(getState())
    const cells = get(header, 'cells', [])
    const hasMoveMode = cells?.some(cell => cell.moveMode)

    useEffect(() => {
        if (!defaultColumns || !cells.length) { return }
        const state = getState()
        const isDefaultColumns = getTableParam(widgetId, IS_DEFAULT_COLUMNS)(state)

        if (isDefaultColumns) { return }

        const defaultIds = parseIds(defaultColumns)
        const columnsState = getAllValuesByKey(cells, { keyToIterate: 'children' })

        for (const column of columnsState) {
            const { columnId, parentId } = column
            const visible = defaultIds.includes(columnId)

            changeColumnParam(widgetId, columnId, VISIBLE_STATE, visible, parentId)
        }

        switchTableParam(widgetId, IS_DEFAULT_COLUMNS)
    }, [cells.length])

    useReduxButton(props)

    const lockedColumns = useMemo(() => parseIds(locked), [locked])

    const handleColumnClick = (
        columnId: HeaderCell['columnId'],
        visibleState: HeaderCell['visibleState'],
        parentId?: HeaderCell['parentId'],
    ) => {
        changeColumnParam(widgetId, columnId, VISIBLE_STATE, !visibleState, parentId)
    }

    const menuId = `${widgetId}-toggle-menu`

    return (
        <UncontrolledButtonDropdown direction={nested ? 'right' : 'down'}>
            <div>
                <div className="n2o-dropdown visible">
                    <div>
                        <DropdownToggle caret>
                            {icon && <i className={icon} />}
                            {label}
                        </DropdownToggle>

                        <DropdownMenu id={menuId} className={classNames({ moveMode: hasMoveMode })}>
                            {cells.map((cell) => {
                                const { columnId, visible, visibleState, moveMode, children } = cell

                                if (!visible) { return null }

                                if (moveMode && children) {
                                    return (
                                        <TableActionsProvider actionListener={actionListener}>
                                            <DragAndDropMenu
                                                key={columnId}
                                                id={columnId}
                                                items={children}
                                                onClick={handleColumnClick}
                                                lockedColumns={lockedColumns}
                                                moveMode={moveMode}
                                                ghostContainerId={menuId}
                                            />
                                        </TableActionsProvider>
                                    )
                                }

                                return (
                                    <section className={DRAG_WRAPPER_CLASS}>
                                        {hasMoveMode && <DragHandle empty />}
                                        <DropdownItem
                                            key={columnId}
                                            item={cell}
                                            onClick={handleColumnClick}
                                            lockedColumns={lockedColumns}
                                        />
                                    </section>
                                )
                            })}
                        </DropdownMenu>
                    </div>
                </div>
            </div>
        </UncontrolledButtonDropdown>
    )
}

ToggleColumn.displayName = 'ToggleColumn'

export default ToggleColumn
