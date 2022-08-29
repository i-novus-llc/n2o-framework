import React from 'react'
import PropTypes from 'prop-types'

import { TooltipHOC } from '../../../../snippets/Tooltip/TooltipHOC'
import { triggerClassName } from '../../../../snippets/Tooltip/utils'

function ListTextCellTriggerBody({ label, labelDashed, tooltipTriggerRef }) {
    return (
        <span
            ref={tooltipTriggerRef}
            className={triggerClassName(labelDashed)}
        >
            {label}
        </span>
    )
}

export const ListTextCellTrigger = TooltipHOC(ListTextCellTriggerBody)

ListTextCellTriggerBody.propTypes = {
    label: PropTypes.string,
    tooltipTriggerRef: PropTypes.func,
    labelDashed: PropTypes.bool,
}
