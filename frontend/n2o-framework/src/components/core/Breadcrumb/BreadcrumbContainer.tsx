import React, { useContext } from 'react'
import { useSelector } from 'react-redux'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { useModel } from '../hooks/useModel'
import { makePageByIdSelector } from '../../../ducks/pages/selectors'
import { activePageSelector } from '../../../ducks/global/selectors'
import { useResolved } from '../../../core/Expression/useResolver'

import { Breadcrumb } from './const'

const useBreadCrumbs = () => {
    const activePage = useSelector(activePageSelector)
    const page = useSelector(makePageByIdSelector(activePage || ''))
    const breadcrumb: Breadcrumb = page?.metadata.breadcrumb || []
    const modelPrefix = page?.metadata.page?.model
    const datasource = page?.metadata.page?.datasource

    const model = useModel(datasource, modelPrefix)
    const resolvedBreadcrumb = useResolved({ breadcrumb }, model).breadcrumb

    return (modelPrefix && datasource) ? resolvedBreadcrumb : breadcrumb
}

export function BreadcrumbContainer(): JSX.Element | null {
    const breadcrumb = useBreadCrumbs()
    const { getComponent } = useContext(FactoryContext)
    const FactoryBreadcrumb = getComponent('DefaultBreadcrumb', FactoryLevels.BREADCRUMBS)

    if (!breadcrumb.length || !FactoryBreadcrumb) { return null }

    return <FactoryBreadcrumb items={breadcrumb} />
}
