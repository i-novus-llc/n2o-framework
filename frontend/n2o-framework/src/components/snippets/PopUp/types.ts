import { ReactNode, RefObject } from 'react'
import type { TooltipFloatingPlacement } from '@i-novus/n2o-components/lib/display/Tooltip/useTooltipFloating'

export interface PopUpProps {
    open: boolean
    className?: string
    triggerClassName?: string
    children?: ReactNode
    placement?: TooltipFloatingPlacement
    triggerRef?: RefObject<HTMLDivElement>
    onTriggerClick(): void
}

export interface PopUpChildProps {
    children: ReactNode
}
