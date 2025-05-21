export type ElementVisibility = {
    header: boolean,
    footer?: boolean,
    breadcrumb: boolean,
}

export type WindowType = Window & typeof globalThis & {
    N2O_ELEMENT_VISIBILITY: ElementVisibility
    N2O_WARNING_WATCHER?: boolean
    _n2oEvalContext: Record<string, unknown>
}
