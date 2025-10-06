import { CSSProperties } from 'react'
import {
    type PanelProps,
    type LinkProps,
    type GroupProps,
    type DropdownProps,
    LinkTarget,
    Position,
    GroupView,
} from '@i-novus/n2o-components/lib/navigation/types'

import { type Mapping } from '../../ducks/datasource/Provider'
import { type DataSourceModels, ModelPrefix } from '../../core/datasource/const'

export type Content = Array<{
    src: string
    id: string
}>

interface BaseNavigationItem {
    src: string
    id: string
    label: string
    icon?: string
    iconPosition?: Position
    visible?: boolean | string
    enabled?: boolean | string
    datasource: string
    model: ModelPrefix
    className?: string
    style?: CSSProperties
}

interface WithDataSourceFeatures {
    models?: DataSourceModels
}

export interface BaseNavigationContent {
    content: Content
}

export interface NavigationPanelProps extends PanelProps, BaseNavigationContent {
    src: string
    datasource: string
    model: string
}

type NavigationLinkPropsEnhancer = BaseNavigationItem & WithDataSourceFeatures

export interface NavigationLinkProps extends NavigationLinkPropsEnhancer {
    url: string
    rootClassName?: string
    target: LinkTarget
    queryMapping: Mapping
    pathMapping: Mapping
}

type NavigationGroupPropsEnhancer = BaseNavigationItem & BaseNavigationContent & WithDataSourceFeatures

export interface NavigationGroupProps extends NavigationGroupPropsEnhancer {
    collapsible: boolean
    defaultState: GroupView
}

export type NavigationDropdownProps = BaseNavigationItem & BaseNavigationContent & WithDataSourceFeatures

export type FactoryComponentProps = PanelProps | LinkProps | GroupProps | DropdownProps
