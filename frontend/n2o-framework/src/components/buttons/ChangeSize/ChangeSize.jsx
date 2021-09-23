import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { compose, withHandlers } from 'recompose'
import { batchActions } from 'redux-batched-actions'
import map from 'lodash/map'
import UncontrolledButtonDropdown from 'reactstrap/lib/UncontrolledButtonDropdown'
import DropdownToggle from 'reactstrap/lib/DropdownToggle'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import DropdownItem from 'reactstrap/lib/DropdownItem'

import { makeModelIdSelector, makeWidgetSizeSelector } from '../../../ducks/widgets/selectors'
import { changeSizeWidget, dataRequestWidget } from '../../../ducks/widgets/store'

const SIZES = [5, 10, 20, 50]

/**
 * Дропдаун для выбора размера(size) виджета
 * @reactProps {string} entityKey - id виджета, размер которого меняется
 * @reactProps {number} size - текущий размер(приходит из редакса)
 * @example
 * <ChangeSize entityKey='TestEntityKey'/>
 */
function ChangeSizeComponent({ renderSizeDropdown }) {
    return (
        <UncontrolledButtonDropdown>
            <DropdownToggle caret>
                <i className="fa fa-list" />
            </DropdownToggle>
            <DropdownMenu>{renderSizeDropdown(SIZES)}</DropdownMenu>
        </UncontrolledButtonDropdown>
    )
}
ChangeSizeComponent.propTypes = {
    renderSizeDropdown: PropTypes.func,
}

const mapStateToProps = (state, props) => ({
    size: makeWidgetSizeSelector(props.entityKey)(state),
    modelId: makeModelIdSelector(props.entityKey)(state),
})

const enhance = compose(
    connect(mapStateToProps),
    withHandlers({
        resize: ({ dispatch, entityKey, modelId }) => size => batchActions([
            dispatch(changeSizeWidget(entityKey, size)),
            dispatch(dataRequestWidget(entityKey, modelId, { size, page: 1 })),
        ]),
    }),
    withHandlers({
        renderSizeDropdown: ({ size, resize }) => sizes => map(sizes, (s, i) => (
            <DropdownItem key={i} toggle={false} onClick={() => resize(s)}>
                <span className="n2o-dropdown-check-container">
                    {size === s && <i className="fa fa-check" aria-hidden="true" />}
                </span>
                <span>{s}</span>
            </DropdownItem>
        )),
    }),
)
const ChangeSize = enhance(ChangeSizeComponent)

export { ChangeSize }
export { ChangeSizeComponent }
