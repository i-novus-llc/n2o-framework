import React from 'react';
import { reduxForm } from 'redux-form';
import Form from './Form';
import ReduxField from './ReduxField';
import createValidator from '../../../core/validation/createValidator';
import PropTypes from 'prop-types';

const ReduxFormWrapper = reduxForm({
  destroyOnUnmount: true,
  enableReinitialize: true
})(Form);

function ReduxForm(props, context) {
  return (
    <ReduxFormWrapper
      {...createValidator(props.validation, props.form, context.store)}
      {...props}
    />
  );
}

ReduxForm.contextTypes = {
  store: PropTypes.object
};

ReduxForm.Field = ReduxField;

export default ReduxForm;
