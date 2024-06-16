import React, { Children, useCallback, useMemo } from 'react'
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
import { COUNT_NEVER } from '../snippets/Pagination/constants'

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
const StandardWidget = (props) => {
    const {
        widgetId, toolbar, filter,
        fetchData, datasource, pagination,
        disabled, className, style,
        children, loading, error,
    } = props
    const renderToolbar = useCallback((place) => {
        const paginationComponent = pagination[place]
        const currentToolbar = toolbar[place]
        const toolbarClassNames = classNames(
            'd-flex',
            'flex-column',
            'n2o-standard-widget-layout-toolbar-wrapper',
            `n2o-standard-widget-layout-toolbar--${place.toLowerCase().replace(/(bottom)|(top)/, '')}`,
            {
                'flex-column-reverse': place.includes(PLACES.top),
            },
        )

        return (
            currentToolbar || paginationComponent ? (
                <div className={toolbarClassNames} key={place}>
                    {paginationComponent}
                    {currentToolbar ? <Toolbar toolbar={currentToolbar} entityKey={widgetId} /> : null}
                </div>
            ) : null
        )
    }, [pagination, toolbar, widgetId])
    const { showCount } = props

    const withCount = showCount !== COUNT_NEVER

    const filterComponent = (
        <WidgetFilters
            widgetId={widgetId}
            fetchData={fetchData}
            datasource={datasource}
            fieldsets={filter.filterFieldsets}
            withCount={withCount}
            {...filter}
        />
    )

    const { topToolbars, bottomToolbars } = useMemo(() => ({
        topToolbars: [
            renderToolbar(PLACES.topLeft),
            renderToolbar(PLACES.topCenter),
            renderToolbar(PLACES.topRight),
        ].filter(Boolean),
        bottomToolbars: [
            renderToolbar(PLACES.bottomLeft),
            renderToolbar(PLACES.bottomCenter),
            renderToolbar(PLACES.bottomRight),
        ].filter(Boolean),
    }), [renderToolbar])

    const classes = classNames([
        className,
        'n2o-standard-widget-layout',
        { 'n2o-disabled': disabled },
    ])

    const errorComponent = isEmpty(error) ? null : <ErrorContainer error={error} />

    const childrenWithError = Children.map(children, (child) => {
        if (React.isValidElement(child)) {
            return React.cloneElement(child, { errorComponent })
        }

        return child
    })

    return (
        <div className={classes} style={style}>
            {filter.filterPlace === PLACES.left ? (
                <div className="n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--left">
                    {filterComponent}
                </div>
            ) : null}
            <div className="n2o-standard-widget-layout-center">
                {filter.filterPlace === PLACES.top ? (
                    <div className="n2o-standard-widget-layout-center-filter">
                        {filterComponent}
                    </div>
                ) : null}
                {topToolbars.length ? (
                    <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar-top">
                        {topToolbars}
                    </div>
                ) : null}
                <div className="n2o-standard-widget-layout-content">
                    <Spinner loading={loading} type="cover">
                        {childrenWithError}
                    </Spinner>
                </div>
                {bottomToolbars.length ? (
                    <div className="n2o-standard-widget-layout-toolbar n2o-standard-widget-layout-toolbar-bottom">
                        {bottomToolbars}
                    </div>
                ) : null}
            </div>
            {filter.filterPlace === PLACES.right ? (
                <div className="n2o-standard-widget-layout-aside n2o-standard-widget-layout-aside--right">
                    {filterComponent}
                </div>
            ) : null}
        </div>
    )
}

StandardWidget.defaultProps = {
    toolbar: {},
    filter: {},
    pagination: {},
}

StandardWidget.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    widgetId: PropTypes.string,
    datasource: PropTypes.string,
    toolbar: PropTypes.object,
    filter: PropTypes.object,
    error: PropTypes.object,
    fetchData: PropTypes.func,
    disabled: PropTypes.bool,
    children: PropTypes.node,
    loading: PropTypes.bool,
    pagination: PropTypes.object,
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
