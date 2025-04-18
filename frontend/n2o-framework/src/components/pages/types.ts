import { CSSProperties, ReactNode } from 'react'
import { Dispatch } from 'redux'
import { type State as ScrollContextProps } from '@i-novus/n2o-components/lib/layouts/ScrollContainer'

import { ToolbarProps } from '../buttons/Toolbar'
import { State as RegionsState } from '../../ducks/regions/Regions'
import { Metadata } from '../../ducks/pages/Pages'

export enum FIXED_PLACE {
    TOP = 'top',
    LEFT = 'left',
    RIGHT = 'right',
}

export type Places = Record<FIXED_PLACE, CSSProperties & { fixed: boolean }>

export interface DefaultPageProps {
    id?: string
    metadata: Metadata,
    toolbar?: {
        topLeft?: ToolbarProps
        topCenter?: ToolbarProps
        topRight?: ToolbarProps
        bottomLeft?: ToolbarProps
        bottomCenter?: ToolbarProps
        bottomRight?: ToolbarProps
    };
    entityKey?: string;
    error?: false | {
        severity: string
        text: string
    }
    children?: ReactNode
    disabled?: boolean
    dispatch: Dispatch
    rootPage?: boolean
    scroll: boolean
}

export interface PageRegionsProps {
    id?: string
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
