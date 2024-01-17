import React from 'react'
import PropTypes from 'prop-types'
import { useSelector } from 'react-redux'

import { getModelSelector } from '../../ducks/models/selectors'
import { ModelPrefix } from '../../core/datasource/const'

import { resolveItems } from './utils'

export const withItemsResolver = (Component) => {
    const WithItemsResolver = (props) => {
        const { menu, extraMenu, datasources, datasource } = props

        const datasourceModels = useSelector(getModelSelector(`models.${ModelPrefix.source}`))
        const datasourceModel = datasourceModels[datasource]?.[0] || {}

        if (!datasources || !datasource) { return <Component {...props} extraMenu={{ items: extraMenu }} /> }

        const { items = [] } = menu

        const resolvedItems = resolveItems(items, datasourceModels, datasource)
        const resolvedExtraItems = resolveItems(extraMenu, datasourceModels, datasource)

        return (
            <Component
                {...props}
                menu={{ items: resolvedItems }}
                extraMenu={{ items: resolvedExtraItems }}
                datasourceModel={datasourceModel}
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
