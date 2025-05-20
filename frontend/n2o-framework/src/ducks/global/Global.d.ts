import { type ErrorContainerError } from '../../core/error/types'
import { Locale, LocaleArray, type Locales } from '../../locales'
import { type SimpleHeaderBodyProps } from '../../plugins/Header/SimpleHeader/SimpleHeader'
import { type SidebarProps } from '../../plugins/SideBar/types'

export interface Config {
    datasources?: Record<string, unknown>
    header?: SimpleHeaderBodyProps
    layout?: Record<string, unknown>
    sidebars?: SidebarProps[]
    footer?: Record<string, unknown>
}

export type State = {
    activePages: string[]
    breadcrumbs: Record<string, string>
    ready: boolean
    loading: boolean
    error?: ErrorContainerError
    locale: Locale
    locales?: LocaleArray
    messages: Record<string, unknown>
    menu: Config
    rootPageId: string | null
    user?: object
    redirectPath?: string
}

export type RouterState = {
    location: { search: string }
    action: string
}
