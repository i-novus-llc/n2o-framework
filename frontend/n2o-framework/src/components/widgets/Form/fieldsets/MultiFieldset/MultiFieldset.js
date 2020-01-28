import React from 'react';
import { FieldArray } from 'redux-form';

import MultiFieldsetItem from './MultiFieldsetItem';

function MultiFieldset({
  name,
  label,
  addButtonLabel,
  removeButtonLabel,
  needAddButton,
  needRemoveButton,
  needCopyButton,
  needRemoveAllButton,
  canRemoveFirstItem,
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
            canRemoveFirstItem={canRemoveFirstItem}
          />
        )}
      />
    </div>
  );
}

export default MultiFieldset;
