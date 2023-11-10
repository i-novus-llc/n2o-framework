import React from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import { useTableWidget } from '../../widgets/AdvancedTable'

/**
 * Дропдаун для скрытия/показа колонок в таблице
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {array} columns - кологки(приходит из редакса)
 * @example
 * <ToggleColumn entityKey='TestEntityKey'/>
 */
export const ToggleColumn = (props) => {
    const { columnsState, changeColumnParam } = useTableWidget()
    const { icon, label, entityKey: widgetId, nested = false } = props

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
                                const { conditions, columnId, label, visible } = column

                                const toggleVisibility = () => {
                                    changeColumnParam(widgetId, columnId, 'visible', !visible)
                                }

                                return (
                                    isEmpty(conditions) && (
                                        <DropdownItem
                                            key={columnId}
                                            toggle={false}
                                            onClick={toggleVisibility}
                                        >
                                            <span className="n2o-dropdown-check-container">
                                                {visible && <i className="fa fa-check" aria-hidden="true" />}
                                            </span>
                                            <span>{label || columnId}</span>
                                        </DropdownItem>
                                    )
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
