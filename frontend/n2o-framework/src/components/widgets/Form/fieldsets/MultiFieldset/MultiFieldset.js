import React from 'react';
import { FieldArray } from 'redux-form';

import MultiFieldsetItem from './MultiFieldsetItem';

function MultiFieldset({
  name,
  label,
  addButtonLabel,
  removeButtonLabel,
  removeAllButtonLabel,
  copyButtonLabel,
  needAddButton,
  needRemoveButton,
  needCopyButton,
  needRemoveAllButton,
  render,
  rows,
}) {
  return (
    <div className="n2o-multi-fieldset">
      <FieldArray
        name={name}
        component={props => (
          <MultiFieldsetItem
            {...props}
            render={render}
            rows={rows}
            label={label}
            needAddButton={needAddButton}
            needRemoveButton={needRemoveButton}
            needCopyButton={needCopyButton}
            needRemoveAllButton={needRemoveAllButton}
            addButtonLabel={addButtonLabel}
            removeButtonLabel={removeButtonLabel}
            copyButtonLabel={copyButtonLabel}
            removeAllButtonLabel={removeAllButtonLabel}
          />
        )}
      />
    </div>
  );
}

export default MultiFieldset;
