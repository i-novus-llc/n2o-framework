import React, { useContext } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import { makeWidgetSizeSelector } from '../../../ducks/widgets/selectors'
import { WidgetContext } from '../../../core/widget/context'

const SIZES = [5, 10, 20, 50]

/**
 * Дропдаун для выбора размера(size) виджета
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {number} size - текущий размер(приходит из редакса)
 * @example
 * <ChangeSize entityKey='TestEntityKey'/>
 */
function ChangeSizeComponent({ size: currentSize }) {
    const { setSize } = useContext(WidgetContext)

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
ChangeSizeComponent.propTypes = {
    size: PropTypes.number,
}

const mapStateToProps = (state, props) => ({
    size: makeWidgetSizeSelector(props.entityKey)(state),
})

const ChangeSize = connect(mapStateToProps)(ChangeSizeComponent)

export { ChangeSize }
export { ChangeSizeComponent }
