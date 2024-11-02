import { CSSProperties, ReactNode } from 'react'
import { Dispatch } from 'redux'

import { DataSourceState } from '../../ducks/datasource/DataSource'
import { ModelPrefix } from '../../core/datasource/const'
import { ToolbarProps } from '../buttons/Toolbar'
import { type Breadcrumb } from '../core/Breadcrumb/const'
import { State as RegionsState } from '../../ducks/regions/Regions'
import { Widget } from '../../ducks/widgets/Widgets'

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
}

export interface PageRegionsProps {
    id: string
    regions: {
        left?: RegionsState[]
        right?: RegionsState[]
    }
    width?: Record<string, string>
    routable?: boolean
}

export type PageWithRegionsProps = DefaultPageProps & PageRegionsProps

export type SearchablePageProps = PageWithRegionsProps & {
    pageId: string
    withToolbar: boolean
    initSearchValue?: string
}
