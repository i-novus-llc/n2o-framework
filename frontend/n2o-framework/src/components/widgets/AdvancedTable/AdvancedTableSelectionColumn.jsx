import React from 'react'
import PropTypes from 'prop-types'
import { compose, withState, withHandlers } from 'recompose'

import Checkbox from '../../controls/Checkbox/CheckboxN2O'

import { NATIVE_CHECKED_PARAM } from './const'

function AdvancedTableSelectionColumn({ onChange, checked, setRef }) {
    return (
        <div className="n2o-advanced-table-selection-item">
            <Checkbox
                inline
                checked={checked}
                onChange={onChange}
                ref={setRef}
            />
        </div>
    )
}

AdvancedTableSelectionColumn.propTypes = {
    onChange: PropTypes.func,
    setRef: PropTypes.func,
    checked: PropTypes.bool,
}

const enhance = compose(
    withState('checked', 'setChecked', false),
    withHandlers({
        onChange: ({ onChange, setChecked }) => (event) => {
            const { target } = event.nativeEvent
            const checked = target.getAttribute(NATIVE_CHECKED_PARAM) === 'false'

            setChecked(checked)
            onChange(checked)

            target.setAttribute(NATIVE_CHECKED_PARAM, !checked)
        },
    }),
)

export { AdvancedTableSelectionColumn }
export default enhance(AdvancedTableSelectionColumn)
