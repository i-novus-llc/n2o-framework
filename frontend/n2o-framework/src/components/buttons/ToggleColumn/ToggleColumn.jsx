import React from 'react'
import PropTypes from 'prop-types'
import { compose, withHandlers } from 'recompose'
import { connect } from 'react-redux'
import map from 'lodash/map'
import isEmpty from 'lodash/isEmpty'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import { getContainerColumns } from '../../../ducks/columns/selectors'
import { toggleColumnVisibility } from '../../../ducks/columns/store'
import { TABLE_ICON } from '../constants'

/**
 * Дропдаун для скрытия/показа колонок в таблице
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {array} columns - кологки(приходит из редакса)
 * @example
 * <ToggleColumn entityKey='TestEntityKey'/>
 */
function ToggleColumnComponent(props) {
    const { columns, renderColumnDropdown, icon } = props
    const arrayOfColumns = map(columns, (value, key) => ({ key, value }))
    const filteredColumns = arrayOfColumns.filter(
        ({ key, value = {} }) => key && value.label && value.frozen !== true,
    )

    return (
        <UncontrolledButtonDropdown>
            <DropdownToggle caret>
                <i className={icon || TABLE_ICON} />
            </DropdownToggle>
            <DropdownMenu>{renderColumnDropdown(filteredColumns)}</DropdownMenu>
        </UncontrolledButtonDropdown>
    )
}

ToggleColumnComponent.propTypes = {
    columns: PropTypes.object,
    renderColumnDropdown: PropTypes.func,
    icon: PropTypes.string,
}

const mapStateToProps = (state, props) => ({
    columns: getContainerColumns(props.entityKey)(state),
})

const enhance = compose(
    connect(mapStateToProps),
    withHandlers({
        toggleVisibility: ({ dispatch, entityKey }) => id => dispatch(toggleColumnVisibility(entityKey, id)),
    }),
    withHandlers({
        renderColumnDropdown: ({ toggleVisibility }) => (columns) => {
            const notSelected = columns.filter(col => !col.value.visible).map(col => col.key)

            return map(columns, ({ key, value }, i) => {
                const selected = !notSelected.includes(key)
                const { conditions } = value

                return (
                    isEmpty(conditions) && (
                        <DropdownItem
                            key={i}
                            toggle={false}
                            onClick={() => toggleVisibility(key)}
                        >
                            <span className="n2o-dropdown-check-container">
                                {selected && <i className="fa fa-check" aria-hidden="true" />}
                            </span>
                            <span>{value.label || key}</span>
                        </DropdownItem>
                    )
                )
            })
        },
    }),
)

const ToggleColumn = enhance(ToggleColumnComponent)

export { ToggleColumn }

export default ToggleColumnComponent
