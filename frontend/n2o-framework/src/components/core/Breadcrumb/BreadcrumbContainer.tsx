import React, { useContext } from 'react'
import { useSelector } from 'react-redux'

// @ts-ignore ignore import error from js file
import { WithDataSource } from '../../../core/datasource/WithDataSource'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { dataSourceModelsSelector } from '../../../ducks/datasource/selectors'

import { IBreadcrumbContainer } from './const'
import { breadcrumbResolver } from './breadcrumbResolver'

function BreadcrumbContainerBody({
    breadcrumb = [],
    modelPrefix,
    datasource,
}: IBreadcrumbContainer): JSX.Element | null {
    const { getComponent } = useContext(FactoryContext)
    const FactoryBreadcrumb = getComponent('DefaultBreadcrumb', FactoryLevels.BREADCRUMBS)
    const models = useSelector(dataSourceModelsSelector(datasource))

    if (!breadcrumb.length || !FactoryBreadcrumb) {
        return null
    }

    const resolvedBreadcrumb = breadcrumbResolver(models, breadcrumb, modelPrefix)

    return (
        <FactoryBreadcrumb items={resolvedBreadcrumb} />
    )
}

export const BreadcrumbContainer = WithDataSource(BreadcrumbContainerBody)
