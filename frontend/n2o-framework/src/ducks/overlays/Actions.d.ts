import { Action } from '../Action'

interface InsertOverlayPayload {
    mode: 'modal' | 'drawer' | 'dialog'
    name: string
    visible: boolean
}

interface InsertPayload {
    name: string
    visible: boolean
    mode: 'modal' | 'popover'
    type: 'page' | 'confirm'
    props?: { target: string }
}

type RemovePayload = {
    id: string
}

export type InsertOverlay = Action<string, InsertOverlayPayload>
export type Insert = Action<string, InsertPayload>
export type Remove = Action<string, RemovePayload>
