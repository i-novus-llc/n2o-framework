import React, { Component } from 'react';
import { isArray, isString } from 'lodash';
import cn from 'classnames';
import PropTypes from 'prop-types';
import { Resizable } from 'react-resizable';
import AdvancedTableFilter from './AdvancedTableFilter';

/**
 * Компонент ячейки заголовка
 * @param children - компонент потомок
 * @param className - класс ячейки
 * @param columnId - id колонки
 * @param dataIndex - id ключа с данными из модели
 * @param id - id колонки
 * @param index - index колонки
 * @param label - текст заголовка
 * @param multiHeader - флаг многоуровневого заголовка
 * @param onFilter - callback на фильтрацию
 * @param onResize - callback на изменение размера колонок
 * @param onSort - функция вызова сортировки
 * @param sorting - настройки сортировки
 * @param title - компонент в ячейки заголовка
 * @param widgetId - id виджета
 *  @param width - длина колонки
 *  @param resizable - флаг функции resize колонки
 *  @param selectionHead - флаг чекбокса в заголовке
 *  @param selectionClass - класс для чекбокса в заголовке
 */
class AdvancedTableHeaderCell extends Component {
  constructor(props) {
    super(props);

    this.renderCell = this.renderCell.bind(this);
    this.renderMultiCell = this.renderMultiCell.bind(this);
    this.renderStringChild = this.renderStringChild.bind(this);
  }

  renderMultiCell() {
    const {
      colSpan,
      rowSpan,
      className,
      columnId,
      id,
      label,
      sorting,
      onSort,
      widgetId
    } = this.props;
    return (
      <th
        {...this.props}
        title={label}
        className={cn('n2o-advanced-table-header-text-center', className)}
        colSpan={colSpan}
        rowSpan={rowSpan}
      >
        {React.createElement(this.props.component, {
          className,
          columnId,
          id,
          label,
          sorting: sorting && sorting[id],
          onSort,
          widgetId
        })}
      </th>
    );
  }

  renderStringChild() {
    const { className, children } = this.props;
    return <th className={cn('n2o-advanced-table-header-text-center', className)}>{children}</th>;
  }

  renderCell() {
    const {
      id,
      multiHeader,
      children,
      selectionHead,
      selectionClass,
      filterable,
      colSpan,
      rowSpan,
      onFilter,
      filters,
      label,
      title
    } = this.props;

    let cellContent = null;

    if (isString(title)) {
      cellContent = title;
    } else if (isString(children)) {
      return this.renderStringChild();
    } else if (multiHeader && isArray(children)) {
      return this.renderMultiCell();
    } else {
      cellContent = children;
    }

    const cell = (
      <th
        {...this.props}
        title={label}
        rowSpan={rowSpan}
        colSpan={colSpan}
        className={cn('n2o-advanced-table-header-cel', {
          [selectionClass]: selectionHead,
          'n2o-advanced-table-header-text-center': multiHeader
        })}
      >
        <div className="n2o-advanced-table-header-cell-content">
          {filterable ? (
            <AdvancedTableFilter id={id} onFilter={onFilter} value={filters && filters[id]}>
              {cellContent}
            </AdvancedTableFilter>
          ) : (
            cellContent
          )}
        </div>
      </th>
    );

    return cell;
  }

  render() {
    const { width, onResize, resizable } = this.props;

    return (
      <React.Fragment>
        {resizable && width ? (
          <Resizable width={width} height={0} onResize={onResize} handleSize={[10, 10]}>
            {this.renderCell()}
          </Resizable>
        ) : (
          this.renderCell()
        )}
      </React.Fragment>
    );
  }
}

AdvancedTableHeaderCell.propTypes = {
  children: PropTypes.oneOf(PropTypes.array, PropTypes.string, PropTypes.object),
  className: PropTypes.string,
  columnId: PropTypes.string,
  dataIndex: PropTypes.string,
  id: PropTypes.string,
  index: PropTypes.oneOf(PropTypes.string, PropTypes.number),
  label: PropTypes.string,
  multiHeader: PropTypes.bool,
  onCell: PropTypes.func,
  onFilter: PropTypes.func,
  onHeaderCell: PropTypes.func,
  onResize: PropTypes.func,
  onSort: PropTypes.func,
  sorting: PropTypes.object,
  title: PropTypes.oneOf(PropTypes.string, PropTypes.func),
  widgetId: PropTypes.string,
  width: PropTypes.number,
  resizable: PropTypes.bool,
  selectionHead: PropTypes.bool,
  selectionClass: PropTypes.string
};

AdvancedTableHeaderCell.defaultProps = {
  multiHeader: false,
  resizable: false,
  selectionHead: false,
  sorting: {},
  onResize: () => {},
  onSort: () => {},
  onFilter: () => {}
};

export default AdvancedTableHeaderCell;
