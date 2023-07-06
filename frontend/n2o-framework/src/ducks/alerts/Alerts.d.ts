import { KeyType, SeverityType } from './constants'

export interface IAlert {
    id: string
    title?: string
    severity: SeverityType
    text?: string
    timeout?: number
    closeButton: boolean
    placement: KeyType
    stopped?: boolean
    modelLink?: string
}

export interface IConfig {
    timeout: {
        error: number
        info: number
        success: number
        warning: number
    }
}

export type State = Partial<Record<KeyType, IAlert[]>>
