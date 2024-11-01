import { DataSourceModels, ModelPrefix } from '../../../core/datasource/const'

/* a label type equal to null is required during expression parsing */
export interface Crumb {
    label: string | null
    path?: string
}

export type Breadcrumb = Crumb[]

export interface BreadcrumbContainer {
    breadcrumb?: Breadcrumb
    models?: DataSourceModels,
    modelPrefix?: ModelPrefix
    datasource?: string
}
