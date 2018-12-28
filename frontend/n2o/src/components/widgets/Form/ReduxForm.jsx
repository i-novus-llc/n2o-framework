import { compose, withProps, getContext } from 'recompose';
import { reduxForm } from 'redux-form';
import Form from './Form';
import ReduxField from './ReduxField';
import createValidator from '../../../core/validation/createValidator';
import PropTypes from 'prop-types';

const ReduxForm = compose(
  getContext({
    store: PropTypes.object
  }),
  withProps(props => ({
    ...createValidator(props.validation, props.form, props.store)
  })),
  reduxForm({
    destroyOnUnmount: true,
    enableReinitialize: true
  })
)(Form);

ReduxForm.Field = ReduxField;

export default ReduxForm;
