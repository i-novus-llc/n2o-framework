import { CSSProperties } from 'react'

import type { ContentMeta, State as RegionsState } from '../../../ducks/regions/Regions'
import { State } from '../../../ducks/widgets/Widgets'

export interface ListRegionProps {
    id: string
    parent?: string
    getWidgetProps(id: string): Record<string, unknown>
    className?: string
    style?: CSSProperties
    disabled?: boolean
    isVisible?: boolean
    label: string
    pageId: string
    regionsState: RegionsState
    widgetsState: State
    collapsible?: boolean
    expand?: boolean
    hasSeparator?: boolean
    content?: ContentMeta[]
}
