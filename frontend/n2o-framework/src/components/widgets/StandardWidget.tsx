import React, { Children, useCallback, useMemo, ReactNode, CSSProperties, memo } from 'react'
import classNames from 'classnames'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import { isEmpty } from 'lodash'
import { SpinnerType } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'

import Toolbar, { ToolbarProps } from '../buttons/Toolbar'
import { Spinner } from '../snippets/Spinner/Spinner'
import { dataSourceError } from '../../ducks/datasource/selectors'
import { ErrorContainer } from '../../core/error/Container'
import { State } from '../../ducks/State'
import { type ErrorContainerProps } from '../../core/error/types'
import { Model } from '../../ducks/models/selectors'

import { WidgetFilters, type Props as WidgetFiltersProps } from './WidgetFilters'

export enum PLACES {
    top = 'top',
    left = 'left',
    right = 'right',
    center = 'center',
    topLeft = 'topLeft',
    topRight = 'topRight',
    topCenter = 'topCenter',
    bottomLeft = 'bottomLeft',
    bottomRight = 'bottomRight',
    bottomCenter = 'bottomCenter',
}

export interface Props {
    widgetId: string
    toolbar: Record<string, ToolbarProps>
    filter?: { filterPlace: PLACES, filterFieldsets: WidgetFiltersProps['fieldsets'] }
    fetchData?: WidgetFiltersProps['fetchData']
    datasource: string
    pagination?: Record<string, unknown>
    disabled?: boolean
    className?: string
    style?: CSSProperties
    children: ReactNode
    loading: boolean
    error: ErrorContainerProps['error']
    activeModel?: Model | Model[]
    showCount?: boolean
}

/**
 * Виджет таблица
 * @reactProps {string} widgetId - id виджета
 * @reactProps {Object} toolbar
 * @reactProps {Object} filter
 * @reactProps {boolean} disabled - флаг активности
 * @reactProps {node} children - элемент потомок компонента StandardWidget
 */
const StandardWidget = memo(({
    widgetId,
    fetchData,
    datasource,
    disabled,
    className,
    style,
    children,
    loading,
    error,
    filter = {} as never,
    toolbar = {},
    pagination = {},
}: Props) => {
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

    const filterComponent = (
        <WidgetFilters
            widgetId={widgetId}
            fetchData={fetchData}
            datasource={datasource}
            fieldsets={filter.filterFieldsets}
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
            return React.cloneElement(child, { errorComponent } as never)
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
                    <Spinner loading={loading} type={SpinnerType.cover}>
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
})

const mapStateToProps = createStructuredSelector({
    // @ts-ignore TODO объеденить типы ErrorContainerProps['error'] и dataSourceError, убрать as never
    error: (state: State, { datasource }: Props) => {
        if (state.datasource[datasource]) {
            return dataSourceError(datasource)(state)
        }

        return null
    },
})

// @ts-ignore TODO объеденить типы ErrorContainerProps['error'] и dataSourceError, убрать as never
export default connect(mapStateToProps)(StandardWidget)
