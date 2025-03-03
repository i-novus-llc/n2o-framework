import { CSSProperties, ReactNode } from 'react'
import { Dispatch } from 'redux'
import { type State as ScrollContextProps } from '@i-novus/n2o-components/lib/layouts/ScrollContainer'

import { DataSourceState } from '../../ducks/datasource/DataSource'
import { ModelPrefix } from '../../core/datasource/const'
import { ToolbarProps } from '../buttons/Toolbar'
import { type Breadcrumb } from '../core/Breadcrumb/const'
import { State as RegionsState } from '../../ducks/regions/Regions'
import { Widget } from '../../ducks/widgets/Widgets'

export enum FIXED_PLACE {
    TOP = 'top',
    LEFT = 'left',
    RIGHT = 'right',
}

export type Places = Record<FIXED_PLACE, CSSProperties & { fixed: boolean }>

export interface DefaultPageProps {
    id?: string
    metadata?: {
        style?: CSSProperties
        className?: string
        datasources: Record<string, DataSourceState>
        id?: string
        widget?: Widget
        page: {
            title?: string
            htmlTitle?: string
            datasource?: string
            model: ModelPrefix
        }
        breadcrumb: Breadcrumb
        width: Record<string, string>
        searchBar: {
            className: string
            datasource: string
            fieldId: string
            placeholder: string
            throttleDelay: number
            trigger: 'CHANGE' | 'CLICK'
        }
        toolbar?: {
            topLeft?: ToolbarProps
            topCenter?: ToolbarProps
            topRight?: ToolbarProps
            bottomLeft?: ToolbarProps
            bottomCenter?: ToolbarProps
            bottomRight?: ToolbarProps
        }
        places?: Places
        needScrollButton?: boolean
    }
    toolbar?: {
        topLeft?: ToolbarProps
        topCenter?: ToolbarProps
        topRight?: ToolbarProps
        bottomLeft?: ToolbarProps
        bottomCenter?: ToolbarProps
        bottomRight?: ToolbarProps
    };
    entityKey?: string;
    error?: false | { [key: string]: string }
    children?: ReactNode
    disabled?: boolean
    dispatch: Dispatch
    rootPage?: boolean
}

export interface PageRegionsProps {
    id: string
    regions: {
        left?: RegionsState[]
        right?: RegionsState[]
        top?: RegionsState[]
    }
    width?: Record<string, string>
    routable?: boolean
}

export type PageWithRegionsProps = DefaultPageProps & PageRegionsProps

export type SearchablePageProps = PageWithRegionsProps & {
    pageId: string
    initSearchValue?: string
}

export type TopLeftRightPageProps = PageWithRegionsProps & {
    style: CSSProperties
    isFixed: boolean
    scrollContext: ScrollContextProps
}
