import React, { ReactNode } from 'react'

import { TooltipTheme, TooltipComponent } from './TooltipComponent'
import { useTooltipFloating, Container, type TooltipFloating } from './useTooltipFloating'
import { useTooltipTrigger, Trigger, type TooltipTrigger } from './useTooltipTrigger'

export interface Props extends TooltipFloating {
    hint: ReactNode | string | number
    className?: string
    trigger?: Trigger
    children: TooltipTrigger['children']
    closeOnClickOutside: boolean
    theme?: TooltipTheme,
}

export function Tooltip({
    hint,
    className,
    children,
    theme,
    closeOnClickOutside = true,
    placement = 'top',
    delay = 0,
    trigger = Trigger.HOVER,
    container = Container.TARGET,
}: Props) {
    const {
        open,
        handleOpen,
        handleClose,
        toggle,
        refs,
        floatingRef,
        floatingStyles,
        portal,
    } = useTooltipFloating({
        delay,
        placement,
        container,
        closeOnClickOutside: closeOnClickOutside && trigger === Trigger.CLICK,
    })

    const triggerElement = useTooltipTrigger({
        children,
        refs,
        trigger,
        handleOpen,
        handleClose,
        toggle,
    })

    return (
        <>
            {triggerElement}
            <TooltipComponent
                floatingRef={floatingRef}
                style={floatingStyles}
                className={className}
                visible={open}
                portal={portal}
                theme={theme}
            >
                {hint}
            </TooltipComponent>
        </>
    )
}
