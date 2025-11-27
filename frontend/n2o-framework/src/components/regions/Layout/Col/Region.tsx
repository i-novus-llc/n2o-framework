import React from 'react'
import { Col } from '@i-novus/n2o-components/src/display/Layout/Col'

import { type ColRegionProps } from '../types'
import { LayoutContent } from '../LayoutContent'

export function Region({ content = [], ...rest }: ColRegionProps) {
    return <Col {...rest}><LayoutContent content={content} /></Col>
}
