import React from 'react';
import PropTypes from 'prop-types';
import TableTree, { Headers, Header, Rows, Row, Cell } from '@atlaskit/table-tree';

class TableTreeExample extends React.Component {

  /**
   * Рендер
   */

  render() {
    const {
      headers,
      items
    } = this.props;

    return (
      <TableTree>
        <Headers>
          {
            headers.map((header) =>
              <Header width={header.width}>
                {header.title}
              </Header>
            )
          }
        </Headers>
        <Rows
          items={items}
          render={({ title, numbering, page, children }) => (
            <Row itemId={numbering} hasChildren={children.length > 0}>
              <Cell singleLine>{title}</Cell>
              <Cell singleLine>{numbering}</Cell>
              <Cell singleLine>{page}</Cell>
            </Row>
          )}
        />
      </TableTree>
    );
  }
}

TableTreeExample.propTypes = {
  headers: PropTypes.array,
  items: PropTypes.func,
};

export default TableTreeExample;
