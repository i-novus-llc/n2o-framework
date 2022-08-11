import React from 'react'
import PropTypes from 'prop-types'
import { getContext, compose, mapProps } from 'recompose'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'
import { withRouter } from 'react-router-dom'

import withSecurity from '../../core/auth/withSecurity'

import { getMatchingSidebar } from './helpers'

const initialItems = {
    headerItems: [],
    headerExtraItems: [],
    sidebarItems: [],
    sidebarExtraItems: [],
}

export class MenuContainer extends React.Component {
    state = {
        ...initialItems,
    };

    async componentDidMount() {
        await this.getItemsWithAccess()
    }

    async componentDidUpdate(prevProps) {
        const { user, headerItems, headerExtraItems, sidebarItems, sidebarExtraItems } = this.props

        const notEqualHeaderItems = !isEqual(headerItems, prevProps.headerItems)
        const notEqualHeaderExtraItems = !isEqual(headerExtraItems, prevProps.headerExtraItems)
        const notEqualSidebarItems = !isEqual(sidebarItems, prevProps.sidebarItems)
        const notEqualSidebarExtraItems = !isEqual(sidebarExtraItems, prevProps.sidebarExtraItems)
        const notEqualUser = !isEqual(user, prevProps.user)

        if (
            notEqualHeaderItems ||
            notEqualHeaderExtraItems ||
            notEqualSidebarItems ||
            notEqualSidebarExtraItems ||
            notEqualUser
        ) {
            await this.getItemsWithAccess()
        }
    }

    makeSecure = async (items) => {
        const resolvedItems = []

        if (Array.isArray(items) && !isEmpty(items)) {
            for (const item of items) {
                const resolvedItem = await this.checkItem(item)

                if (resolvedItem) {
                    resolvedItems.push(resolvedItem)
                }
            }
        }

        return resolvedItems
    };

    checkItem = async (item) => {
        const { checkSecurity } = this.props
        const { security, items } = item

        // проверка на секьюру родителя, если не прошел дальше нет смысла идти
        if (!isEmpty(security)) {
            try {
                await checkSecurity(security)
            } catch (err) {
                return null
            }
        }

        if (!isEmpty(items)) {
            return {
                ...item,
                items: await this.makeSecure(items),
            }
        }

        return item
    };

    getItemsWithAccess = async () => {
        const { headerItems, headerExtraItems, sidebarItems, sidebarExtraItems } = this.props

        this.setState({
            headerItems: await this.makeSecure(headerItems),
            headerExtraItems: await this.makeSecure(headerExtraItems),
            sidebarItems: await this.makeSecure(sidebarItems),
            sidebarExtraItems: await this.makeSecure(sidebarExtraItems),
        })
    };

    mapRenderProps = () => {
        const { headerItems, headerExtraItems, sidebarItems, sidebarExtraItems } = this.state
        const { header, sidebar, location, datasources = {} } = this.props

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
    };

    render() {
        const { render } = this.props

        return render(this.mapRenderProps())
    }
}

MenuContainer.propTypes = {
    render: PropTypes.func,
    user: PropTypes.any,
    checkSecurity: PropTypes.func,
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
    withSecurity,
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
