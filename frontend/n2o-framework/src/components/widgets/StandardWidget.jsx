import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { pure } from 'recompose'

import Toolbar from '../buttons/Toolbar'
import { Spinner } from '../snippets/Spinner/Spinner'

import WidgetFilters from './WidgetFilters'

const PLACES = {
    top: 'top',
    left: 'left',
    right: 'right',
    topLeft: 'topLeft',
    topRight: 'topRight',
    topCenter: 'topCenter',
    bottomLeft: 'bottomLeft',
    bottomRight: 'bottomRight',
    bottomCenter: 'bottomCenter',
}

/**
 * Виджет таблица
 * @reactProps {string} widgetId - id виджета
 * @reactProps {Object} toolbar
 * @reactProps {Object} filter
 * @reactProps {boolean} disabled - флаг активности
 * @reactProps {node} children - элемент потомок компонента StandardWidget
 */
class StandardWidget extends React.Component {
    // eslint-disable-next-line consistent-return
    renderSection(place) {
        const { widgetId, toolbar, filter, setFilter, filterModel, fetchData } = this.props
        const { [place]: propsPlace } = this.props

        if (propsPlace && React.isValidElement(propsPlace)) {
            return propsPlace
        }
        const filterProps = {
            ...filter,
            fieldsets: filter.filterFieldsets,
        }

        switch (place) {
            case PLACES.top:
            case PLACES.left:
            case PLACES.right: {
                return (
                    <WidgetFilters
                        widgetId={widgetId}
                        setFilter={setFilter}
                        fetchData={fetchData}
                        filterModel={filterModel}
                        {...filterProps}
                    />
                )
            }
            case PLACES.topLeft:
            case PLACES.topRight:
            case PLACES.topCenter:
            case PLACES.bottomLeft:
            case PLACES.bottomRight:
            case PLACES.bottomCenter: {
                return <Toolbar toolbar={toolbar[place]} entityKey={widgetId} />
            }
            default:
                break
        }
    }

    render() {
        const { disabled, filter, className, style, children, loading } = this.props

        const classes = classNames([
            className,
            'n2o-standard-widget-layout',
            { 'n2o-disabled': disabled },
        ])

        return (
            <div className={classes} style={style}>
                {filter.filterPlace === PLACES.left && this.renderSection(PLACES.left)}
                <div className="n2o-standard-widget-layout-center">
                    <div>
                        {filter.filterPlace === PLACES.top && this.renderSection(PLACES.top)}
                    </div>
                    <div className="d-flex justify-content-between">
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--left">
                            {this.renderSection(PLACES.topLeft)}
                        </div>
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--center">
                            {this.renderSection(PLACES.topCenter)}
                        </div>
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--right">
                            {this.renderSection(PLACES.topRight)}
                        </div>
                    </div>
                    <div>
                        <Spinner loading={loading} type="cover">
                            {children}
                        </Spinner>

                    </div>
                    <div className="d-flex justify-content-between">
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--left">
                            {this.renderSection(PLACES.bottomLeft)}
                        </div>
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--center">
                            {this.renderSection(PLACES.bottomCenter)}
                        </div>
                        <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar--right">
                            {this.renderSection(PLACES.bottomRight)}
                        </div>
                    </div>
                    <div>
                        <div />
                    </div>
                </div>
                <div className="n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--right">
                    {filter.filterPlace === PLACES.right && this.renderSection(PLACES.right)}
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
    toolbar: PropTypes.object,
    filter: PropTypes.object,
    filterModel: PropTypes.object,
    setFilter: PropTypes.func,
    fetchData: PropTypes.func,
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
    loading: PropTypes.bool,
}

export default pure(StandardWidget)
