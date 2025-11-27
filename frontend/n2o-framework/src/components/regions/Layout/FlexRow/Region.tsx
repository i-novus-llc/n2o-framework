import React from 'react'
import { FlexRow } from '@i-novus/n2o-components/lib/display/Layout/FlexRow'

import { type RowRegionProps } from '../types'
import { LayoutContent } from '../LayoutContent'

export function Region({ wrap, align, justify, content }: RowRegionProps) {
    return (
        <FlexRow className="n2o-layout-flex-row" wrap={wrap} align={align} justify={justify}>
            <LayoutContent content={content} />
        </FlexRow>
    )
}
