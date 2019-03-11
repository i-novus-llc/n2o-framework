import React from 'react';
import { compose } from 'recompose';
import PropTypes from 'prop-types';
import dependency from '../../../core/dependency';
import StandardWidget from '../StandardWidget';
import ListContainer from './ListContainer';
import Fieldsets from '../Form/fieldsets';
import Pagination from '../Table/TablePagination';

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
 * @param {object|null} rowClick - кастомный клик
 * @param {boolean} hasMoreButton - флаг включения загрузки по нажатию на кнопку
 * @param {number} maxHeight - максимальная высота виджета
 * @param {boolean} fetchOnScroll - запрос при скролле
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
    rowClick,
    hasMoreButton,
    maxHeight,
    fetchOnScroll
  },
  context
) {
  const prepareFilters = () => {
    return context.resolveProps(filter, Fieldsets.StandardFieldset);
  };

  const resolveSections = () => {
    return context.resolveProps(list);
  };

  return (
    <StandardWidget
      disabled={disabled}
      widgetId={widgetId}
      toolbar={toolbar}
      actions={actions}
      filter={prepareFilters()}
      bottomLeft={paging && <Pagination widgetId={widgetId} />}
    >
      <ListContainer
        page={1}
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
      />
    </StandardWidget>
  );
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
  maxHeight: PropTypes.number
};
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
  fetchOnScroll: false
};
ListWidget.contextTypes = {
  resolveProps: PropTypes.func
};

export default compose(dependency)(ListWidget);
