import React from 'react'

import { useResolved } from '../../core/Expression/useResolver'

export const withTitlesResolver = (Component) => {
    return (props) => {
        const { logo = {}, subtitle, datasourceModel } = props

        const { title } = logo
        const { title: resolvedTitle, subtitle: resolvedSubtitle } = useResolved({ title, subtitle }, datasourceModel)

        return <Component {...props} logo={{ ...logo, title: resolvedTitle }} subtitle={resolvedSubtitle} />
    }
}
