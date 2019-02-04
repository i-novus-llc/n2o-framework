import React from 'react';
import cn from 'classnames';

function AdvancedTableExpandIcon({ record, expanded, onExpand }) {
  return (
    (record.expandedContent || record.children) && (
      <div className="n2o-advanced-table-expand" onClick={e => onExpand(record, e)}>
        <i
          className={cn('fa', {
            'fa-chevron-right': !expanded,
            'fa-chevron-down': expanded
          })}
        />
      </div>
    )
  );
}

export default AdvancedTableExpandIcon;
