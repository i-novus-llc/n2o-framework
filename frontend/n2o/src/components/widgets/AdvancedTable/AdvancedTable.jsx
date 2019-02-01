import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AdvancedCellRenderer from './AdvancedTableCellRenderer';
import Table from 'rc-table';
import { HotKeys } from 'react-hotkeys';

const columns = [
  {
    title: '手机号',
    dataIndex: 'a',
    colSpan: 2,
    width: 100,
    key: 'a',
    render(o, row, index) {
      const obj = {
        children: o,
        props: {}
      };
      // 设置第一行为链接
      if (index === 0) {
        obj.children = AdvancedCellRenderer;
      }
      // 第5行合并两列
      if (index === 4) {
        obj.props.colSpan = 2;
      }

      if (index === 5) {
        obj.props.colSpan = 6;
      }
      return obj;
    }
  },
  {
    title: '电话',
    dataIndex: 'b',
    colSpan: 0,
    width: 100,
    key: 'b',
    render(o, row, index) {
      const obj = {
        children: o,
        props: {}
      };
      // 列合并掉的表格设置colSpan=0，不会去渲染
      if (index === 4 || index === 5) {
        obj.props.colSpan = 0;
      }
      return obj;
    }
  },
  {
    title: 'Name',
    dataIndex: 'c',
    width: 100,
    key: 'c',
    render(o, row, index) {
      const obj = {
        children: o,
        props: {}
      };

      if (index === 5) {
        obj.props.colSpan = 0;
      }
      return obj;
    }
  },
  {
    title: 'Address',
    dataIndex: 'd',
    width: 200,
    key: 'd',
    render(o, row, index) {
      const obj = {
        children: o,
        props: {}
      };
      if (index === 0) {
        obj.props.rowSpan = 2;
      }
      if (index === 1 || index === 5) {
        obj.props.rowSpan = 0;
      }

      return obj;
    }
  },
  {
    title: 'Gender',
    dataIndex: 'e',
    width: 200,
    key: 'e',
    render(o, row, index) {
      const obj = {
        children: o,
        props: {}
      };
      if (index === 5) {
        obj.props.colSpan = 0;
      }
      return obj;
    }
  },
  {
    title: 'Operations',
    dataIndex: '',
    key: 'f',
    render(o, row, index) {
      if (index === 5) {
        return {
          props: {
            colSpan: 0
          }
        };
      }
      return <a href="#">Operations</a>;
    }
  }
];

class AdvancedTable extends Component {
  constructor(props) {
    super(props);
    this.prepareColumns = this.prepareColumns.bind(this);
  }

  prepareColumns(columns) {
    return columns.map(({ id, ...rest }) => {
      return {
        id,
        value: 'test',
        render(o, row, index) {
          return {
            children: o,
            props: {
              id,
              ...rest
            }
          };
        }
      };
    });
  }

  render() {
    return (
      <HotKeys>
        <div className="n2o-advanced-table">
          <Table columns={columns} />
        </div>
      </HotKeys>
    );
  }
}

AdvancedTable.propTypes = {};

export default AdvancedTable;
