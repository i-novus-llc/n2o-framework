import { RefObject } from 'react'

export enum CELL_TYPE {
    ICON = 'icon',
    TEXT = 'text',
}

type LinkCellModel = Record<string, unknown>

export interface Props {
    widgetId: string
    className?: string
    fieldKey: string
    id: string
    icon: string
    type: CELL_TYPE
    url: string
    disabled: boolean
    callAction(model: LinkCellModel): void
    action: string
    resolveWidget(model: LinkCellModel): void
    model: LinkCellModel
    forwardedRef: RefObject<HTMLElement>
    tooltipFieldId: string
}
