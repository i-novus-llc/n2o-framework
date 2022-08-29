import React from 'react'
import { usePopperTooltip, Config } from 'react-popper-tooltip'

import { Tooltip as TooltipSnippet } from './Tooltip2'

interface IUseTooltip {
    Tooltip: React.ReactNode | null,
    setTriggerRef: React.Dispatch<React.SetStateAction<HTMLElement | null>>,
    visible: boolean,
}

export function useTooltip(config: Config): IUseTooltip {
    const {
        getArrowProps,
        getTooltipProps,
        setTooltipRef,
        setTriggerRef,
        visible,
    } = usePopperTooltip(config)

    const Tooltip = (
        <TooltipSnippet
            className="n2o-tooltip"
            setTooltipRef={setTooltipRef}
            getTooltipProps={getTooltipProps}
            getArrowProps={getArrowProps}
        />
    )

    return {
        Tooltip: visible ? Tooltip : null,
        setTriggerRef,
        visible,
    }
}
