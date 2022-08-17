import React from 'react'
import PropTypes from 'prop-types'

import { resolveItems } from './utils'

export const withItemsResolver = (Component) => {
    const WithItemsResolver = (props) => {
        const { menu, extraMenu, datasources, datasource, models } = props

        if (!datasources || !datasource) {
            return <Component {...props} />
        }

        const { items = [] } = menu
        const { items: extraItems = [] } = extraMenu

        const resolvedItems = resolveItems(items, models)
        const resolvedExtraItems = resolveItems(extraItems, models)

        return (
            <Component
                {...props}
                menu={{ ...menu, items: resolvedItems }}
                extraMenu={{ ...extraMenu, items: resolvedExtraItems }}
            />
        )
    }

    WithItemsResolver.propTypes = {
        datasources: PropTypes.object,
        datasource: PropTypes.string,
        menu: PropTypes.object,
        fetchData: PropTypes.func,
        models: PropTypes.object,
        extraMenu: PropTypes.object,
    }

    return WithItemsResolver
}
