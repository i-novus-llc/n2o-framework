import { RefObject } from 'react'

export enum ProgressBarColors {
    DEFAULT = 'default',
    SUCCESS = 'success',
    INFO = 'info',
    WARNING = 'warning',
    DANGER = 'danger',
}

export interface Props {
    id: string
    color: ProgressBarColors
    model: Record<string, string>
    size: string
    visible: boolean
    forwardedRef: RefObject<HTMLElement>
    tooltipFieldId: string
    animated: boolean
    striped: boolean
}
