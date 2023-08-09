import { Action } from '../Action'

import { Key } from './constants'
import { Alert } from './Alerts'

type AlertPayload = {
    key: Key
    alert: Alert
    alerts?: Alert[]
}

type AlertsPayload = {
    key: Key
    alert?: Alert
    alerts: Alert[]
}

type RemovePayload = {
    key: Key
    id: string
}

type RemoveAllPayload = {
    key: Key
}

type StopRemovingPayload = {
    key: Key
    id: string
}

export type Add = Action<string, AlertPayload>
export type AddMulti = Action<string, AlertsPayload>
export type Remove = Action<string, RemovePayload>
export type RemoveAll = Action<string, RemoveAllPayload>
export type StopRemoving = Action<string, StopRemovingPayload>
