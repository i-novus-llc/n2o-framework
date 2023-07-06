export interface IOverlayProps {
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

export interface IOverlay {
    visible: boolean
    name: string
    mode: 'modal' | 'drawer' | 'dialog'
    props?: IOverlay
    showPrompt?: boolean
}

export type State = IOverlay[]
