import {
  compose,
  withHandlers,
  withProps,
  onlyUpdateForKeys,
  withState,
  lifecycle,
  defaultProps,
  withPropsOnChange,
  mapProps
} from 'recompose';
import { isEmpty, isEqual } from 'lodash';
import merge from 'deepmerge';
import { getFormValues } from 'redux-form';
import { createStructuredSelector } from 'reselect';
import { connect } from 'react-redux';

import ReduxForm from './ReduxForm';
import widgetContainer from '../WidgetContainer';
import { FORM } from '../widgetTypes';

/**
 * Обертка в widgetContainer, мэппинг пропсов
 */

export default compose(
  widgetContainer(
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
          modelPrefix: props.modelPrefix
        };
      }
    },
    FORM
  ),
  withProps(props => ({
    form: props.widgetId
  })),
  connect(
    createStructuredSelector({
      reduxFormValues: (state, props) => getFormValues(props.form)(state) || {}
    })
  ),
  withState('defaultValues', 'setDefaultValues', null),
  lifecycle({
    componentDidUpdate(prevProps) {
      const {
        datasource,
        activeModel,
        defaultValues,
        reduxFormValues,
        setDefaultValues
      } = this.props;
      if (
        !isEqual(prevProps.activeModel, activeModel) &&
        !isEqual(activeModel, defaultValues) &&
        !isEqual(activeModel, reduxFormValues)
      ) {
        setDefaultValues(activeModel);
      } else if (!isEmpty(defaultValues) && !isEqual(prevProps.datasource, datasource)) {
        setDefaultValues(null);
      }
    }
  }),
  withPropsOnChange(
    (props, nextProps) => {
      return (
        !isEqual(props.isEnabled, nextProps.isEnabled) ||
        !isEqual(props.defaultValues, nextProps.defaultValues) ||
        !isEqual(props.datasource, nextProps.datasource)
      );
    },
    props => ({
      initialValues: props.isEnabled
        ? props.defaultValues
          ? props.defaultValues
          : merge(props.resolveModel || {}, props.datasource || {})
        : {}
    })
  ),
  withHandlers({
    onChange: props => values => {
      if (props.modelPrefix && isEqual(props.initialValues, props.reduxFormValues)) {
        props.onResolve(props.initialValues);
      }
      if (isEmpty(values) || !props.modelPrefix) {
        props.onResolve(values);
      } else {
        props.onSetModel(values);
      }
    }
  }),
  onlyUpdateForKeys(['initialValues'])
)(ReduxForm);
