import React from 'react'
import PropTypes from 'prop-types'
import UncontrolledButtonDropdown from 'reactstrap/lib/UncontrolledButtonDropdown'
import DropdownToggle from 'reactstrap/lib/DropdownToggle'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import DropdownItem from 'reactstrap/lib/DropdownItem'
import { connect } from 'react-redux'

import { changeSizeWidget, dataRequestWidget } from '../../../ducks/widgets/store'
import { makeModelIdSelector, makeWidgetSizeSelector } from '../../../ducks/widgets/selectors'

/**
 * Дропдаун для выбора размера(size) виджета
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {number} size - текущий размер(приходит из редакса)
 * @example
 * <ChangeSize entityKey='TestEntityKey'/>
 */
class ChangeSize extends React.Component {
    constructor(props) {
        super(props)
        this.sizes = [5, 10, 20, 50]
        this.resize = this.resize.bind(this)
    }

    /**
     * изменение размера
     * @param size
     */
    resize(size) {
        const { dispatch, entityKey, modelId } = this.props

        dispatch(changeSizeWidget(entityKey, size))
        dispatch(dataRequestWidget(entityKey, modelId, { size, page: 1 }))
    }

    /**
     * рендер меню
     * @param sizes
     */
    renderSizeDropdown(sizes) {
        const { size } = this.props

        return sizes.map((s, index) => (
            // eslint-disable-next-line react/no-array-index-key
            <DropdownItem key={index} toggle={false} onClick={() => this.resize(s)}>
                <span className="n2o-dropdown-check-container">
                    {size === s && <i className="fa fa-check" aria-hidden="true" />}
                </span>
                <span>{s}</span>
            </DropdownItem>
        ))
    }

    /**
     * базовый рендер
     * @returns {*}
     */
    render() {
        return (
            <UncontrolledButtonDropdown>
                <DropdownToggle caret>
                    <i className="fa fa-list" />
                </DropdownToggle>
                <DropdownMenu>{this.renderSizeDropdown(this.sizes)}</DropdownMenu>
            </UncontrolledButtonDropdown>
        )
    }
}

ChangeSize.propTypes = {
    size: PropTypes.number,
    entityKey: PropTypes.string,
    modelId: PropTypes.string,
    dispatch: PropTypes.func,
}

const mapStateToProps = (state, props) => ({
    size: makeWidgetSizeSelector(props.entityKey)(state),
    modelId: makeModelIdSelector(props.entityKey)(state),
})

export { ChangeSize }
export default connect(mapStateToProps)(ChangeSize)
