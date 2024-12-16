import { CSSProperties } from 'react'

import { ToolbarProps } from '../../buttons/Toolbar'

export interface Props {
    id: string
    toolbar: Record<string, ToolbarProps>
    className?: string
    style?: CSSProperties
    html: string
    loading: boolean
    datasource: string
}
