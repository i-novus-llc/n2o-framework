import React from 'react';
import Factory from '../../../core/factory/Factory';

function AdvancedTableCellRenderer(props) {
  console.log(props);
  return <Factory {...props} />;
}

export default AdvancedTableCellRenderer;
