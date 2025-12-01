import React from 'react'
import { Row } from '@i-novus/n2o-components/lib/display/Layout/Row'

import { type RowRegionProps } from '../types'
import { LayoutContent } from '../LayoutContent'

export function Region({ content, ...rest }: RowRegionProps) {
    return <Row {...rest}><LayoutContent content={content} /></Row>
}
