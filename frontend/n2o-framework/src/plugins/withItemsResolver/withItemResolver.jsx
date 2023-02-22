import React from 'react'
import PropTypes from 'prop-types'
import { useSelector } from 'react-redux'

import { dataSourceModelByPrefixSelector } from '../../ducks/datasource/selectors'
import { ModelPrefix } from '../../core/datasource/const'

import { resolveItems } from './utils'

export const withItemsResolver = (Component) => {
    const WithItemsResolver = (props) => {
        const { menu, extraMenu, datasources, datasource } = props
        const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source))?.[0] || {}

        if (!datasources || !datasource) {
            return <Component {...props} />
        }

        const { items = [] } = menu
        const { items: extraItems = [] } = extraMenu

        const resolvedItems = resolveItems(items, datasourceModel)
        const resolvedExtraItems = resolveItems(extraItems, datasourceModel)

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
