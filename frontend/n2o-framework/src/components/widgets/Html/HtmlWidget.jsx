import React from 'react'
import { useSelector } from 'react-redux'

import StandardWidget from '../StandardWidget'
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import { Html } from './Html'

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

function Widget({
    id,
    toolbar,
    className,
    style,
    url,
    html,
    loading,
    datasource,
}) {
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source))?.[0]

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
                data={datasourceModel}
            />
        </StandardWidget>
    )
}

/**
 * @type ConnectedWidget
 */
export const HtmlWidget = WidgetHOC(Widget)
