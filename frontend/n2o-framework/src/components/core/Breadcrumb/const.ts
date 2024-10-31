import { DataSourceModels, ModelPrefix } from '../../../core/datasource/const'

/* a label type equal to null is required during expression parsing */
export interface Crumb {
    label: string | null
    path?: string
}

export type breadcrumb = Crumb[]

export interface BreadcrumbContainer {
    breadcrumb?: breadcrumb
    models?: DataSourceModels,
    modelPrefix?: ModelPrefix
    datasource?: string
}
