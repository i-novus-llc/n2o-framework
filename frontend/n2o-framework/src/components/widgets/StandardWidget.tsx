import React, {
    Children,
    useCallback,
    useMemo,
    ReactNode,
    CSSProperties,
    memo,
    isValidElement,
    cloneElement,
    useContext, ComponentType,
} from 'react'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import { useSelector } from 'react-redux'

import { Toolbar, type ToolbarProps } from '../buttons/Toolbar'
import { dataSourceError } from '../../ducks/datasource/selectors'
import { ErrorContainer } from '../../core/error/Container'
import { type ErrorContainerError } from '../../core/error/types'
import { type Model } from '../../ducks/models/selectors'
import { type Widget } from '../../ducks/widgets/Widgets'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'
import { FactoryContext } from '../../core/factory/context'
import { FactoryLevels } from '../../core/factory/factoryLevels'

import { type WidgetLayoutProps, PLACES } from './WidgetLayout/WidgetLayout'
import { WidgetFilters, type Props as WidgetFiltersProps } from './WidgetFilters'

export type WidgetFilter = { filterPlace: PLACES, filterFieldsets: WidgetFiltersProps['fieldsets'] }

export interface Props extends Widget {
    widgetId: string
    toolbar: Record<string, ToolbarProps>
    filter?: WidgetFilter
    fetchData?: WidgetFiltersProps['fetchData']
    datasource: string
    pagination?: Record<string, unknown>
    disabled?: boolean
    className?: string
    style?: CSSProperties
    children?: ReactNode
    loading: boolean
    error?: ErrorContainerError
    activeModel?: Model | Model[]
    showCount?: boolean
    stickyFooter?: boolean
    scrollbar?: ReactNode
}

/**
 * Обертка над виджетами, передает компоненты и параметры в WidgetLayout
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
    filter = EMPTY_OBJECT as WidgetFilter,
    toolbar = EMPTY_OBJECT,
    pagination = EMPTY_OBJECT,
    stickyFooter = false,
    scrollbar,
}: Props) => {
    const { getComponent } = useContext(FactoryContext)
    const FactoryWidgetLayout = getComponent('WidgetLayout', FactoryLevels.WIDGETS) as ComponentType<WidgetLayoutProps>

    const error = useSelector(dataSourceError(datasource))
    const renderToolbar = useCallback((place: PLACES) => {
        const { [place]: placePagination } = pagination
        const { [place]: placeToolbar } = toolbar

        if (!placePagination && !placeToolbar) { return null }

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
            <div className={toolbarClassNames} key={place}>
                {placePagination}
                {placeToolbar && <Toolbar toolbar={placeToolbar} entityKey={widgetId} />}
            </div>
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

    const classes = classNames([className, 'n2o-standard-widget-layout', { 'n2o-disabled': disabled }])

    const errorComponent = useMemo(() => (
        isEmpty(error) ? null : <ErrorContainer error={error} />
    ), [error])

    const childrenWithError = Children.map(children, (child) => {
        if (isValidElement<Record<string, unknown>>(child)) {
            return cloneElement(child, { errorComponent })
        }

        return child
    })

    return (
        <FactoryWidgetLayout
            className={classes}
            style={style}
            filterComponent={filterComponent}
            filterPlace={filter.filterPlace}
            topToolbars={topToolbars}
            bottomToolbars={bottomToolbars}
            scrollbar={scrollbar}
            stickyFooter={stickyFooter}
            loading={loading}
        >
            {childrenWithError}
        </FactoryWidgetLayout>
    )
})

export default StandardWidget
