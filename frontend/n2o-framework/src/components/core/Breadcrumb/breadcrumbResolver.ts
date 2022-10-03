import { IDataSourceModels, ModelPrefixes } from '../../../core/datasource/const'
import { textResolver } from '../textResolver'

import { breadcrumb } from './const'

export const breadcrumbResolver = (
    models: IDataSourceModels,
    breadcrumb: breadcrumb,
    modelPrefix?: ModelPrefixes,
): breadcrumb => {
    if (!modelPrefix) {
        return breadcrumb
    }

    return breadcrumb.map(({ label, ...rest }) => {
        const resolvedLabel = textResolver(models, label, modelPrefix)

        return { label: resolvedLabel, ...rest }
    })
}
