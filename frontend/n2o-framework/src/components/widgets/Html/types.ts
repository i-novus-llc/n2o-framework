import { CSSProperties } from 'react'

import { ToolbarProps } from '../../buttons/Toolbar'
import { Widget } from '../../../ducks/widgets/Widgets'

export interface HtmlWidgetProps extends Widget {
    id: string
    toolbar: Record<string, ToolbarProps>
    className?: string
    style?: CSSProperties
    html: string
    loading: boolean
    datasource: string
}
