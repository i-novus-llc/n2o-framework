import { BadgeType } from '@i-novus/n2o-components/lib/inputs/InputSelect/PopupList'
import { Action, Dispatch } from 'redux'

import { type Model } from '../Widget/WithActiveModel'

export interface FilterProps {
    onFilter(value: string): void
    filterPlaceholder?: string
}

export interface ExpandBtnProps {
    onShowAll(): void
    onHideAll(): void
}

export interface BaseNodeProps {
    prefixCls: string
    imageFieldId: string
    labelFieldId: string
    badge: BadgeType
    valueFieldId: string
    searchValue: string
    searchKeys: string
    data: Record<string, string>
    filter: FilterType
}

export type FilterType = 'includes' | 'startsWith' | 'endsWith'

export interface DatasourceItem {
    id: string
    parentId: string
    [key: string]: string
}

export interface TreeProps {
    id: string
    resolveModel: Record<string, unknown>
    multiselect: boolean
    hasCheckboxes: boolean
    onResolve(keys: string[]): void
    showLine?: boolean
    expandBtn?: boolean
    prefixCls: string
    filterPlaceholder?: string
    datasource: DatasourceItem[]
    iconFieldId: string
    valueFieldId: string
    labelFieldId: string
    parentFieldId: string
    parentIcon: string
    childIcon: string
    value: string
    searchKeys: string[]
    searchValue: string
    filter: FilterType
}

export interface WithWidgetHandlersProps {
    rowClick?: { action: Action }
    setResolve(model: Model | Model[] | null): void
    datasource: Model[]
    valueFieldId: string;
    multiselect: boolean;
    dispatch: Dispatch
}

export enum KEY_CODES {
    KEY_DOWN = 'down',
    KEY_UP = 'up',
    KEY_SPACE = 'space',
    ENTER = 'enter',
    CTRL_ENTER = 'ctrl+enter',
    LEFT = 'left',
    RIGHT = 'right',
}
