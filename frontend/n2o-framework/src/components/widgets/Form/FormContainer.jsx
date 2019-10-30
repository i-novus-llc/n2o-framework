import {
  compose,
  withHandlers,
  withProps,
  onlyUpdateForKeys,
  withState,
  lifecycle,
  withPropsOnChange,
  getContext,
} from 'recompose';
import { isEmpty, isEqual } from 'lodash';
import merge from 'deepmerge';
import { getFormValues } from 'redux-form';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import ReduxForm from './ReduxForm';
import widgetContainer from '../WidgetContainer';
import { FORM } from '../widgetTypes';
import { getFieldsKeys } from './utils';
import createValidator from '../../../core/validation/createValidator';
import { PREFIXES } from '../../../constants/models';

const arrayMergeFunction = (destinationArray, sourceArray) => sourceArray;

export const withWidgetContainer = widgetContainer(
  {
    mapProps: props => {
      return {
        widgetId: props.widgetId,
        isEnabled: props.isEnabled,
        pageId: props.pageId,
        autoFocus: props.autoFocus,
        fieldsets: props.fieldsets,
        datasource: props.datasource && props.datasource[0],
        onSetModel: props.onSetModel,
        onResolve: props.onResolve,
        resolveModel: props.resolveModel,
        activeModel: props.activeModel,
        validation: props.validation,
        modelPrefix: props.modelPrefix,
        prompt: props.prompt,
        setActive: props.onFocus,
        placeholder: props.placeholder,
      };
    },
  },
  FORM
);

export const mapStateToProps = createStructuredSelector({
  reduxFormValues: (state, props) => getFormValues(props.form)(state) || {},
});

export const withLiveCycleMethods = lifecycle({
  componentDidUpdate(prevProps) {
    const {
      datasource,
      activeModel,
      defaultValues,
      reduxFormValues,
      setDefaultValues,
      resolveModel,
    } = this.props;
    if (
      !isEqual(prevProps.activeModel, activeModel) &&
      !isEqual(activeModel, defaultValues) &&
      !isEqual(activeModel, reduxFormValues)
    ) {
      setDefaultValues(activeModel);
    } else if (
      (!isEmpty(defaultValues) && !isEqual(prevProps.datasource, datasource)) ||
      (prevProps.datasource && !datasource)
    ) {
      setDefaultValues({});
    } else if (
      isEqual(prevProps.resolveModel, resolveModel) &&
      !isEqual(prevProps.reduxFormValues, reduxFormValues) &&
      isEqual(datasource, resolveModel)
    ) {
      setDefaultValues(reduxFormValues);
    }
  },
});

export const withPropsOnChangeWidget = withPropsOnChange(
  (props, nextProps) => {
    return (
      !isEqual(props.defaultValues, nextProps.defaultValues) ||
      !isEqual(props.datasource, nextProps.datasource)
    );
  },
  props => {
    return {
      initialValues:
        (props.defaultValues && !isEmpty(props.defaultValues)) ||
        (props.defaultValues &&
          (!props.datasource && !isEmpty(props.defaultValues)))
          ? props.defaultValues
          : merge(props.resolveModel || {}, props.datasource || {}, {
              arrayMerge: arrayMergeFunction,
            }),
    };
  }
);

export const withWidgetHandlers = withHandlers({
  onChange: props => (values, dispatch, options, prevValues) => {
    props.setActive && props.setActive();
    if (
      props.modelPrefix &&
      isEqual(props.initialValues, props.reduxFormValues) &&
      !isEqual(props.initialValues, props.resolveModel)
    ) {
      props.onResolve(props.initialValues);
    }

    if (isEmpty(values) || !props.modelPrefix) {
      props.onResolve(values);
    } else if (!isEqual(props.reduxFormValues, prevValues)) {
      props.onSetModel(
        props.modelPrefix || PREFIXES.resolve,
        props.widgetId,
        values
      );
    }
  },
});

/**
 * Обертка в widgetContainer, мэппинг пропсов
 */

export default compose(
  withWidgetContainer,
  getContext({
    store: PropTypes.object,
  }),
  withProps(props => {
    const state = props.store && props.store.getState();
    const fields = getFieldsKeys(props.fieldsets);

    return {
      form: props.widgetId,
      prompt: props.prompt,
      store: props.store,
      fields,
      ...createValidator(props.validation, props.widgetId, state, fields),
      ...props,
    };
  }),
  connect(mapStateToProps),
  withState('defaultValues', 'setDefaultValues', null),
  withLiveCycleMethods,
  withPropsOnChangeWidget,
  withWidgetHandlers,
  onlyUpdateForKeys(['initialValues'])
)(ReduxForm);
