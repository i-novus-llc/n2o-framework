import { Action } from '../Action'

interface InsertOverlayPayload {
    name: string
    visible: boolean
    mode: 'modal' | 'drawer' | 'dialog'
    addition?: Record<string, unknown>
}

export type InsertOverlay = Action<string, InsertOverlayPayload>
