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
  disabled,
  ...rest
}) {
  const onChange = e => {
    const data = set(
      {
        ...model,
      },
      fieldKey || id,
      e.nativeEvent.target.checked
    );
    updateFieldInModel(e.nativeEvent.target.checked);
    callActionImpl(e, { model: data });
  };
  return (
    visible && (
      <CheckboxN2O
        className="сheckbox-сell"
        inline={true}
        onChange={onChange}
        disabled={disabled}
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
  callInvoke: PropTypes.func,
  visible: PropTypes.bool,
};

CheckboxCell.defaultProps = {
  visible: true,
  disabled: false,
};

export default withCell(CheckboxCell);
