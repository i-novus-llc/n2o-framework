import React from 'react';
import { pure } from 'recompose';
import PropTypes from 'prop-types';
import { filter, isString } from 'lodash';

/**
 * Компонент обертка Cell
 * @param children - вставляемый компонент
 * @param hasSpan - флаг возможности colSpan/rowSpan в этой колонке
 * @param record - модель строки
 * @returns {*}
 * @constructor
 */
class AdvancedTableCell extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      width: undefined,
      height: undefined,
    };

    this.setRef = this.setRef.bind(this);
    this.getCellSize = this.getCellSize.bind(this);
  }

  setRef(el) {
    this._cell = el;
  }

  getCellSize() {
    if (this._cell) {
      return {
        parentWidth: this._cell.clientWidth,
        parentHeight: this._cell.clientHeight,
      };
    }
  }

  render() {
    const { children, hasSpan, record } = this.props;
    const { span } = record;
    let colSpan = 1;
    let rowSpan = 1;

    if (hasSpan && span) {
      if (span.colSpan === 0 || span.rowSpan === 0) {
        return null;
      }
      colSpan = span.colSpan;
      rowSpan = span.rowSpan;
    }

    return (
      <td colSpan={colSpan} rowSpan={rowSpan}>
        <div ref={this.setRef} className="n2o-advanced-table-cell-expand">
          {React.Children.map(children, child => {
            if (!child) return;

            let props = {
              ...child.props,
            };

            if (child.key) {
              props = {
                ...props,
                ...this.getCellSize(),
              };
            }

            return child && React.cloneElement(child, props);
          })}
        </div>
      </td>
    );
  }
}

AdvancedTableCell.propTypes = {
  children: PropTypes.any,
  hasSpan: PropTypes.bool,
  record: PropTypes.object,
};

AdvancedTableCell.defaultProps = {
  hasSpan: false,
  record: {},
};

export default pure(AdvancedTableCell);
