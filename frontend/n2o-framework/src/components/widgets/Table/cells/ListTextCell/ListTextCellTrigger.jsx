import React from 'react'
import PropTypes from 'prop-types'
import onClickOutsideHOC from 'react-onclickoutside'

import { triggerClassName } from './utils'

function Component({ label, labelDashed, forwardedRef }) {
    return <span ref={forwardedRef} className={triggerClassName(labelDashed)}>{label}</span>
}

const Extended = onClickOutsideHOC(Component)

export function ListTextCellTrigger({ label, labelDashed, forwardedRef, tooltipClose }) {
    return (
        <Extended
            label={label}
            labelDashed={labelDashed}
            forwardedRef={forwardedRef}
            handleClickOutside={tooltipClose}
        />
    )
}

ListTextCellTrigger.propTypes = {
    label: PropTypes.string,
    forwardedRef: PropTypes.func,
    labelDashed: PropTypes.bool,
}
