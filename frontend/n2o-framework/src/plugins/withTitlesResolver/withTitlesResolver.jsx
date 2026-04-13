import React from 'react'
import { useSelector } from 'react-redux'

import { useResolved } from '../../core/Expression/useResolver'
import { ModelPrefix } from '../../core/models/types'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'

export const withTitlesResolver = (Component) => {
    return (props) => {
        const { logo = {}, subtitle, model: prefix = ModelPrefix.active, datasource } = props
        const model = useSelector(getModelByPrefixAndNameSelector(prefix, datasource))

        const { title } = logo
        const { title: resolvedTitle, subtitle: resolvedSubtitle } = useResolved({ title, subtitle }, model)

        return <Component {...props} logo={{ ...logo, title: resolvedTitle }} subtitle={resolvedSubtitle} />
    }
}
