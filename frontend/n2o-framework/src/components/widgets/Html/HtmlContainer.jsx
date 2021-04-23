import React from 'react'

import widgetContainer from '../WidgetContainer'
import { HTML } from '../widgetTypes'

import Html from './Html'
import 'whatwg-fetch'

function HtmlContainer(props) {
    return <Html {...props} />
}

export default widgetContainer(
    {
        mapProps: props => ({
            ...props,
            data: props.datasource && props.datasource[0],
            loading: props.isLoading,
        }),
    },
    HTML,
)(HtmlContainer)
