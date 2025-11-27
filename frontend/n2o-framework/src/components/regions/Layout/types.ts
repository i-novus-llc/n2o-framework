import { type RowProps, type ColProps } from '@i-novus/n2o-components/src/display/Layout/types'

import { type ContentMeta } from '../../../ducks/regions/Regions'

export interface RowRegionProps extends RowProps {
    content: ContentMeta[]
    id: string
    pageId: string
    routable: boolean
    parent: string
}

type Enhancer = ColProps & ContentMeta

export interface ColRegionProps extends Enhancer {
    content?: ContentMeta[]
    id: string
    pageId: string
    routable?: boolean
    fetch?: string
    parent?: string
}
