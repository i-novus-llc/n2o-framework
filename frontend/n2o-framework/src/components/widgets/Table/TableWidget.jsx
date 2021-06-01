import React from 'react'
import PropTypes from 'prop-types'
import values from 'lodash/values'

import StandardWidget from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
// eslint-disable-next-line import/no-named-as-default
import dependency from '../../../core/dependency'

import N2OPagination from './N2OPagination'
import TableContainer from './TableContainer'

/**
 * Виджет таблица
 * @reactProps {string} containerId - id контейнера
 * @reactProps {string} pageId - id страницы
 * @reactProps {string} widgetId - id виджета
 * @reactProps {object} actions
 * @reactProps {object} tools
 * @reactProps {object} dataProvider
 * @reactProps {object} table
 * @reactProps {number} table.size
 * @reactProps {boolean} table.fetchOnInit - фетчить / не фетчить данные при инициализации
 * @reactProps {boolean} table.hasSelect
 * @reactProps {string} table.className - css класс
 * @reactProps {object} table.style - css стили
 * @reactProps {boolean} table.autoFocus
 * @reactProps {object} table.sorting
 * @reactProps {array} table.headers
 * @reactProps {array} table.cells
 */
class TableWidget extends React.Component {
    /**
   * Замена src на компонент
   */
    getWidgetProps() {
        const { toolbar, actions, dataProvider, table } = this.props
        const { resolveProps } = this.context
        const {
            headers,
            cells,
            sorting,
            hasFocus,
            hasSelect,
            autoFocus,
            rowColor,
            rowClick,
        } = table

        return {
            headers: values(resolveProps(headers)),
            cells: values(resolveProps(cells)),
            sorting,
            toolbar,
            rowColor,
            actions,
            hasFocus,
            hasSelect,
            autoFocus,
            dataProvider,
            rowClick,
        }
    }

    prepareFilters() {
        const { resolveProps } = this.context
        const { filter } = this.props

        return resolveProps(
            filter,
            Fieldsets.StandardFieldset,
        )
    }

    render() {
        const {
            id: widgetId,
            toolbar,
            disabled,
            actions,
            table: { fetchOnInit, size },
            pageId,
            paging,
            className,
            style,
        } = this.props

        return (
            <StandardWidget
                disabled={disabled}
                widgetId={widgetId}
                toolbar={toolbar}
                actions={actions}
                filter={this.prepareFilters()}
                bottomLeft={
                    paging && <N2OPagination widgetId={widgetId} {...paging} />
                }
                className={className}
                style={style}
            >
                <TableContainer
                    widgetId={widgetId}
                    pageId={pageId}
                    size={size}
                    page={1}
                    fetchOnInit={fetchOnInit}
                    {...this.getWidgetProps()}
                />
            </StandardWidget>
        )
    }
}

TableWidget.defaultProps = {
    toolbar: {},
    filter: {},
}

TableWidget.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    containerId: PropTypes.string.isRequired,
    pageId: PropTypes.string.isRequired,
    widgetId: PropTypes.string,
    actions: PropTypes.object,
    toolbar: PropTypes.object,
    dataProvider: PropTypes.object,
    table: PropTypes.arrayOf(
        PropTypes.shape({
            size: PropTypes.number,
            fetchOnInit: PropTypes.bool,
            hasSelect: PropTypes.bool,
            className: PropTypes.string,
            style: PropTypes.object,
            autoFocus: PropTypes.bool,
            sorting: PropTypes.object,
            headers: PropTypes.array,
            cells: PropTypes.array,
        }),
    ),
    paging: PropTypes.oneOfType([PropTypes.bool, PropTypes.object]),
    filter: PropTypes.object,
    id: PropTypes.string,
    disabled: PropTypes.bool,
}

TableWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

export default dependency(TableWidget)
