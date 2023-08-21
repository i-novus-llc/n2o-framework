interface VisibleDependency {
    on: string
    condition: string
}

export interface Content {
    src: string
    id: string
    name: string
    datasource?: string
    fetchOnVisibility?: boolean
    fetchOnInit?: boolean
    content?: Content[]
    visible?: boolean
    dependency?: {
        visible?: VisibleDependency[]
    }
    activeEntity?: string
    lazy?: boolean
    tabs?: Tab[]
}

export interface Tab {
    id: string
    label: string
    opened: boolean
    content: Content[]
    invalid?: boolean
    security?: {
        object: {
            permissions: string[]
            roles: string[]
        }
    }
}

export interface Region {
    regionId: string | null
    activeEntity: string | boolean | null
    isInit: boolean
    panels: Array<Record<string, unknown>>
    datasource: string | null
    lazy?: boolean
    tabs: Tab[]
    alwaysRefresh?: boolean
}

export type State = {
    [regionId: string]: Region
}
