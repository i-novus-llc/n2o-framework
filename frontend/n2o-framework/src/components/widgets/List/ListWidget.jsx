import React from 'react'
import { compose } from 'recompose'
import PropTypes from 'prop-types'

// eslint-disable-next-line import/no-named-as-default
import dependency from '../../../core/dependency'
import StandardWidget from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import { getN2OPagination } from '../Table/N2OPagination'
import { pagingType } from '../../snippets/Pagination/types'

import ListContainer from './ListContainer'

/**
 * Виджет ListWidget
 * @param {string} id - id виджета
 * @param {object} toolbar - конфиг тулбара
 * @param {boolean} disabled - флаг активности
 * @param {string} pageId - id страницы
 * @param {object} paging - конфиг пагинации
 * @param {string} className - класс
 * @param {object} style - объект стилей
 * @param {object} filter - конфиг фильтра
 * @param {object} dataProvider - конфиг dataProvider
 * @param {boolean} fetchOnInit - флаг запроса при инициализации
 * @param {object} list - объект конфиг секций в виджете
 * @param {object} placeholder
 * @param {object|null} rowClick - кастомный клик
 * @param {boolean} hasMoreButton - флаг включения загрузки по нажатию на кнопку
 * @param {number} maxHeight - максимальная высота виджета
 * @param {boolean} fetchOnScroll - запрос при скролле
 * @param {string} prevText - текст previous кнопки пагинации
 * @param {boolean} divider - флаг разделителя между строками
 * @param {boolean} hasSelect - флаг включения выбора строк
 * @param {boolean} rows - настройка security
 * @param {object} context - контекст
 * @returns {*}
 * @constructor
 */
function ListWidget(
    {
        id: widgetId,
        datasource: modelId = widgetId,
        toolbar,
        disabled,
        pageId,
        paging,
        className,
        style,
        filter,
        dataProvider,
        fetchOnInit,
        list,
        placeholder,
        rowClick,
        hasMoreButton,
        maxHeight,
        fetchOnScroll,
        divider,
        hasSelect,
        rows,
    },
    context,
) {
    const { size, place = 'bottomLeft' } = paging

    const prepareFilters = () => context.resolveProps(filter, Fieldsets.StandardFieldset)

    const resolveSections = () => context.resolveProps(list)

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            modelId={modelId}
            toolbar={toolbar}
            filter={prepareFilters()}
            {...getN2OPagination(paging, place, widgetId, modelId)}
            className={className}
            style={style}
        >
            <ListContainer
                page={1}
                size={size}
                maxHeight={maxHeight}
                pageId={pageId}
                hasMoreButton={hasMoreButton}
                list={resolveSections()}
                disabled={disabled}
                dataProvider={dataProvider}
                widgetId={widgetId}
                modelId={modelId}
                fetchOnInit={fetchOnInit}
                rowClick={rowClick}
                fetchOnScroll={fetchOnScroll}
                deferredSpinnerStart={0}
                divider={divider}
                hasSelect={hasSelect}
                placeholder={placeholder}
                rows={rows}
            />
        </StandardWidget>
    )
}

ListWidget.propTypes = {
    id: PropTypes.string,
    datasource: PropTypes.string,
    toolbar: PropTypes.object,
    disabled: PropTypes.bool,
    pageId: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    filter: PropTypes.object,
    dataProvider: PropTypes.object,
    fetchOnInit: PropTypes.bool,
    list: PropTypes.object,
    fetchOnScroll: PropTypes.bool,
    rowClick: PropTypes.func,
    hasMoreButton: PropTypes.bool,
    maxHeight: PropTypes.number,
    prevText: PropTypes.string,
    nextText: PropTypes.string,
    hasSelect: PropTypes.bool,
    paging: pagingType,
    placeholder: PropTypes.object,
    divider: PropTypes.bool,
    rows: PropTypes.bool,
}
ListWidget.defaultProps = {
    rowClick: null,
    hasMoreButton: false,
    toolbar: {},
    disabled: false,
    className: '',
    style: {},
    filter: {},
    list: {},
    paging: {
        prevLabel: 'Назад',
        nextLabel: 'Вперед',
        maxPages: 0,
        prev: true,
        next: true,
    },
    fetchOnScroll: false,
    hasSelect: false,
}
ListWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

export default compose(dependency)(ListWidget)
