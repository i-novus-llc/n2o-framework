import React from 'react';
import PropTypes from 'prop-types';
import { get, set } from 'lodash';

import withCell from '../../withCell';
import CheckboxN2O from '../../../../controls/Checkbox/CheckboxN2O';

function CheckboxCell({
  callActionImpl,
  updateFieldInModel,
  model,
  fieldKey,
  id,
  visible,
  readOnly,
  ...rest
}) {
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
    visible && (
      <CheckboxN2O
        className="сheckbox-сell"
        inline={true}
        onChange={onChange}
        disabled={readOnly}
        checked={model && get(model, fieldKey || id)}
        {...rest}
      />
    )
  );
}

CheckboxCell.propTypes = {
  id: PropTypes.string,
  model: PropTypes.object,
  fieldKey: PropTypes.string,
  className: PropTypes.string,
  readOnly: PropTypes.bool,
  callInvoke: PropTypes.func,
  visible: PropTypes.bool
};

CheckboxCell.defaultProps = {
  readOnly: false,
  visible: true
};

export default withCell(CheckboxCell);
