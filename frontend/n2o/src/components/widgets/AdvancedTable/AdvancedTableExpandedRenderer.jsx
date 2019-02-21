import React from 'react';
import Table from 'rc-table';
import PropTypes from 'prop-types';
import AdvancedTableEmptyText from './AdvancedTableEmptyText';

/**
 * Компонент контента в подстроке text/html/table
 * @param record - модель строки
 * @param expandedFieldId - id поля в настройками подстроки
 * @returns {*}
 * @constructor
 */
function AdvancedTableExpandedRenderer({ record, expandedFieldId }) {
  const expandedContent = record[expandedFieldId];
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
    } else if (expandedContent.type === 'html') {
      return (
        <div
          className="n2o-advanced-table-expanded-row-content"
          dangerouslySetInnerHTML={{ __html: expandedContent.value }}
        />
      );
    } else {
      return <div className="n2o-advanced-table-expanded-row-content">{expandedContent.value}</div>;
    }
  }

  return null;
}

AdvancedTableExpandedRenderer.propTypes = {
  record: PropTypes.object
};

export default AdvancedTableExpandedRenderer;
