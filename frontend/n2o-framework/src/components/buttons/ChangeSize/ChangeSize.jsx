import React, { useContext } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { UncontrolledButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap'

import { makeWidgetSizeSelector } from '../../../ducks/widgets/selectors'
import { DataSourceContext } from '../../../core/widget/context'
import { SIZES } from '../constants'

/**
 * Дропдаун для выбора размера(size) виджета
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {number} size - текущий размер(приходит из редакса)
 * @example
 * <ChangeSize entityKey='TestEntityKey'/>
 */
function ChangeSizeComponent(props) {
    const { size: currentSize, icon, label, nested = false } = props

    const { setSize } = useContext(DataSourceContext)

    const items = SIZES.map((size, i) => {
        const onClick = () => setSize(size)

        return (
            <DropdownItem toggle={false} onClick={onClick}>
                <span className="n2o-dropdown-check-container">
                    {currentSize === size && <i className="fa fa-check" aria-hidden="true" />}
                </span>
                <span>{size}</span>
            </DropdownItem>
        )
    })

    return (
        <UncontrolledButtonDropdown direction={nested ? 'left' : 'down'}>
            <div>
                <div className="n2o-dropdown visible">
                    <div>
                        <DropdownToggle caret>
                            {icon && <i className={icon} />}
                            {label}
                        </DropdownToggle>
                        <DropdownMenu>{items}</DropdownMenu>
                    </div>
                </div>
            </div>
        </UncontrolledButtonDropdown>
    )
}
ChangeSizeComponent.propTypes = {
    size: PropTypes.number,
    icon: PropTypes.string,
}

const mapStateToProps = (state, { entityKey: widgetId }) => ({
    size: makeWidgetSizeSelector(widgetId)(state),
})

const ChangeSize = connect(mapStateToProps)(ChangeSizeComponent)

export { ChangeSize }
export { ChangeSizeComponent }
