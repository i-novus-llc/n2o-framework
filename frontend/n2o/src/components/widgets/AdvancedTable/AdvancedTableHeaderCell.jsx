import React, { Component } from 'react';
import { pick } from 'lodash';
import PropTypes from 'prop-types';
import { Resizable } from 'react-resizable';
import Factory from '../../../core/factory/Factory';
import 'react-resizable/css/styles.css';
import AdvancedTableFilter from './AdvancedTableFilter';
import columnHOC from '../Table/ColumnContainer';
import TableCell from '../Table/TableCell';

const ReduxCell = columnHOC(TableCell);

class AdvancedTableHeaderCell extends Component {
  constructor(props) {
    super(props);

    this.state = {
      visible: false
    };

    this.handleVisibleChange = this.handleVisibleChange.bind(this);
  }

  handleVisibleChange(visible) {
    this.setState({ visible });
  }

  render() {
    const {
      id,
      width,
      onFilter,
      onResize,
      className,
      redux,
      title,
      component,
      filterable,
      resizable,
      rowSelection,
      selectionHead
    } = this.props;

    const propStyles = pick(this.props, ['width']);
    const cellContent = redux ? (
      <ReduxCell
        className={className}
        {...propStyles}
        {...this.props}
        component={this.props.component}
        label={title}
        as={'div'}
      />
    ) : (
      <TableCell
        className={className}
        {...propStyles}
        {...this.props}
        component={this.props.component}
        as={'div'}
      />
    );

    const cell = (
      <th className="n2o-advanced-table-header-cell">
        <div className="n2o-advanced-table-header-cell-content">
          {filterable ? (
            <AdvancedTableFilter
              id={id}
              onVisibleChange={this.handleVisibleChange}
              visible={this.state.visible}
              onFilter={onFilter}
            >
              {!selectionHead ? cellContent : this.props.children}
            </AdvancedTableFilter>
          ) : !selectionHead ? (
            cellContent
          ) : (
            this.props.children
          )}
        </div>
      </th>
    );
    return (
      <React.Fragment>
        {resizable && width ? (
          <Resizable width={width} height={0} onResize={onResize}>
            {cell}
          </Resizable>
        ) : (
          cell
        )}
      </React.Fragment>
    );
  }

  // render() {
  //   const {
  //     onResize,
  //     resizable,
  //     className,
  //     children,
  //     filterable,
  //     title,
  //     onFilter,
  //     id,
  //     width,
  //     ...restProps
  //   } = this.props;
  //   console.log('point')
  //   console.log(this.props)
  //   const component = (
  //     <th {...this.props} width={width} className="n2o-advanced-table-header-cell">
  //       <div className="n2o-advanced-table-header-cell-content">
  //         {filterable ? (
  //           <AdvancedTableFilter
  //             id={id}
  //             onVisibleChange={this.handleVisibleChange}
  //             visible={this.state.visible}
  //             onFilter={onFilter}
  //           >
  //             {title}
  //           </AdvancedTableFilter>
  //         ) : (
  //           this.props.title || this.props.component
  //         )}
  //       </div>
  //     </th>
  //   );
  //   return !resizable || (!resizable && !width) ? (
  //     component
  //   ) : (
  //     <Resizable width={this.props.width} height={0} onResize={onResize}>
  //       {component}
  //     </Resizable>
  //   );
  // }
}

AdvancedTableHeaderCell.propTypes = {};

export default AdvancedTableHeaderCell;
