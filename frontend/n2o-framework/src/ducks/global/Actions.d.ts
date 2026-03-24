import { Action } from '../Action'
import { EventType } from '../watchEvents/watchEvents'

interface Layout {
    fullSizeHeader: boolean
    fixed: boolean
}

interface Menu {
    layout: Layout
    events?: EventType[]
    datasources: Record<string, never>
    wsPrefix?: string
}

export interface RequestConfigPayload {
    user: Record<string, never>
    locale: string
    menu: Menu
}

export type RequestConfigAction = Action<string, RequestConfigPayload>
