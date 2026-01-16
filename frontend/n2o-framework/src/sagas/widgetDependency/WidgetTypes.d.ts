import { DEPENDENCY_TYPES } from '../../core/dependencyTypes'
import { Model } from '../../ducks/models/selectors'

export type ModelLinkType = string
export type Dependency = { condition: string, on: ModelLinkType }

export interface ResolveOption {
    model: Model
    config: Dependency
}

export type Dependencies = Partial<Record<DEPENDENCY_TYPES, Dependency[]>>

export interface WidgetDependencies {
    dependency?: Dependencies
    parents?: string[]
    widgetId: string
}

export type WidgetsDependencies = Record<string, WidgetDependencies>
