import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { pure } from 'recompose'

import Toolbar from '../buttons/Toolbar'

// eslint-disable-next-line import/no-named-as-default
import WidgetAlerts from './WidgetAlerts'
import WidgetFilters from './WidgetFilters'

/**
 * Виджет таблица
 * @reactProps {string} widgetId - id виджета
 * @reactProps {Object} toolbar
 * @reactProps {Object} actions
 * @reactProps {Object} filter
 * @reactProps {boolean} disabled - флаг активности
 * @reactProps {node} children - элемент потомок компонента StandardWidget
 */
class StandardWidget extends React.Component {
    // eslint-disable-next-line consistent-return
    renderSection(place) {
        const { widgetId, toolbar, filter, modelId } = this.props
        const { [place]: propsPlace } = this.props

        if (propsPlace && React.isValidElement(propsPlace)) {
            return propsPlace
        }
        const filterProps = {
            ...filter,
            fieldsets: filter.filterFieldsets,
        }

        switch (place) {
            case 'left':
                return <WidgetFilters widgetId={widgetId} modelId={modelId} {...filterProps} />
            case 'top':
                return <WidgetFilters widgetId={widgetId} modelId={modelId} {...filterProps} />
            case 'topLeft':
                return <Toolbar toolbar={toolbar.topLeft} entityKey={widgetId} />
            case 'topRight':
                return <Toolbar toolbar={toolbar.topRight} entityKey={widgetId} />
            case 'topCenter':
                return <Toolbar toolbar={toolbar.topCenter} entityKey={widgetId} />
            case 'bottomLeft':
                return <Toolbar toolbar={toolbar.bottomLeft} entityKey={widgetId} />
            case 'bottomRight':
                return <Toolbar toolbar={toolbar.bottomRight} entityKey={widgetId} />
            case 'bottomCenter':
                return <Toolbar toolbar={toolbar.bottomCenter} entityKey={widgetId} />
            case 'right':
                return <WidgetFilters widgetId={widgetId} modelId={modelId} {...filterProps} />
            default:
                break
        }
    }

    render() {
        const { widgetId, disabled, filter, className, style, children } = this.props

        const classes = classNames([
            className,
            'n2o-standard-widget-layout',
            { 'n2o-disabled': disabled },
        ])

        return (
            <div className={classes} style={style}>
                {filter.filterPlace === 'left' && this.renderSection('left')}
                <div className="n2o-standard-widget-layout-center">
                    <div>
                        {filter.filterPlace === 'top' && this.renderSection('top')}
                        <WidgetAlerts widgetId={widgetId} />
                    </div>
                    <div className="d-flex justify-content-between">
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--left">
                            {this.renderSection('topLeft')}
                        </div>
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--center">
                            {this.renderSection('topCenter')}
                        </div>
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--right">
                            {this.renderSection('topRight')}
                        </div>
                    </div>
                    <div>
                        <div />
                        <div>{children}</div>
                        <div />
                    </div>
                    <div className="d-flex justify-content-between">
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--left">
                            {this.renderSection('bottomLeft')}
                        </div>
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--center">
                            {this.renderSection('bottomCenter')}
                        </div>
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--right">
                            {this.renderSection('bottomRight')}
                        </div>
                    </div>
                    <div>
                        <div />
                    </div>
                </div>
                <div className="n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--right">
                    {filter.filterPlace === 'right' && this.renderSection('right')}
                </div>
            </div>
        )
    }
}

StandardWidget.defaultProps = {
    toolbar: {},
    filter: {},
}

StandardWidget.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    widgetId: PropTypes.string,
    modelId: PropTypes.string,
    toolbar: PropTypes.object,
    actions: PropTypes.object,
    filter: PropTypes.object,
    disabled: PropTypes.bool,
    left: PropTypes.element,
    top: PropTypes.element,
    topLeft: PropTypes.oneOfType([PropTypes.bool, PropTypes.array, PropTypes.node]),
    topCenter: PropTypes.oneOfType([PropTypes.bool, PropTypes.array, PropTypes.node]),
    topRight: PropTypes.oneOfType([PropTypes.bool, PropTypes.array, PropTypes.node]),
    bottomLeft: PropTypes.oneOfType([
        PropTypes.bool,
        PropTypes.array,
        PropTypes.node,
    ]),
    bottomCenter: PropTypes.oneOfType([
        PropTypes.bool,
        PropTypes.array,
        PropTypes.node,
    ]),
    bottomRight: PropTypes.oneOfType([
        PropTypes.bool,
        PropTypes.array,
        PropTypes.node,
    ]),
    children: PropTypes.node,
}

export default pure(StandardWidget)
