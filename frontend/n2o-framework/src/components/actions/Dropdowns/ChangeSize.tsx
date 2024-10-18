import React, { useContext } from 'react'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'
import { connect } from 'react-redux'

import { makeWidgetSizeSelector } from '../../../ducks/widgets/selectors'
import { DataSourceContext } from '../../../core/widget/context'
import { State } from '../../../ducks/State'

const SIZES = [5, 10, 20, 50]

/**
 * Дропдаун для выбора размера(size) виджета
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {number} size - текущий размер(приходит из редакса)
 * @example
 * <ChangeSize entityKey='TestEntityKey'/>
 */

function ChangeSize({ size: currentSize }: { size: number }) {
    const { setSize } = useContext(DataSourceContext)

    const items = SIZES.map((size, i) => (
        <DropdownItem toggle={false} onClick={() => setSize(size)}>
            <span className="n2o-dropdown-check-container">
                {currentSize === size && <i className="fa fa-check" aria-hidden="true" />}
            </span>
            <span>{size}</span>
        </DropdownItem>
    ))

    return (
        <UncontrolledButtonDropdown>
            <DropdownToggle caret>
                <i className="fa fa-list" />
            </DropdownToggle>
            <DropdownMenu>{items}</DropdownMenu>
        </UncontrolledButtonDropdown>
    )
}

const mapStateToProps = (state: State, { entityKey: widgetId }: { entityKey: string }) => ({
    size: makeWidgetSizeSelector(widgetId)(state),
})

export { ChangeSize }
export default connect(mapStateToProps)(ChangeSize)
