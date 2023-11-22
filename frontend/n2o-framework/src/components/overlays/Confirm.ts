export enum ConfirmMode {
    POPOVER = 'popover',
    MODAL = 'modal',
}

type Color = 'info' | 'danger' | 'warning' | 'success' | 'primary' | 'secondary' | 'link'

export interface ConfirmJSON {
    text: string
    title: string
    modelLink: string
    condition: string
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

export interface ConfirmProps {
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
    id: string
    operation: { id: string, type: string }
    className?: string
    size: 'lg' | 'sm'
}
