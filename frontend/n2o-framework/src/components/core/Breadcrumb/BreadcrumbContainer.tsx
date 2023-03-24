import React, { useContext } from 'react'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { useModel } from '../hooks/useModel'

import { IBreadcrumbContainer } from './const'
import { breadcrumbResolver } from './breadcrumbResolver'

export function BreadcrumbContainer({
    modelPrefix,
    datasource,
    breadcrumb = [],
}: IBreadcrumbContainer): JSX.Element | null {
    const { getComponent } = useContext(FactoryContext)
    const FactoryBreadcrumb = getComponent('DefaultBreadcrumb', FactoryLevels.BREADCRUMBS)
    const model = useModel(datasource, modelPrefix)

    if (!breadcrumb.length || !FactoryBreadcrumb) {
        return null
    }

    const resolvedBreadcrumb = (modelPrefix && datasource)
        ? breadcrumbResolver(model, breadcrumb)
        : breadcrumb

    return (
        <FactoryBreadcrumb items={resolvedBreadcrumb} />
    )
}
