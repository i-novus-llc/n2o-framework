import { ModelPrefix } from '../../core/datasource/const'

export enum ConfirmMode {
    POPOVER = 'popover',
    MODAL = 'modal',
}

type Color = 'info' | 'danger' | 'warning' | 'success' | 'primary' | 'secondary' | 'link'

export type ConfirmJSON = {
    text: string
    title: string
    modelLink: `models.${ModelPrefix}[${string}`
    condition: `\`${string}\``
    closeButton: boolean
    reverseButtons: boolean
    mode: ConfirmMode
    ok: {
        label: string
        color: Color
    },
    cancel: {
        label: string
        color: Color
    }
}

export type ConfirmProps = {
    text: string
    title: string
    closeButton: boolean
    reverseButtons: boolean
    ok: {
        label: string
        color: Color
    },
    cancel: {
        label: string
        color: Color
    }
    onConfirm(): void;
    onDeny(): void;
}
