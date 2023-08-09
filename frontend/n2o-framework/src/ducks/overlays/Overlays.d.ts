export interface OverlayProps {
    pageId: string
    pageUrl: string
    pathMapping: {
        queryMapping: object
        size: string
    }
    scrollable: boolean
    closeButton: boolean
    prompt: boolean
    hasHeader: boolean
    backdrop: string
    mode?: boolean
}

export interface Overlay {
    visible: boolean
    name: string
    mode: 'modal' | 'drawer' | 'dialog'
    props?: Overlay
    showPrompt?: boolean
}

export type State = Overlay[]
