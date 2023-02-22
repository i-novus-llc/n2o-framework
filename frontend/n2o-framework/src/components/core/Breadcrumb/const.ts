import { IDataSourceModels, ModelPrefix } from '../../../core/datasource/const'

/* a label type equal to null is required during expression parsing */
export interface ICrumb {
    label: string | null
    path?: string
}

export type breadcrumb = ICrumb[]

export interface IBreadcrumbContainer {
    breadcrumb: breadcrumb
    models: IDataSourceModels,
    modelPrefix?: ModelPrefix
    datasource: string
}
