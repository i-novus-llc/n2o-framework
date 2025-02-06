import type { LayoutType, Margin, StackOffsetType } from 'recharts/types/util/types'
import { CSSProperties } from 'react'

import { ToolbarProps } from '../../buttons/Toolbar'
import { Widget } from '../../../ducks/widgets/Widgets'

export enum CHART_TYPE {
    LINE = 'line',
    AREA = 'area',
    BAR = 'bar',
    PIE = 'pie',
}

export interface DataItem {
    hasLabel?: string
    label?: string
    dataKey: string
    stroke?: string
    name?: string
}

export interface Props {
    stackOffset?: StackOffsetType
    width: number
    height: number
    layout: LayoutType
    data: DataItem[]
    margin: Margin
    baseValue: unknown
    XAxis: Record<string, unknown>
    YAxis: Record<string, unknown>
    cartesianGrid: Record<string, unknown>
    tooltip: Record<string, unknown>
    legend: Record<string, unknown>
    areas?: DataItem[]
    lines?: DataItem[]
    bars?: DataItem[]
    pie?: Record<string, unknown>
    size?: number
    barCategoryGap?: number
    barGap?: number
    barSize?: number
    maxBarSize?: number
    reverseStackOrder?: boolean
}

export interface ChartWidgetProps extends Widget {
    id: string
    datasource: string
    toolbar: Record<string, ToolbarProps>
    disabled: boolean
    chart: { type: CHART_TYPE, width: Props['width'], height: Props['height'] } & Props
    filter: Record<string, unknown>
    className: string
    style: CSSProperties
    loading: boolean
}
