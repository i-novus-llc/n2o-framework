import React from 'react'
import PropTypes from 'prop-types'
import { getContext, compose, mapProps } from 'recompose'
import get from 'lodash/get'
import { withRouter } from 'react-router-dom'

import { getMatchingSidebar } from './helpers'

export class MenuContainer extends React.Component {
    mapRenderProps = () => {
        const {
            headerItems, headerExtraItems, sidebarItems, sidebarExtraItems,
            header, sidebar, location, datasources = {},
        } = this.props

        if (!header && !sidebar) {
            return this.props
        }

        const withSecurityProps = (props, items, extraItems, type) => ({
            [type]: {
                ...props,
                datasources,
                location,
                menu: {
                    items,
                },
                extraMenu: extraItems,
            },
        })

        return {
            ...this.props,
            ...withSecurityProps(header, headerItems, headerExtraItems, 'header'),
            ...withSecurityProps(sidebar, sidebarItems, sidebarExtraItems, 'sidebar'),
        }
    }

    render() {
        const { render } = this.props

        return render(this.mapRenderProps())
    }
}

MenuContainer.propTypes = {
    render: PropTypes.func,
    header: PropTypes.object,
    location: PropTypes.object,
    sidebar: PropTypes.object,
    datasources: PropTypes.object,
    headerItems: PropTypes.array,
    headerExtraItems: PropTypes.array,
    sidebarItems: PropTypes.array,
    sidebarExtraItems: PropTypes.array,
}

MenuContainer.defaultProps = {
    render: () => {},
}

export const ConfigContainer = compose(
    getContext({
        getFromConfig: PropTypes.func,
    }),
    withRouter,
    mapProps(({ getFromConfig, ...rest }) => {
        let configProps = {}

        if (getFromConfig) {
            configProps = getFromConfig('menu')
        }

        const { header, sidebars = [] } = configProps
        const {
            location: { pathname },
        } = rest

        const sidebar = getMatchingSidebar(sidebars, pathname) || {}

        const { path } = sidebar

        return {
            ...rest,
            ...configProps,
            sidebar: { ...sidebar, id: `Sidebar:${path}` },
            headerItems: get(header, 'menu.items') || [],
            headerExtraItems: get(header, 'extraMenu.items') || [],
            sidebarItems: get(sidebar, 'menu.items') || [],
            sidebarExtraItems: get(sidebar, 'extraMenu.items') || [],
        }
    }),
)(MenuContainer)

export default ConfigContainer
