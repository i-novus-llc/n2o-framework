import { ComponentType, LegacyRef, ReactNode } from 'react'

interface ComponentProps {
    children: ReactNode
    model: Record<string, unknown>
}

export interface Props {
    model: Record<string, Array<Record<string, unknown>>>;
    fieldKey: string
    labelFieldId: string
    content?: { component: ComponentType<ComponentProps> }
    forwardedRef: LegacyRef<HTMLDivElement>
    color: string
    amountToGroup: number
    visible: boolean
    inline: boolean
    separator: string | null
    tooltipFieldId: string
    placement: string
}
