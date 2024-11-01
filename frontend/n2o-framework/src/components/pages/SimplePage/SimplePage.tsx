import React from 'react'
import get from 'lodash/get'

import { WIDGETS } from '../../../core/factory/factoryLevels'
import { Factory } from '../../../core/factory/Factory'
import { DefaultPage } from '../DefaultPage'
import { type DefaultPageProps } from '../types'

export function SimplePage({ id, metadata, ...rest }: DefaultPageProps) {
    const widget = get(metadata, 'widget', {})

    return (
        <DefaultPage metadata={metadata} {...rest}>
            <div className="n2o-simple-page">
                <div>
                    <Factory key={`simple-page-${id}`} level={WIDGETS} {...widget} pageId={id} />
                </div>
            </div>
        </DefaultPage>
    )
}

export default SimplePage
