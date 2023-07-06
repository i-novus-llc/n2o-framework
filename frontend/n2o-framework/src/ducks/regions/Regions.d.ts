interface IVisibleDependency {
    on: string
    condition: string
}

export interface IContent {
    src: string
    id: string
    name: string
    datasource?: string
    fetchOnVisibility?: boolean
    fetchOnInit?: boolean
    content?: IContent[]
    visible?: boolean
    dependency?: {
        visible?: IVisibleDependency[]
    }
    activeEntity?: string
    lazy?: boolean
    tabs?: ITab[]
}

export interface ITab {
    id: string
    label: string
    opened: boolean
    content: IContent[]
    invalid?: boolean
    security?: {
        object: {
            permissions: string[]
            roles: string[]
        }
    }
}

export interface IRegion {
    regionId: string | null
    activeEntity: string | boolean | null
    isInit: boolean
    panels: Array<Record<string, unknown>>
    datasource: string | null
    lazy?: boolean
    tabs: ITab[]
    alwaysRefresh?: boolean
}

export type State = {
    [regionId: string]: IRegion
}
