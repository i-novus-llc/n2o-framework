import { CSSProperties } from 'react'

import { type ContentMeta } from '../../../ducks/regions/Regions'
import { WithGetWidgetProps } from '../withWidgetProps'

export interface NoneRegionProps extends WithGetWidgetProps {
    id: string
    content?: ContentMeta[]
    className?: string
    style?: CSSProperties
    pageId: string
    disabled: boolean
    parent: string
    visible: boolean
}
