export type TOverlay = {
    visible: boolean
    name: string
    mode: string
    props: Record<string, unknown>
    showPrompt?: boolean
}

export type TOverlayState = TOverlay[]
