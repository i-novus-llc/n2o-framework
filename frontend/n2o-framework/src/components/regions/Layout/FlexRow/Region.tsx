import React from 'react'
import classNames from 'classnames'
import { FlexRow } from '@i-novus/n2o-components/lib/display/Layout/FlexRow'

import { type RowRegionProps } from '../types'
import { LayoutContent } from '../LayoutContent'

export function Region({ wrap, className, style, align, justify, content }: RowRegionProps) {
    return (
        <FlexRow
            style={style}
            className={classNames('n2o-layout-flex-row', className)}
            wrap={wrap}
            align={align}
            justify={justify}
        >
            <LayoutContent content={content} />
        </FlexRow>
    )
}
