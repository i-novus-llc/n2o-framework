import React from 'react';
import PropTypes from 'prop-types';
import { get, set } from 'lodash';

import withCell from '../../withCell';
import CheckboxN2O from '../../../../controls/Checkbox/CheckboxN2O';

function CheckboxCell({ callActionImpl, updateFieldInModel, model, fieldKey, id, ...rest }) {
  const onChange = e => {
    const data = set(
      {
        ...model
      },
      fieldKey || id,
      e.nativeEvent.target.checked
    );
    updateFieldInModel(e.nativeEvent.target.checked);
    callActionImpl({ model: data });
  };

  return (
    <CheckboxN2O
      className="сheckbox-сell"
      inline={true}
      onChange={onChange}
      checked={model && get(model, fieldKey || id)}
      {...rest}
    />
  );
}

CheckboxCell.propTypes = {
  id: PropTypes.string,
  model: PropTypes.object,
  fieldKey: PropTypes.string,
  className: PropTypes.string,
  readOnly: PropTypes.bool,
  callInvoke: PropTypes.func
};

CheckboxCell.defaultProps = {
  readOnly: false
};

export default withCell(CheckboxCell);
