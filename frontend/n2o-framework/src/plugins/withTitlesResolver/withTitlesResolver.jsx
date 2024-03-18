import React from 'react'
import PropTypes from 'prop-types'

import { useResolved } from '../../core/Expression/useResolver'

export const withTitlesResolver = (Component) => {
    const WithTitlesResolver = (props) => {
        const { logo, subtitle, datasourceModel } = props

        const { title } = logo
        const { title: resolvedTitle, subtitle: resolvedSubtitle } = useResolved({ title, subtitle }, datasourceModel)

        return (
            <Component
                {...props}
                logo={{ ...logo, title: resolvedTitle }}
                subtitle={resolvedSubtitle}
            />
        )
    }

    WithTitlesResolver.propTypes = {
        logo: PropTypes.object,
        subtitle: PropTypes.string,
        datasourceModel: PropTypes.object,
    }

    WithTitlesResolver.defaultProps = {
        logo: {},
    }

    return WithTitlesResolver
}
