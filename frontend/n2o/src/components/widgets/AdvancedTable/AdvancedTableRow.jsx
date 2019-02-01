import React, { Component } from 'react';
import PropTypes from 'prop-types';

class AdvancedTableRow extends Component {
  onClick(ref) {
    const { onClick } = this.props;
    onClick && onClick(ref);
  }

  render() {
    const { children } = this.props;
    return (
      <tr
        ref={el => (this.ref = el)}
        className="n2o-advanced-table-row"
        onClick={this.onClick.bind(this, this.ref)}
      >
        {children}
      </tr>
    );
  }
}

AdvancedTableRow.propTypes = {};

export default AdvancedTableRow;
