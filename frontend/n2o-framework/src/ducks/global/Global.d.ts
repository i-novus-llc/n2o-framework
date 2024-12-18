export type State = {
    activePages: string[]
    breadcrumbs: Record<string, string>
    ready: boolean
    loading: boolean
    error: object | null
    locale: 'ru' | 'en'
    locales?: ['ru', 'en', 'fr']
    messages: Record<string, unknown>
    menu: {
        layout?: Record<string, unknown>
        header?: Record<string, unknown>
        footer?: Record<string, unknown>
        datasources?: Record<string, unknown>
    }
    rootPageId: string | null
    user?: object
    redirectPath?: string
}

export type RouterState = {
    location: { search: string }
    action: string
}
