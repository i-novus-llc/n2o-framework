import { CSSProperties } from 'react'
import { Dispatch } from 'redux'

import { type ToolbarProps } from '../../buttons/Toolbar'
import { type ClickAction } from '../hooks/useOnActionMethod'

export type TilesModel = Record<string, unknown>

export interface TilesCellType {
    component: {
        src: string
        className: string
    }
    model: TilesModel
    style?: CSSProperties;
    className?: string
    index?: number
    widgetId?: string | number
    onResolve(): void
    dispatch: Dispatch
    datasource: string
}

export interface TilesType {
    data: TilesModel[]
    tile: Array<Omit<TilesCellType, 'model' | 'onResolve' | 'dispatch' | 'datasource'>>
    className?: string
    widgetId: TilesCellType['widgetId']
    colsSm: number
    colsMd: number
    colsLg: number
    width: number
    onResolve: TilesCellType['onResolve']
    dispatch: TilesCellType['dispatch']
    datasource: TilesCellType['datasource']
    tileWidth: string
    tileHeight: string
    onRowClickAction: ClickAction
    rowClick: ClickAction
}

export interface TilesWidgetType extends Omit<TilesType, 'widgetId' | 'data' | 'onResolve' | 'width'> {
    id: string
    toolbar: ToolbarProps
    disabled: boolean
    style: CSSProperties
    filter: Record<string, unknown>
    paging: {
        place: string
    }
    height: string
    width: string
    size: number
    count: number
    setPage(): void
    page: number
    loading: boolean
    setResolve(): void
}
