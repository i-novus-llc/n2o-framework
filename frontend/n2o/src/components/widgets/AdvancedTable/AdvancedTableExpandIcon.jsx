import React from 'react';
import cn from 'classnames';

function AdvancedTableExpandIcon({ record, expanded, onExpand }) {
  return (
    <span className="n2o-advanced-table-expand" onClick={e => onExpand(record, e)}>
      {(record.expandedContent || record.children) && (
        <i
          className={cn('fa', {
            'fa-chevron-right': !expanded,
            'fa-chevron-down': expanded
          })}
        />
      )}
    </span>
  );
}

export default AdvancedTableExpandIcon;
