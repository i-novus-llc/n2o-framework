import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Table from 'rc-table';
import AdvancedTableExpandIcon from './AdvancedTableExpandIcon';
import AdvancedTableExpandedRenderer from './AdvancedTableExpandedRenderer';
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
      rowKey,
      expandable = false,
      onExpand,
      expandIconAsCell,
      expandRowByClick,
      onExpandedRowsChange,
      expandedRowKeys,
      tableSize,
      useFixedHeader,
      hotKeys
    } = this.props;
    return (
      <HotKeys keyMap={hotKeys.keyMap} handlers={hotKeys.handlers}>
        <div className="n2o-advanced-table table-responsive">
          <Table
            prefixCls={'n2o-advanced-table'}
            className={cx('n2o-table table table-hover', className, {
              'has-focus': hasFocus,
              [`table-${tableSize}`]: tableSize
            })}
            columns={columns}
            data={data}
            onRow={onRow}
            components={components}
            rowKey={rowKey && rowKey}
            expandIcon={AdvancedTableExpandIcon}
            expandIconAsCell={expandIconAsCell}
            expandRowByClick={expandRowByClick}
            expandedRowRender={expandable && AdvancedTableExpandedRenderer}
            expandedRowKeys={expandedRowKeys}
            onExpandedRowsChange={onExpandedRowsChange}
            onExpand={onExpand}
            useFixedHeader={useFixedHeader}
            indentSize={'20px'}
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
