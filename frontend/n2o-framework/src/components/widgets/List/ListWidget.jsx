import React from 'react'
import { compose } from 'recompose'
import PropTypes from 'prop-types'

import dependency from '../../../core/dependency'
import StandardWidget from '../StandardWidget'
import Fieldsets from '../Form/fieldsets'
import Pagination from '../Table/TablePagination'

import ListContainer from './ListContainer'

/**
 * Виджет ListWidget
 * @param {string} widgetId - id виджета
 * @param {object} toolbar - конфиг тулбара
 * @param {boolean} disabled - флаг активности
 * @param {object} actions - объект экшенов
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
 * @param {string} nextText - текст next кнопки пагинации
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
        toolbar,
        disabled,
        actions,
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
    const { size } = paging

    const prepareFilters = () => context.resolveProps(filter, Fieldsets.StandardFieldset)

    const resolveSections = () => context.resolveProps(list)

    return (
        <StandardWidget
            disabled={disabled}
            widgetId={widgetId}
            toolbar={toolbar}
            actions={actions}
            filter={prepareFilters()}
            bottomLeft={paging && <Pagination widgetId={widgetId} {...paging} />}
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
                fetchOnInit={fetchOnInit}
                actions={actions}
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
    widgetId: PropTypes.string,
    toolbar: PropTypes.object,
    disabled: PropTypes.bool,
    actions: PropTypes.object,
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
}
ListWidget.defaultProps = {
    rowClick: null,
    hasMoreButton: false,
    toolbar: {},
    disabled: false,
    actions: {},
    className: '',
    style: {},
    filter: {},
    list: {},
    paging: {
        prevText: 'Назад',
        nextText: 'Вперед',
        withoutBody: true,
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
