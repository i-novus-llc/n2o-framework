import React from 'react';
import { compose, withHandlers, mapProps } from 'recompose';
import some from 'lodash/some';

import * as presets from '../../../../core/validation/presets';
import {
  addFieldMessage,
  removeFieldMessage,
} from '../../../../actions/formPlugin';

export default Field => {
  function FieldWithValidate(props) {
    return <Field {...props} />;
  }

  const enhance = compose(
    withHandlers({
      validateField: ({ dispatch, validation, meta, input }) => value => {
        let message = {};
        const validateResult = some(validation, ({ severity, text, type }) => {
          const validationFunc = presets[type];
          message = {
            severity,
            text,
          };

          return validationFunc(input.name, { [input.name]: value });
        });

        if (validateResult) {
          dispatch(removeFieldMessage(meta.form, input.name));
        } else {
          dispatch(
            addFieldMessage(meta.form, input.name, message, meta.touched)
          );
        }
      },
    }),
    withHandlers({
      onBlur: ({ input, meta, validateField }) => e => {
        const value = e.target.value;

        if (meta.touched) {
          validateField(value);
        }
        input.onBlur(e);
      },
    }),
    mapProps(({ input, onBlur, ...rest }) =>
      Object.assign(
        {},
        {
          input: Object.assign({}, input, { onBlur }),
        },
        rest
      )
    )
  );

  return enhance(FieldWithValidate);
};
