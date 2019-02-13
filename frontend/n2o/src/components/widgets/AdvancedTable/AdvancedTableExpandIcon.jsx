import React from 'react';
import cn from 'classnames';

function AdvancedTableExpandIcon({ record, expanded, onExpand }) {
  return (
    <span
      className={cn({ 'n2o-advanced-table-expand': record.children || record.expandedContent })}
      onClick={e => onExpand(record, e)}
    >
      {(record.expandedContent || record.children) && (
        <i
          className={cn('fa', 'n2o-advanced-table-expand-icon', {
            'n2o-advanced-table-expand-icon-expanded': expanded,
            'fa-angle-right': !expanded,
            'fa-angle-down': expanded
          })}
        />
      )}
    </span>
  );
}

export default AdvancedTableExpandIcon;
