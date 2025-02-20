import React, { useContext, ReactElement } from 'react'
import get from 'lodash/get'
import { withRouter, type RouteComponentProps } from 'react-router-dom'

import { EMPTY_OBJECT } from '../../utils/emptyTypes'
import { ApplicationContext, type Config } from '../../components/core/Application'
import { type SidebarProps } from '../SideBar/types'

import { getMatchingSidebar } from './helpers'

type EnhancedProps = RouteComponentProps & Config

export interface ConfigContainerProps extends EnhancedProps {
    headerItems: Config['header']['menu']
    headerExtraItems: Config['header']['extraMenu']
    sidebarItems: SidebarProps['menu']
    sidebarExtraItems: SidebarProps['extraMenu']
    sidebar?: SidebarProps
    render(props: ConfigContainerProps): ReactElement | null
}

export type Items = ConfigContainerProps['headerItems'] | ConfigContainerProps['sidebarItems']
export type ExtraItems = ConfigContainerProps['headerExtraItems'] | ConfigContainerProps['sidebarExtraItems']

export enum TYPE {
    HEADER = 'header',
    SIDEBAR = 'sidebar',
}

export const MenuContainer = (props: ConfigContainerProps): ReactElement | null => {
    const {
        headerItems,
        headerExtraItems,
        sidebarItems,
        sidebarExtraItems,
        header,
        sidebar,
        location,
        datasources = EMPTY_OBJECT,
        render = () => null,
    } = props

    const mapRenderProps = (): ConfigContainerProps => {
        if (!header && !sidebar) { return props }

        const withSecurityProps = (
            items: Items,
            extraItems: ExtraItems,
            type: TYPE,
        ) => ({
            [type]: {
                ...props[type],
                datasources,
                location,
                menu: { items },
                extraMenu: extraItems,
            },
        })

        return {
            ...props,
            ...withSecurityProps(headerItems, headerExtraItems, TYPE.HEADER),
            ...withSecurityProps(sidebarItems, sidebarExtraItems, TYPE.SIDEBAR),
        }
    }

    const renderedContent = render(mapRenderProps())

    return renderedContent || null
}

interface ConfigContainerInnerProps extends RouteComponentProps {
    render(props: ConfigContainerProps): ReactElement | null
}

const ConfigContainerInner: React.FC<ConfigContainerInnerProps> = ({ location, render, ...rest }) => {
    const { getFromConfig } = useContext(ApplicationContext)

    const configProps = getFromConfig?.('menu') as Config
    const { header = {}, sidebars = [] } = configProps || {}

    const { pathname } = location
    const sidebar = getMatchingSidebar(sidebars, pathname) || {}
    const { path } = sidebar

    return (
        <MenuContainer
            {...rest}
            {...configProps}
            sidebar={{ ...sidebar, id: `Sidebar:${path}` }}
            headerItems={get(header, 'menu.items', [])}
            headerExtraItems={get(header, 'extraMenu.items', [])}
            sidebarItems={get(sidebar, 'menu.items', [])}
            sidebarExtraItems={get(sidebar, 'extraMenu.items', [])}
            location={location}
            render={render}
        />
    )
}

export const ConfigContainer = withRouter(ConfigContainerInner)
export default ConfigContainer
