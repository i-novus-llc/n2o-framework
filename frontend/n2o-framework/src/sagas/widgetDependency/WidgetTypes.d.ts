import { DEPENDENCY_TYPES } from '../../core/dependencyTypes'
import { type Model } from '../../ducks/models/selectors'
import { type ModelLink } from '../../core/models/types'

export type Dependency = { condition: string, on: ModelLink }

export interface ResolveOption {
    model: Model
    config: Dependency
}

export type Dependencies = Partial<Record<DEPENDENCY_TYPES, Dependency[]>>

export interface WidgetDependencies {
    dependency?: Dependencies
    widgetId: string
}

export type WidgetsDependencies = Record<string, WidgetDependencies>
