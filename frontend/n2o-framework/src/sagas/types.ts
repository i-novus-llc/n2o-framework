// TODO пока тут оставил, в будущем, наверно, вся meta переедет вместе с типами
import { LinkTarget } from '../constants/linkTarget'

export enum MetaType {
    Refresh = 'refresh',
    Redirect = 'redirect',
    Alert = 'alert',
    Clear = 'clear',
    ClearForm = 'clearForm',
    UserDialog = 'userDialog',
    ValidationMessages = 'messages'
}

export interface IMeta {
    [MetaType.Refresh]?: IRefreshMeta
    [MetaType.Redirect]?: IRedirectMeta
    [MetaType.Alert]?: IAlertMeta
    [MetaType.Clear]?: unknown
    [MetaType.ClearForm]?: IClearFormMeta
    [MetaType.UserDialog]?: IUserDialogMeta
    [MetaType.ValidationMessages]?: IValidationMessagesMeta
}

export interface IActionMeta {
    fail?: IMeta
    success?: IMeta
}

export interface IRefreshMeta {
    datasources: string[]
}

export interface IRedirectMeta {
    path: string
    target: LinkTarget
    // TODO сюда тоже закинуть тип IMapping
    pathMapping?: Record<string, unknown>
    queryMapping?: Record<string, unknown>
}

export interface IAlertMeta {
    alert: {
        messages: unknown
        stacked: unknown
    }
}

export interface IClearFormMeta {
    clearForm: string
}

export interface IUserDialogMeta {
    // TODO тут тип пропсов PageDialog должен быть
    dialog: {
        title: string
        description: string
        toolbar: unknown
        [key: string]: unknown
    }
}

export interface IValidationFieldMessage {
    id: string | number
    field: string
    placement: string
    severity: string
    closeButton: boolean
    text: string
    timeout?: number
}

export interface IValidationMessagesMeta {
    form: string
    fields: Record<string, IValidationFieldMessage>
}
