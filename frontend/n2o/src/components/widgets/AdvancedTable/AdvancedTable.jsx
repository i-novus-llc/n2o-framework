import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Table from 'rc-table';
import { HotKeys } from 'react-hotkeys';
import cx from 'classnames';

/**
 * Компонент Таблица
 * @reactProps {boolean} hasFocus - флаг наличия фокуса
 * @reactProps {string} className - класс таблицы
 * @reactProps {Array.<Object>} columns - настройки колонок
 * @reactProps {Array.<Object>} data - данные
 * @reactProps {function} onRow - функция прокидывания дополнительных параметров в компонент строки
 * @reactProps {Object} components - компоненты обертки
 * @reactProps {Node} emptyText - компонент пустых данных
 * @reactProps {object} hotKeys - настройка hot keys
 */
class AdvancedTable extends Component {
  render() {
    const {
      hasFocus,
      className,
      columns,
      data,
      onRow,
      components,
      emptyText,
      hotKeys
    } = this.props;
    return (
      <HotKeys keyMap={hotKeys.keyMap} handlers={hotKeys.handlers}>
        <div className="n2o-advanced-table table-responsive">
          <Table
            className={cx('n2o-table table table-sm table-hover', className, {
              'has-focus': hasFocus
            })}
            columns={columns}
            data={data}
            onRow={onRow}
            components={components}
            emptyText={emptyText}
          />
        </div>
      </HotKeys>
    );
  }
}

AdvancedTable.propTypes = {
  hasFocus: PropTypes.bool,
  className: PropTypes.string,
  columns: PropTypes.arrayOf(PropTypes.object),
  data: PropTypes.arrayOf(PropTypes.object),
  onRow: PropTypes.func,
  components: PropTypes.object,
  emptyText: PropTypes.node,
  hotKeys: PropTypes.object
};

export default AdvancedTable;
