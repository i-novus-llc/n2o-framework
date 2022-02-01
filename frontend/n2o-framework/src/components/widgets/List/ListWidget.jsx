import React, { useContext, useMemo } from 'react'
import PropTypes from 'prop-types'

// eslint-disable-next-line import/no-named-as-default
import { WidgetHOC } from '../../../core/widget/WidgetHOC'
import { widgetPropTypes } from '../../../core/widget/propTypes'
import { FactoryContext } from '../../../core/factory/context'
import WidgetLayout from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import { N2OPagination } from '../Table/N2OPagination'

import ListContainer from './ListContainer'

/**
 * Виджет ListWidget
 * @constructor
 */
function ListWidget(props) {
    const {
        id: widgetId,
        datasource,
        toolbar,
        disabled,
        paging,
        className,
        style,
        filter,
        list,
        placeholder,
        rowClick,
        hasMoreButton,
        maxHeight,
        fetchOnScroll,
        divider,
        hasSelect,
        rows,
        size,
        count,
        models,
        page,
        setPage,
        loading,
    } = props
    const { place = 'bottomLeft' } = paging
    const pagination = {
        [place]: (
            <N2OPagination
                {...paging}
                size={size}
                count={count}
                activePage={page}
                datasource={models.datasource}
                setPage={setPage}
            />
        ),
    }
    const { resolveProps } = useContext(FactoryContext)
    const resolvedFilter = useMemo(() => resolveProps(filter, Fieldsets.StandardFieldset), [filter, resolveProps])
    const resolvedList = useMemo(() => resolveProps(list), [list, resolveProps])

    return (
        <WidgetLayout
            disabled={disabled}
            widgetId={widgetId}
            datasource={datasource}
            toolbar={toolbar}
            filter={resolvedFilter}
            {...pagination}
            className={className}
            style={style}
            loading={loading}
        >
            <ListContainer
                {...props}
                maxHeight={maxHeight}
                hasMoreButton={hasMoreButton}
                list={resolvedList}
                rowClick={rowClick}
                fetchOnScroll={fetchOnScroll}
                deferredSpinnerStart={0}
                divider={divider}
                hasSelect={hasSelect}
                placeholder={placeholder}
                rows={rows}
            />
        </WidgetLayout>
    )
}

ListWidget.propTypes = {
    ...widgetPropTypes,
    list: PropTypes.object,
    fetchOnScroll: PropTypes.bool,
    rowClick: PropTypes.func,
    hasMoreButton: PropTypes.bool,
    maxHeight: PropTypes.number,
    prevText: PropTypes.string,
    nextText: PropTypes.string,
    hasSelect: PropTypes.bool,
    placeholder: PropTypes.object,
    divider: PropTypes.bool,
    rows: PropTypes.bool,
}
ListWidget.defaultProps = {
    rowClick: null,
    hasMoreButton: false,
    list: {},
    fetchOnScroll: false,
    hasSelect: false,
}

export default WidgetHOC(ListWidget)
