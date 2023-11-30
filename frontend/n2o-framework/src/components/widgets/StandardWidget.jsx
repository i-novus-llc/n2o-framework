import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { pure, compose } from 'recompose'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import { isEmpty } from 'lodash'

import Toolbar from '../buttons/Toolbar'
import { Spinner } from '../snippets/Spinner/Spinner'
import { dataSourceError } from '../../ducks/datasource/selectors'
import { ErrorContainer } from '../../core/error/Container'

import WidgetFilters from './WidgetFilters'

const PLACES = {
    top: 'top',
    left: 'left',
    right: 'right',
    center: 'center',
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
        const { widgetId, toolbar, filter, fetchData, datasource } = this.props

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
                        fetchData={fetchData}
                        datasource={datasource}
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
                const { [place]: propsPlace } = this.props
                const hasPropsPlace = propsPlace && React.isValidElement(propsPlace)
                const toolbarClassNames = classNames(
                    'd-flex flex-column',
                    {
                        'flex-column': place.includes(PLACES.bottom),
                        'flex-column-reverse': place.includes(PLACES.top),
                        'align-items-center': place.toLowerCase().includes(PLACES.center),
                        'align-items-end': place.toLowerCase().includes(PLACES.right),
                    },
                )
                const currentToolbar = toolbar[place]

                return (
                    <div className={toolbarClassNames}>
                        {hasPropsPlace && (
                            <div className={`m${place.includes(PLACES.top) ? 't' : 'b'}-2`}>
                                {propsPlace}
                            </div>
                        )}
                        <Toolbar toolbar={currentToolbar} entityKey={widgetId} />
                    </div>
                )
            }
            default:
                break
        }
    }

    render() {
        const {
            disabled,
            filter,
            className,
            style,
            children,
            loading,
            error,
        } = this.props

        const classes = classNames([
            className,
            'n2o-standard-widget-layout',
            { 'n2o-disabled': disabled },
        ])

        const errorComponent = isEmpty(error) ? null : <ErrorContainer error={error} />

        const childrenWithProps = React.Children.map(children, (child) => {
            if (React.isValidElement(child)) {
                return React.cloneElement(child, { errorComponent })
            }

            return child
        })

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
                            {childrenWithProps}
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
    datasource: PropTypes.string,
    toolbar: PropTypes.object,
    filter: PropTypes.object,
    filterModel: PropTypes.object,
    error: PropTypes.object,
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

const mapStateToProps = createStructuredSelector({
    error: (state, { datasource }) => {
        if (state.datasource[datasource]) {
            return dataSourceError(datasource)(state)
        }

        return null
    },
})

export default compose(
    pure,
    connect(mapStateToProps),
)(StandardWidget)
