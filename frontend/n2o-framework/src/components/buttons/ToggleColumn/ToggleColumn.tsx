import React from 'react'
import { useStore } from 'react-redux'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu } from 'reactstrap'
import classNames from 'classnames'
import get from 'lodash/get'

import { useTableWidget } from '../../widgets/AdvancedTable'
import { makeTableByIdSelector } from '../../../ducks/table/selectors'
import { VISIBLE_STATE } from '../../../ducks/table/constants'
import { useReduxButton } from '../useReduxButton'
import { type HeaderCell } from '../../../ducks/table/Table'
import { TableActionsProvider } from '../../Table/provider/TableActions'

import { type ToggleColumnProps } from './types'
import { DRAG_WRAPPER_CLASS, DragAndDropColumn } from './DragAndDropColumn/DragAndDropColumn'
import { Column } from './Column'
import { DragHandle } from './DragAndDropColumn/DragHandle'
import { MultiColumn } from './MultiColumn/MultiColumn'

export const ToggleColumn = (props: ToggleColumnProps) => {
    const { icon, label, entityKey: widgetId, nested = false } = props

    const { getState } = useStore()

    const { header } = makeTableByIdSelector(widgetId)(getState())
    const cells = get(header, 'cells', [])
    const hasMoveMode = cells?.some(cell => cell.moveMode)

    useReduxButton(props)
    const { changeColumnParam, actionListener } = useTableWidget()

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
            <div className="n2o-dropdown n2o-toggle-column visible">
                <div>
                    <DropdownToggle caret>
                        {icon && <i className={icon} />}
                        {label}
                    </DropdownToggle>

                    <DropdownMenu id={menuId} className={classNames('n2o-toggle-column__menu', { moveMode: hasMoveMode })}>
                        {cells.map((cell) => {
                            const { columnId, visible, moveMode, children } = cell

                            if (!visible) { return null }

                            if (moveMode && children) {
                                return (
                                    <TableActionsProvider actionListener={actionListener}>
                                        <DragAndDropColumn
                                            key={columnId}
                                            id={columnId}
                                            items={children}
                                            onClick={handleColumnClick}
                                            moveMode={moveMode}
                                            ghostContainerId={menuId}
                                        />
                                    </TableActionsProvider>
                                )
                            }

                            if (children) {
                                const { label, enabled } = cell

                                return (
                                    <section className={DRAG_WRAPPER_CLASS}>
                                        {hasMoveMode && <DragHandle empty />}
                                        <MultiColumn
                                            items={children}
                                            onClick={handleColumnClick}
                                            label={label}
                                            enabled={enabled}
                                        />
                                    </section>
                                )
                            }

                            return (
                                <section className={DRAG_WRAPPER_CLASS}>
                                    {hasMoveMode && <DragHandle empty />}
                                    <Column key={columnId} item={cell} onClick={handleColumnClick} />
                                </section>
                            )
                        })}
                    </DropdownMenu>
                </div>
            </div>
        </UncontrolledButtonDropdown>
    )
}

ToggleColumn.displayName = 'ToggleColumn'

export default ToggleColumn
