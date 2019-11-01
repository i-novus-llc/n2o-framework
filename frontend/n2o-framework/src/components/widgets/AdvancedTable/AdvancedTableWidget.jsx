import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AdvancedTableContainer from './AdvancedTableContainer';
import TablePagination from '../Table/TablePagination';
import StandardWidget from '../StandardWidget';
import Fieldsets from '../Form/fieldsets';

import dependency from '../../../core/dependency';
import values from 'lodash/values';

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
      className,
      headers,
      cells,
      sorting,
      hasFocus,
      hasSelect,
      autoFocus,
      rowSelection,
      tableSize,
      useFixedHeader,
      expandable,
      scroll,
      onFetch,
      multiHeader,
      bordered,
      rowClick,
      expandedFieldId,
      rowClass,
    } = this.props.table;
    const { toolbar, actions, dataProvider, placeholder } = this.props;
    const { resolveProps } = this.context;
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
      rowSelection,
      tableSize,
      useFixedHeader,
      expandable,
      scroll,
      onFetch,
      multiHeader,
      bordered,
      rowClick,
      expandedFieldId,
      rowClass,
      className,
    };
  }

  prepareFilters() {
    return this.context.resolveProps(
      this.props.filter,
      Fieldsets.StandardFieldset
    );
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
    } = this.props;
    return (
      <StandardWidget
        disabled={disabled}
        widgetId={widgetId}
        toolbar={toolbar}
        actions={actions}
        filter={this.prepareFilters()}
        bottomLeft={paging && <TablePagination widgetId={widgetId} />}
        className={className}
        style={style}
      >
        <AdvancedTableContainer
          widgetId={widgetId}
          pageId={pageId}
          size={size}
          page={1}
          fetchOnInit={fetchOnInit}
          {...this.getWidgetProps()}
        />
      </StandardWidget>
    );
  }
}

AdvancedTableWidget.contextTypes = {
  resolveProps: PropTypes.func,
};

AdvancedTableWidget.defaultProps = {
  toolbar: {},
  filter: {},
  bordered: false,
  expandFieldId: 'expandedContent',
};

AdvancedTableWidget.propTypes = {
  pageId: PropTypes.string.isRequired,
  widgetId: PropTypes.string,
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
  paging: PropTypes.oneOfType([PropTypes.bool, PropTypes.object]),
  multiHeader: PropTypes.bool,
  scroll: PropTypes.object,
  expandable: PropTypes.bool,
  useFixedHeader: PropTypes.bool,
  tableSize: PropTypes.string,
  rowSelection: PropTypes.bool,
  rowClass: PropTypes.string,
  expandFieldId: PropTypes.string,
};

export default dependency(AdvancedTableWidget);
