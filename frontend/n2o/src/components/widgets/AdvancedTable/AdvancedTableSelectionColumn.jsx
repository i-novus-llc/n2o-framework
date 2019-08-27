import React from 'react';
import { compose, withState, withHandlers } from 'recompose';
import Checkbox from '../../controls/Checkbox/CheckboxN2O';

import { get } from 'lodash';

function AdvancedTableSelectionColumn({ onChange, checked, setRef }) {
  return (
    <div className="n2o-advanced-table-selection-item">
      <Checkbox
        inline={true}
        checked={checked}
        onChange={onChange}
        ref={setRef}
      />
    </div>
  );
}

export default compose(
  withState('checked', 'setChecked', false),
  withHandlers({
    onChange: ({ onChange, setChecked }) => event => {
      const checked = !get(event, 'target.checked', false);

      setChecked(checked);
      onChange(checked);
    },
  })
)(AdvancedTableSelectionColumn);
