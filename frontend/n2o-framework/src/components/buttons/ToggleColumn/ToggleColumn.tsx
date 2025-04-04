import React, { useEffect } from 'react'
import { useStore } from 'react-redux'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'
import classNames from 'classnames'

import { useTableWidget } from '../../widgets/AdvancedTable'
import { getTableParam } from '../../../ducks/table/selectors'
import { VISIBLE_STATE, IS_DEFAULT_COLUMNS } from '../../../ducks/table/constants'
import { useReduxButton } from '../useReduxButton'

import { type Props, type TableContextProps } from './types'

/**
 * Дропдаун для скрытия/показа колонок в таблице
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {array} columns - кологки(приходит из редакса)
 * @reactProps {string} defaultColumns - изначально видимые колонки (visibleState) (прим. 'id1,id2'), остальные скрываются
 * @example
 * <ToggleColumn entityKey='TestEntityKey'/>
 */

function getLabel(label: string, icon: string, columnId: string) {
    if (!label && !icon) { return columnId }
    if (!label) { return null }

    return label
}

export const ToggleColumn = (props: Props) => {
    const { icon, label, entityKey: widgetId, defaultColumns = '', nested = false } = props
    const { columnsState, changeColumnParam, switchTableParam } = useTableWidget() as TableContextProps

    const { getState } = useStore()

    useEffect(() => {
        if (!defaultColumns || !columnsState.length) { return }

        const state = getState()
        const isDefaultColumns = getTableParam(widgetId, IS_DEFAULT_COLUMNS)(state)

        if (isDefaultColumns) { return }

        const defaultIds = defaultColumns.replace(/\s/g, '').split(',')

        for (const column of columnsState) {
            const { columnId } = column
            const visible = defaultIds.includes(columnId)

            changeColumnParam(widgetId, columnId, VISIBLE_STATE, visible)
        }

        switchTableParam(widgetId, IS_DEFAULT_COLUMNS)
    }, [columnsState.length])

    useReduxButton(props)

    return (
        <UncontrolledButtonDropdown direction={nested ? 'right' : 'down'}>
            <div>
                <div className="n2o-dropdown visible">
                    <div>
                        <DropdownToggle caret>
                            {icon && <i className={icon} />}
                            {label}
                        </DropdownToggle>

                        <DropdownMenu>
                            {columnsState.map((column) => {
                                const { columnId, label, icon, visible, visibleState } = column

                                if (!visible) { return null }

                                const currentLabel = getLabel(label, icon, columnId)

                                const toggleVisibility = () => {
                                    /* visibleState - локальная видимость колонки, переключаемая в ToggleColumn
                                       visible - влияет на общую видимость колонки и соответствующей кнопки переключения в ToggleColumn */
                                    changeColumnParam(widgetId, columnId, 'visibleState', !visibleState)
                                }

                                return (
                                    <DropdownItem
                                        key={columnId}
                                        toggle={false}
                                        onClick={toggleVisibility}
                                    >
                                        <span className="n2o-dropdown-check-container">
                                            {visibleState && <i className="fa fa-check" aria-hidden="true" />}
                                        </span>
                                        {icon && <i className={classNames(icon, { 'mr-1': currentLabel })} />}
                                        {currentLabel && <span>{currentLabel}</span>}
                                    </DropdownItem>
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
