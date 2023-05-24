export type TGlobalState = {
    breadcrumbs: Record<string, string>
    ready: boolean
    loading: boolean
    error: object | null
    locale: 'ru'| 'en'
    messages: object
    menu: object
    rootPageId: string | null
}
