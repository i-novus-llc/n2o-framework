import React, { useContext } from 'react'

// @ts-ignore ignore import error from js file
import { WithDataSource } from '../../../core/datasource/WithDataSource'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'

import { IBreadcrumbContainer } from './const'
import { breadcrumbResolver } from './breadcrumbResolver'

function BreadcrumbContainerBody({
    breadcrumb = [],
    models,
    modelPrefix,
}: IBreadcrumbContainer): JSX.Element | null {
    const { getComponent } = useContext(FactoryContext)
    const FactoryBreadcrumb = getComponent('DefaultBreadcrumb', FactoryLevels.BREADCRUMBS)

    if (!breadcrumb.length || !FactoryBreadcrumb) {
        return null
    }

    const resolvedBreadcrumb = breadcrumbResolver(models, breadcrumb, modelPrefix)

    return (
        <FactoryBreadcrumb items={resolvedBreadcrumb} />
    )
}

export const BreadcrumbContainer = WithDataSource(BreadcrumbContainerBody)
