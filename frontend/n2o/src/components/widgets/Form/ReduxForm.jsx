import React from 'react';
import { compose, withProps, getContext } from 'recompose';
import { Prompt } from 'react-router-dom';
import { reduxForm } from 'redux-form';
import Form from './Form';
import ReduxField from './ReduxField';
import createValidator from '../../../core/validation/createValidator';
import PropTypes from 'prop-types';

function ReduxForm(props) {
  return (
    <React.Fragment>
      {props.prompt && <Prompt when={props.dirty} message={props.promptMessage} />}
      <Form {...props} />
    </React.Fragment>
  );
}

ReduxForm.contextTypes = {
  store: PropTypes.object
};

ReduxForm.propTypes = {
  prompt: PropTypes.bool,
  promptMessage: PropTypes.string
};

ReduxForm.defaultProps = {
  prompt: false,
  promptMessage: 'Вы уверены, что хотите уйти?'
};

ReduxForm.Field = ReduxField;

export default compose(
  getContext({
    state: PropTypes.object
  }),
  withProps(props => ({
    ...createValidator(props.validation, props.form, props.state)
  })),
  reduxForm({
    destroyOnUnmount: true,
    enableReinitialize: true
  })
)(ReduxForm);
