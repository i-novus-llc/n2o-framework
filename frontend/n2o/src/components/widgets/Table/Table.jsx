import React from 'react';
import ReactDOM from 'react-dom';
import PropTypes from 'prop-types';
import { FormattedMessage } from 'react-intl';
import _, { isEmpty, isEqual } from 'lodash';
import { pure } from 'recompose';
import { HotKeys } from 'react-hotkeys';
import cx from 'classnames';

import propsResolver from '../../../utils/propsResolver';
import TableHeader from './TableHeader';
import TableBody from './TableBody';
import TableRow from './TableRow';
import TableCell from './TableCell';

import { widgetSetSort } from '../../../actions/widgets';
import TextTableHeader from './headers/TextTableHeader';
import TextCell from './cells/TextCell/TextCell';
import SecurityCheck from '../../../core/auth/SecurityCheck';
import columnHOC from './ColumnContainer';

export const getIndex = (datasource, selectedId) => {
  const index = _.findIndex(datasource, model => model.id == selectedId);
  return index >= 0 ? index : 0;
};

const ReduxCell = columnHOC(TableCell);

/**
 * Компонент таблицы.
 * Отображает данные в виде таблицы с возможностью задать различные компоненты для колонок.
 * Сами данные ожидает в виде массива объектов.
 * Можно задавать заголовки колонок разными компонентами.
 * @reactProps {string} className - имя css класса
 * @reactProps {string} colorFieldId
 * @reactProps {object} style - имя css класса
 * @reactProps {boolean} hasSelect - становится ли строка активной при фокусе или нет
 * @reactProps {boolean} hasFocus
 * @reactProps {boolean} autoFocus - селект при фокусе
 * @reactProps {array} cells - массив из объектов ячеек
 * @reactProps {array} headers - массив из объектов ячеек-хэдеров
 * @reactProps {object} sorting
 * @reactProps {function} onSort
 * @reactProps {array} datasource - данные
 * @reactProps {function} onResolve - резолв модели в редакс
 * @reactProps {node} children - элемент потомок компонента Table
 * @reactProps {function} onFocus - событие фокуса
 * @reactProps {object} redux
 * @reactProps {object} resolveModel
 * @reactProps {string} widgetId - идентификатор виджета
 * @reactProps {boolean} isActive
 * @example
 * const headers = [
 *  {
 *    id: "id",
 *    label: "ID",
 *    sortable: false,
 *    component: FilteredHeader
 *  },
 *  {
 *    id: "name",
 *    label: "Имя",
 *    sortable: true,
 *    component: TextHeader
 *  },
 *  {
 *    id: "vip",
 *    sortable: false,
 *    component: IconHeader,
 *    componentProps: {
 *      icon: "plus"
 *    }
 *  },
 *];
 *
 *const cells = [
 *  {
 *    id: "id",
 *    component: TextCell
 *  },
 *  {
 *    id: "name",
 *    component: TextCell
 *  },
 *  {
 *    id: "vip",
 *    component: TextCell
 *  }
 *];
 *
 *const datasource = [
 *  {
 *    id: "1",
 *    name: "Foo",
 *    vip: "yes"
 *  },
 *  {
 *    id: "2",
 *    name: "Bar",
 *    vip: "no"
 *  }
 *]
 *
 *<Table headers={headers} cells={cells} datasource={datasource} />
 */
class Table extends React.Component {
  constructor(props) {
    super(props);
    this.rows = [];
    this.state = {
      focusIndex: props.autoFocus
        ? getIndex(props.datasource, props.selectedId)
        : props.hasFocus
          ? 0
          : -1,
      selectIndex: props.hasSelect ? getIndex(props.datasource, props.selectedId) : -1
    };
    this.onKeyDown = this.onKeyDown.bind(this);
  }

  handleRow(id, index, noResolve) {
    const { datasource, hasFocus, hasSelect } = this.props;
    hasSelect && !noResolve && this.props.onResolve(_.find(datasource, { id }));
    if (hasSelect && hasFocus) {
      this.setSelectAndFocus(index, index);
    } else if (hasFocus) {
      this.setNewFocusIndex(index);
    } else if (hasSelect) {
      this.setNewSelectIndex(index);
    }
  }

  setNewFocusIndex(index) {
    this.setState({ focusIndex: index }, () => this.focusActiveRow());
  }

  setNewSelectIndex(index) {
    this.setState({ selectIndex: index });
  }

  setSelectAndFocus(selectIndex, focusIndex) {
    this.setState({ selectIndex, focusIndex }, () => {
      this.focusActiveRow();
    });
  }

  focusActiveRow() {
    this.rows[this.state.focusIndex] &&
      ReactDOM.findDOMNode(this.rows[this.state.focusIndex]).focus();
  }

  onKeyDown(e) {
    const keyNm = e.key;
    const { datasource, children, hasFocus, hasSelect, autoFocus, onResolve } = this.props;
    const { focusIndex } = this.state;
    if (keyNm === 'ArrowUp' || keyNm === 'ArrowDown') {
      if (!React.Children.count(children) && hasFocus) {
        let newFocusIndex = keyNm === 'ArrowUp' ? focusIndex - 1 : focusIndex + 1;
        newFocusIndex =
          newFocusIndex < datasource.length && newFocusIndex >= 0 ? newFocusIndex : focusIndex;
        if (hasSelect && autoFocus) {
          this.setSelectAndFocus(newFocusIndex, newFocusIndex);
          this.props.onResolve(datasource[newFocusIndex]);
        } else {
          this.setNewFocusIndex(newFocusIndex);
        }
      }
    } else if (keyNm === ' ' && hasSelect && !autoFocus) {
      this.props.onResolve(datasource[this.state.focusIndex]);
      this.setNewSelectIndex(this.state.focusIndex);
    }
  }

  componentDidUpdate(prevProps) {
    const { hasSelect, datasource, selectedId, isAnyTableFocused, isActive } = this.props;
    if (hasSelect && !isEqual(datasource, prevProps.datasource)) {
      const id = getIndex(datasource, selectedId);
      isAnyTableFocused && !isActive ? this.setNewSelectIndex(id) : this.setSelectAndFocus(id, id);
    }
  }

  componentDidMount() {
    const { isAnyTableFocused, isActive, focusIndex, selectIndex } = this.state;
    !isAnyTableFocused && isActive && this.setSelectAndFocus(selectIndex, focusIndex);
  }

  renderCell(props) {
    const { redux } = this.props;
    if (redux) {
      return <ReduxCell {...props} />;
    }
    return <TableCell {...props} />;
  }

  render() {
    const {
      datasource,
      actions,
      headers,
      cells,
      sorting,
      onSort,
      onFocus,
      onResolve,
      children,
      hasFocus,
      rowColor,
      widgetId,
      isActive
    } = this.props;

    if (React.Children.count(children)) {
      return (
        <div className="table-responsive">
          <table className="table table-sm table-hover">{children}</table>
        </div>
      );
    }
    return (
      <HotKeys keyMap={{ events: ['up', 'down', 'space'] }} handlers={{ events: this.onKeyDown }}>
        <div className="table-responsive">
          <table
            className={cx('n2o-table table table-sm table-hover', { 'has-focus': hasFocus })}
            ref={table => (this.table = table)}
            onFocus={!isActive ? onFocus : undefined}
          >
            {headers && (
              <TableHeader>
                <TableRow>
                  {headers.map(header => {
                    return this.renderCell({
                      key: header.id,
                      columnId: header.id,
                      widgetId,
                      as: 'th',
                      sorting: sorting[header.id],
                      onSort: onSort,
                      ...header
                    });
                  })}
                </TableRow>
              </TableHeader>
            )}
            <TableBody>
              {datasource && datasource.length ? (
                datasource.map((data, index) => (
                  <TableRow
                    onClick={isActive ? () => this.handleRow(data.id, index) : undefined}
                    onFocus={!isActive ? () => this.handleRow(data.id, index, true) : undefined}
                    key={index}
                    color={rowColor && propsResolver(rowColor, data)}
                    ref={row => {
                      this.rows[index] = row;
                    }}
                    model={data}
                    className={cx({ 'table-active': index === this.state.selectIndex })}
                    tabIndex={1}
                  >
                    {cells.map(cell => {
                      return this.renderCell({
                        index,
                        key: cell.id,
                        widgetId,
                        columnId: cell.id,
                        model: data,
                        ...cell
                      });
                    })}
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={headers.length} style={{ textAlign: 'center' }}>
                    <span className="text-muted">
                      <FormattedMessage
                        id="table.notFound"
                        defaultMessage="Нет данных для отображения"
                      />
                    </span>
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </table>
        </div>
      </HotKeys>
    );
  }
}

Table.propTypes = {
  /* Default props */
  className: PropTypes.string,
  colorFieldId: PropTypes.string,
  style: PropTypes.string,
  children: PropTypes.node,
  widgetId: PropTypes.string,
  isActive: PropTypes.bool,
  /* Specific props */
  hasFocus: PropTypes.bool,
  hasSelect: PropTypes.bool,
  autoFocus: PropTypes.bool,
  headers: PropTypes.array,
  cells: PropTypes.array,
  sorting: PropTypes.object,
  onSort: PropTypes.func,
  redux: PropTypes.object,
  /* Logic props */
  datasource: PropTypes.array,
  resolveModel: PropTypes.object,
  onResolve: PropTypes.func,
  onFocus: PropTypes.func
};

Table.defaultProps = {
  sorting: {},
  onResolve: () => {},
  redux: false
};

Table.Header = TableHeader;
Table.Body = TableBody;
Table.Row = TableRow;
Table.Cell = TableCell;

//Table = pure(Table);
export default Table;
