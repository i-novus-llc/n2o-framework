import React, { Component } from 'react'
import PropTypes from 'prop-types'
import values from 'lodash/values'

import { getN2OPagination } from '../Table/N2OPagination'
import { pagingType } from '../../snippets/Pagination/types'
import StandardWidget from '../StandardWidget'
import { StandardFieldset } from '../Form/fieldsets'
import { dependency } from '../../../core/dependency'

// eslint-disable-next-line import/no-named-as-default
import AdvancedTableContainer from './AdvancedTableContainer'

/**
 * Компонент AdvancedTableWidget
 * @param pageId - id страницы
 * @param widgetId - id виджета
 * @param actions - экшены
 * @param toolbar - тулбар таблицы
 * @param dataProvider
 * @param bordered - флаг таблицы с боредарми
 * @param rowClick - действие клика по строке
 * @param paging - объект пагинации
 * @param multiHeader - флаг использования многоуровненвого заголовка
 * @param scroll - объект скролла для таблицы
 * @param expandable - флаг использования контента в подстроке
 * @param useFixedHeader - флаг использования фиксированного заголовка
 * @param tableSize - размер таблицы
 * @param rowSelection - флаг использования чекбоксов для мульти селекта
 * @param rowClass - expression цвет строки
 */
class AdvancedTableWidget extends Component {
    getWidgetProps() {
        const {
            toolbar,
            actions,
            dataProvider,
            placeholder,
            children,
            table,
        } = this.props
        const {
            className,
            headers,
            cells,
            sorting,
            hasFocus,
            hasSelect,
            autoFocus,
            rowSelection,
            autoCheckboxOnSelect,
            tableSize,
            useFixedHeader,
            expandable,
            scroll,
            onFetch,
            multiHeader,
            bordered,
            rowClick,
            expandedFieldId,
            rows,
            rowClass,
            width,
            height,
            textWrap,
        } = table
        const { resolveProps } = this.context

        return {
            headers: values(resolveProps(headers)),
            cells: values(resolveProps(cells)),
            sorting,
            toolbar,
            actions,
            hasFocus,
            hasSelect,
            autoFocus,
            dataProvider,
            placeholder,
            children,
            rowSelection,
            autoCheckboxOnSelect,
            tableSize,
            useFixedHeader,
            expandable,
            scroll,
            onFetch,
            multiHeader,
            rows,
            bordered,
            rowClick,
            expandedFieldId,
            rowClass,
            className,
            width,
            height,
            textWrap,
        }
    }

    prepareFilters() {
        const { resolveProps } = this.context
        const { filter } = this.props

        return resolveProps(
            filter,
            StandardFieldset,
        )
    }

    render() {
        const {
            id: widgetId,
            datasource: modelId = widgetId,
            toolbar,
            disabled,
            actions,
            table: { fetchOnInit, size },
            pageId,
            paging,
            className,
            style,
            children,
        } = this.props

        const { place = 'bottomLeft' } = paging

        return (
            <StandardWidget
                disabled={disabled}
                widgetId={widgetId}
                modelId={modelId}
                toolbar={toolbar}
                actions={actions}
                filter={this.prepareFilters()}
                {...getN2OPagination(paging, place, widgetId, modelId)}
                className={className}
                style={style}
            >
                <AdvancedTableContainer
                    widgetId={widgetId}
                    modelId={modelId}
                    pageId={pageId}
                    size={size}
                    page={1}
                    fetchOnInit={fetchOnInit}
                    {...this.getWidgetProps()}
                >
                    {children}
                </AdvancedTableContainer>
            </StandardWidget>
        )
    }
}

AdvancedTableWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

AdvancedTableWidget.defaultProps = {
    toolbar: {},
    filter: {},
    bordered: false,
    expandFieldId: 'expandedContent',
}

AdvancedTableWidget.propTypes = {
    hasFocus: PropTypes.bool,
    placeholder: PropTypes.string,
    className: PropTypes.string,
    id: PropTypes.string,
    datasource: PropTypes.string,
    disabled: PropTypes.bool,
    style: PropTypes.any,
    filter: PropTypes.any,
    children: PropTypes.any,
    pageId: PropTypes.string.isRequired,
    actions: PropTypes.object,
    toolbar: PropTypes.object,
    dataProvider: PropTypes.object,
    table: PropTypes.shape({
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
    bordered: PropTypes.bool,
    rowClick: PropTypes.object,
    paging: pagingType,
    multiHeader: PropTypes.bool,
    scroll: PropTypes.object,
    expandable: PropTypes.bool,
    useFixedHeader: PropTypes.bool,
    tableSize: PropTypes.string,
    rowSelection: PropTypes.bool,
    autoCheckboxOnSelect: PropTypes.bool,
    rowClass: PropTypes.string,
    expandFieldId: PropTypes.string,
}

export default dependency(AdvancedTableWidget)
