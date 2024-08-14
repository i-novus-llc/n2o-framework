// TODO пока тут оставил, в будущем, наверно, вся meta переедет вместе с типами
import { Meta as N2OMeta } from '../ducks/Action'
import { LinkTarget } from '../constants/linkTarget'

export enum MetaType {
    Refresh = 'refresh',
    Redirect = 'redirect',
    Alert = 'alert',
    Clear = 'clear',
    UserDialog = 'userDialog',
    ValidationMessages = 'messages',
}

export interface Meta {
    [MetaType.Refresh]?: RefreshMeta
    [MetaType.Redirect]?: RedirectMeta
    [MetaType.Alert]?: AlertMeta
    [MetaType.Clear]?: unknown
    [MetaType.UserDialog]?: UserDialogMeta
    [MetaType.ValidationMessages]?: ValidationMessagesMeta
}

export interface ActionMeta extends N2OMeta {
    fail?: Meta
    success?: Meta
}

export interface RefreshMeta {
    datasources: string[]
}

export interface RedirectMeta {
    path: string
    target: LinkTarget
    // TODO сюда тоже закинуть тип IMapping
    pathMapping?: Record<string, unknown>
    queryMapping?: Record<string, unknown>
}

export interface AlertMeta {
    alert: {
        messages: unknown
        stacked: unknown
    }
}

export interface UserDialogMeta {
    // TODO тут тип пропсов PageDialog должен быть
    dialog: {
        title: string
        description: string
        toolbar: unknown
        [key: string]: unknown
    }
}

export interface ValidationFieldMessage {
    id: string | number
    field: string
    placement: string
    severity: string
    closeButton: boolean
    text: string
    timeout?: number
}

export interface ValidationMessagesMeta {
    form: string
    fields: Record<string, ValidationFieldMessage>
}
