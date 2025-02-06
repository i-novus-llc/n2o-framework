import { CSSProperties, ReactNode } from 'react'

import { Props as StandardWidgetProps } from '../StandardWidget'
import { type Props as N2OPaginationProps } from '../Table/N2OPagination'
import { Widget } from '../../../ducks/widgets/Widgets'

export interface ListMoreButtonProps {
    onClick(): void
    style?: CSSProperties
}

export interface HeaderWrapperProps {
    children?: ReactNode
    isValid: boolean
}

export interface ListItemProps {
    leftTop?: { src: string, alt: string }
    leftBottom?: string
    header?: string
    subHeader?: string
    body?: string
    rightTop?: string
    rightBottom?: string
    extra?: string
    selected?: boolean
    onClick(): void
    divider?: boolean
    style?: CSSProperties
    hasSelect?: boolean
    measure(): void
}

export type ListDataItem = { id: string }

export interface ListProps extends Pick<ListItemProps, 'divider'> {
    fetchOnScroll?: boolean
    hasSelect?: boolean
    data: ListDataItem[]
    selectedId: string
    onFetchMore(): void
    hasMoreButton?: boolean
    className?: string
    maxHeight: number
    t(text: string): string
    onItemClick(item: number): void
    rowClick: boolean | null
    rows: { disabled: boolean }
}

export interface ListContainerProps extends ListProps {
    placeholder: string
    datasourceModel: Array<Record<string, unknown>>
    setResolve(model: Record<string, unknown>): void
    onRowClickAction(model: Record<string, unknown>): void
    datasource: string
    id: string
    page: number
    setPage(page: number): void
    list: Record<string, { id: string, columnId: string }>
}

type Enhancer = StandardWidgetProps & ListContainerProps & Widget

export interface ListWidgetProps extends Enhancer {
    size: number
    count: number
    datasourceModelLength: number
}
