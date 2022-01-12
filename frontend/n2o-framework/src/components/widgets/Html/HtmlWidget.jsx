import React from 'react'
import PropTypes from 'prop-types'

import StandardWidget from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/Widget'
import { widgetPropTypes } from '../../../core/widget/propTypes'

import Html from './Html'
import 'whatwg-fetch'

/**
 * HtmlWidget
 * @reactProps {string} containerId - id конейтенера
 * @reactProps {string} pageId - id страницы
 * @reactProps {boolean} url - url для фетчинга
 * @reactProps {string} widgetId - id виджета
 * @reactProps {string} html - html код
 * @reactProps {object} dataProvider
 * @reactProps {object} datasource
 */

function Widget(props) {
    const {
        id,
        toolbar,
        className,
        style,
        url,
        html,
        models,
        loading,
    } = props

    return (
        <StandardWidget
            widgetId={id}
            toolbar={toolbar}
            className={className}
            style={style}
            loading={loading}
        >
            <Html
                url={url}
                id={id}
                html={html}
                data={models.datasource?.[0]}
            />
        </StandardWidget>
    )
}

Widget.propTypes = {
    ...widgetPropTypes,
    url: PropTypes.bool,
    html: PropTypes.string,
}

export const HtmlWidget = WidgetHOC(Widget)
