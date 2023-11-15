import React, { useEffect } from 'react'
import PropTypes from 'prop-types'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import { useTableWidget } from '../../widgets/AdvancedTable'

/**
 * Дропдаун для скрытия/показа колонок в таблице
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {array} columns - кологки(приходит из редакса)
 * @reactProps {string} defaultColumns - изначально видимые колонки (visibleState) (прим. 'id1,id2'), остальные скрываются
 * @example
 * <ToggleColumn entityKey='TestEntityKey'/>
 */

export const ToggleColumn = (props) => {
    const { icon, label, entityKey: widgetId, defaultColumns = '', nested = false } = props
    const { columnsState, changeColumnParam } = useTableWidget({ defaultColumns, widgetId })

    useEffect(() => {
        if (defaultColumns) {
            const defaultIds = defaultColumns.split(',')

            for (const column of columnsState) {
                const { columnId } = column
                const visible = defaultIds.includes(columnId)

                changeColumnParam(widgetId, columnId, 'visibleState', visible)
            }
        }
    }, [columnsState.length])

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
                                const { columnId, label, visible, visibleState } = column

                                if (!visible) {
                                    return null
                                }

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
                                        <span>{label || columnId}</span>
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

ToggleColumn.propTypes = {
    icon: PropTypes.string,
}

ToggleColumn.displayName = 'ToggleColumn'

export default ToggleColumn
