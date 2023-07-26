import React from 'react'
import PropTypes from 'prop-types'

import { useOnActionMethod } from '../hooks/useOnActionMethod'

export const withWidgetHandlers = (WrappedComponent) => {
    const WithHandlers = (props) => {
        const { rowClick, datasource } = props
        const onRowClickAction = useOnActionMethod(datasource, rowClick)

        return (
            <WrappedComponent {...props} onRowClickAction={onRowClickAction} />
        )
    }

    WithHandlers.propTypes = {
        rowClick: PropTypes.object,
        datasource: PropTypes.string,
    }

    return WithHandlers
}
