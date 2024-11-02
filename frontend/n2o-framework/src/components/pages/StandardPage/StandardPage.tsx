import React from 'react'

import { DefaultPage } from '../DefaultPage'
import { PageRegions } from '../PageRegions'
import { type PageWithRegionsProps } from '../types'

/**
 * Страница с одной зоной
 */

export function StandardPage({ id, regions, routable, ...rest }: PageWithRegionsProps) {
    return (
        <DefaultPage {...rest}>
            <div className="n2o-page n2o-page__single-layout">
                <PageRegions id={id} regions={regions} routable={routable} />
            </div>
        </DefaultPage>
    )
}

StandardPage.defaultProps = {
    regions: {},
}

export default StandardPage
