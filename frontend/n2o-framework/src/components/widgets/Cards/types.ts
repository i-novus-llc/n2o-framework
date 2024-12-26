import { Dispatch } from 'redux'
import { CSSProperties } from 'react'

import { type Props as StandardWidgetProps } from '../StandardWidget'
import { type Props as N2OPaginationProps } from '../Table/N2OPagination'
import { type WidgetProps } from '../Widget/WithActiveModel'
import { type ClickAction } from '../hooks/useOnActionMethod'

export interface CardCellProps {
    component?: { src: string }
    model: { id: string }
    dispatch: Dispatch
    onResolve: WidgetProps['setResolve']
    className?: string
    style?: CSSProperties
    index: number
    widgetId?: string
    datasource: string
    id?: string
    key?: string | number
}

type CommonCardProps = 'index' | 'datasource' | 'model' | 'dispatch' | 'onResolve' | 'id' | 'className'

export interface CardProps extends Pick<CardCellProps, CommonCardProps> {
    card: {
        content: CardCellProps[]
        col: string
    }
    alignStyle: CSSProperties
}

type CommonCardsProps = 'className' | 'id' | 'onResolve' | 'dispatch' | 'datasource'

export interface CardsProps extends Pick<CardProps, CommonCardsProps> {
    cards: Array<CardProps['card']>
    data: Array<CardProps['model']>
    align: 'top' | 'bottom'
    height: string
    datasourceModelLength?: number
}

export type CardsContainerProps = Omit<CardsProps, 'onResolve'> & { setResolve: WidgetProps['setResolve'] }

type CommonCardsWidgetProps = 'className' | 'cards' | 'datasource' | 'data' | 'dispatch' | 'setResolve'

export interface CardsWidgetProps extends Pick<CardsContainerProps, CommonCardsWidgetProps> {
    toolbar: StandardWidgetProps['toolbar']
    disabled: StandardWidgetProps['disabled']
    style?: CSSProperties
    filter: StandardWidgetProps['filter']
    paging: { place: string } & N2OPaginationProps
    loading: StandardWidgetProps['loading']
    verticalAlign: CardsProps['align']
    height: string
    size: N2OPaginationProps['size']
    count: N2OPaginationProps['count']
    page: number
    setPage: N2OPaginationProps['setPage']
    datasourceModelLength: number
    id: string
    rowClick: ClickAction
}
