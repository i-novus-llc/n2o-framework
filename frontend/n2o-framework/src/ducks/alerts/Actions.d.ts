import { Action } from '../Action'

import { KeyType } from './constants'
import { IAlert } from './Alerts'

type AlertPayload = {
    key: KeyType
    alert: IAlert
    alerts?: IAlert[]
}

type AlertsPayload = {
    key: KeyType
    alert?: IAlert
    alerts: IAlert[]
}

type RemovePayload = {
    key: KeyType
    id: string
}

type RemoveAllPayload = {
    key: KeyType
}

type StopRemovingPayload = {
    key: KeyType
    id: string
}

export type Add = Action<string, AlertPayload>
export type AddMulti = Action<string, AlertsPayload>
export type Remove = Action<string, RemovePayload>
export type RemoveAll = Action<string, RemoveAllPayload>
export type StopRemoving = Action<string, StopRemovingPayload>
