import React from 'react'
import { compose, withState, withHandlers } from 'recompose'
import get from 'lodash/get'

import Checkbox from '../../controls/Checkbox/CheckboxN2O'

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

const enhance = compose(
    withState('checked', 'setChecked', false),
    withHandlers({
        onChange: ({ onChange, setChecked }) => (event) => {
            const checked = !get(event, 'target.checked', false)

            setChecked(checked)
            onChange(checked)
        },
    }),
)

export { AdvancedTableSelectionColumn }
export default enhance(AdvancedTableSelectionColumn)
