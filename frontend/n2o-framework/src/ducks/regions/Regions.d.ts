import { Tab } from '@i-novus/n2o-components/lib/display/Tabs/Tabs'

import { ServiceInfo } from './Actions'

interface ConditionParams {
    expression: string
    modelLink: string
}

interface Condition {
    enabled?: ConditionParams[]
    visible?: ConditionParams[]
}

export interface TabMeta {
    content: ContentMeta[]
    enabled?: boolean
    visible?: boolean
    conditions?: Condition
    icon?: string
    id: string
    invalid?: boolean
    label: string
    opened: boolean
    tooltip?: string
    className?: string
}

export interface ContentMeta {
    alwaysRefresh: boolean
    children?: boolean
    className?: string
    content?: ContentMeta[]
    hideSingleTab: boolean
    id: string
    lazy?: boolean
    pageId: string
    scrollbar: boolean
    src: string
    fetchOnInit: boolean
    tabs?: Tab[]
    visible?: boolean
    datasource: string
    form?: {fieldsets: string[]}
}

export interface Region {
    regionId: string | null
    activeEntity: string | boolean | null
    isInit: boolean
    panels: Array<Record<string, unknown>>
    datasource: string | null
    lazy?: boolean
    tabs: TabMeta[]
    alwaysRefresh?: boolean
    serviceInfo: ServiceInfo
    parent?: {
        regionId: string
        tabId: string
    }
    visible: boolean
}

export type State = {
    [regionId: string]: Region
}
