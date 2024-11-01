import React from 'react'
import get from 'lodash/get'

import { DefaultPage } from '../DefaultPage'
import { PageRegions } from '../PageRegions'
import { type PageWithRegionsProps } from '../types'

/**
 * Страница с зонами слева и справа
 */

export function LeftRightPage({ id, regions, metadata, ...rest }: PageWithRegionsProps) {
    const width = get(metadata, 'width', {})

    return (
        <DefaultPage metadata={metadata} {...rest}>
            <div className="n2o-page n2o-page__left-right-layout">
                <PageRegions id={id} regions={regions} width={width} />
            </div>
        </DefaultPage>
    )
}

export default LeftRightPage
