import { Key, Severity, PLACEMENT } from './constants'

export interface Alert {
    id: string
    title?: string
    severity: Severity
    text?: string
    timeout?: number
    closeButton: boolean
    placement: PLACEMENT
    stopped?: boolean
    modelLink?: string
    time?: string
}

export interface Config {
    timeout: {
        error: number
        info: number
        success: number
        warning: number
    }
}

export type State = Partial<Record<Key, Alert[]>>
