import { metaPropsType } from '../../plugins/utils'

export type ModelLinkType = string
export type ModelType = metaPropsType
export type IDependency = { condition: string, on: ModelLinkType }

export interface IModel {
    model: ModelType
    config: IDependency
}

export type OptionsType = IModel[]

export interface IDependencies {
    visible?: IDependency[]
    fetch?: IDependency[]
    enabled?: IDependency[]
}

export type PossibleDependencies = 'fetch' | 'visible' | 'enabled'

export interface IWidgetDependencies {
    dependency?: IDependencies
    parents?: string[]
    widgetId?: string
}

export type IWidgetsDependencies = Record<string, IWidgetDependencies>
