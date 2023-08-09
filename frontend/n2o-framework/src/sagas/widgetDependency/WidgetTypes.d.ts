import { metaPropsType } from '../../plugins/utils'

export type ModelLinkType = string
export type ModelType = metaPropsType
export type Dependency = { condition: string, on: ModelLinkType }

export interface Model {
    model: ModelType
    config: Dependency
}

export type OptionsType = Model[]

export interface Dependencies {
    visible?: Dependency[]
    fetch?: Dependency[]
    enabled?: Dependency[]
}

export type PossibleDependencies = 'fetch' | 'visible' | 'enabled'

export interface WidgetDependencies {
    dependency?: Dependencies
    parents?: string[]
    widgetId?: string
}

export type WidgetsDependencies = Record<string, WidgetDependencies>
