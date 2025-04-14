import React from 'react'

import { DefaultPage } from '../DefaultPage'
import { PageRegions } from '../PageRegions'
import { type PageWithRegionsProps } from '../types'
import { EMPTY_OBJECT } from '../../../utils/emptyTypes'

/**
 * Страница с одной зоной
 */
export function StandardPage({ id, regions = EMPTY_OBJECT, routable, ...rest }: PageWithRegionsProps) {
    return (
        <DefaultPage {...rest}>
            <div className="n2o-page n2o-page__single-layout">
                <PageRegions id={id} regions={regions} routable={routable} />
            </div>
        </DefaultPage>
    )
}

export default StandardPage
