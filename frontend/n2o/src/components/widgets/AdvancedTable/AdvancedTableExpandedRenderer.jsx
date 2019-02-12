import React from 'react';
import Table from 'rc-table';
import AdvancedTableEmptyText from './AdvancedTableEmptyText';

function AdvancedTableExpandedRenderer(record, index, indent, expanded) {
  const { expandedContent } = record;
  if (expandedContent) {
    if (expandedContent.type === 'table') {
      return (
        <Table
          className="n2o-advanced-table-nested"
          columns={expandedContent.columns}
          data={expandedContent.data}
          emptyText={AdvancedTableEmptyText}
        />
      );
    }
    if (expandedContent.type === 'html') {
      return (
        <div
          className="n2o-advanced-table-expanded-row-content"
          dangerouslySetInnerHTML={{ __html: expandedContent.value }}
        />
      );
    }
    return <div className="n2o-advanced-table-expanded-row-content">{expandedContent.value}</div>;
  }
  return <div />;
}

export default AdvancedTableExpandedRenderer;
