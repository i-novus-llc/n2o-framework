import { CSSProperties } from 'react'
import { Dispatch } from 'redux'

import { SortDirection } from '../../../../core/datasource/const'

export interface TextTableHeaderProps {
    sortingParam: string
    sorting: SortDirection
    label: string
    style?: CSSProperties
    dispatch?: Dispatch
    widgetId: string
    columnId: string
    visible: boolean
    toggleVisibility(visible: boolean): void
}
