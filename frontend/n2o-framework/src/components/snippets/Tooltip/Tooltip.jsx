import React from 'react'
import PropTypes from 'prop-types'
import TooltipTrigger from 'react-popper-tooltip'

import 'react-popper-tooltip/dist/styles.css'
import { RenderTooltipTrigger, RenderTooltipBody } from './utils'

export function Tooltip({ hint, label, labelDashed, placement, trigger, theme }) {
    // trigger для появления tooltip, отображает label
    // eslint-disable-next-line react/prop-types
    const Trigger = ({ getTriggerProps, triggerRef }) => (
        <RenderTooltipTrigger
            getTriggerProps={getTriggerProps}
            triggerRef={triggerRef}
            label={label}
            labelDashed={labelDashed}
            hint={hint}
        />
    )

    // hint отображает лист
    // eslint-disable-next-line react/prop-types
    const TooltipBody = ({ getTooltipProps, getArrowProps, tooltipRef, arrowRef }) => (
        <RenderTooltipBody
            getTooltipProps={getTooltipProps}
            getArrowProps={getArrowProps}
            tooltipRef={tooltipRef}
            arrowRef={arrowRef}
            hint={hint}
            placement={placement}
            theme={theme}
        />
    )

    const RenderTooltip = () => (
        <TooltipTrigger
            label={label}
            labelDashed={labelDashed}
            placement={placement}
            trigger={trigger}
            tooltip={TooltipBody}
            delayShow={200}
        >
            {Trigger}
        </TooltipTrigger>
    )

    return <RenderTooltip />
}

Tooltip.propTypes = {
    label: PropTypes.any,
    placement: PropTypes.string,
    labelDashed: PropTypes.bool,
    trigger: PropTypes.string,
    theme: PropTypes.string,
    hint: PropTypes.any,
}

Tooltip.defaultProps = {
    labelDashed: false,
    placement: 'bottom',
    trigger: 'hover',
    theme: 'dark',
}

export default Tooltip
