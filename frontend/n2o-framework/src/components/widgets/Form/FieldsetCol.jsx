import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers, pure, getContext, mapProps } from 'recompose';
import { getFormValues } from 'redux-form';
import Col from 'reactstrap/lib/Col';
import get from 'lodash/get';
import ReduxField from './ReduxField';
import FieldsetContainer from './Fieldset';
import evalExpression, { parseExpression } from '../../../utils/evalExpression';

function FieldsetCol({
  col,
  defaultCol,
  colId,
  autoFocusId,
  labelPosition,
  labelWidth,
  labelAlignment,
  modelPrefix,
  form,
  parentName,
  parentIndex,
  colVisible = true,
  disabled,
}) {
  return (
    colVisible && (
      <Col xs={col.size || defaultCol} key={colId} className={col.className}>
        {col.fields &&
          col.fields.map((field, i) => {
            const autoFocus =
              field.id && autoFocusId && field.id === autoFocusId;
            const key = 'field' + i;
            const name = parentName ? `${parentName}.${field.id}` : field.id;

            return (
              <ReduxField
                labelPosition={labelPosition}
                labelWidth={labelWidth}
                labelAlignment={labelAlignment}
                key={key}
                autoFocus={autoFocus}
                form={form}
                modelPrefix={modelPrefix}
                name={name}
                parentName={parentName}
                parentIndex={parentIndex}
                disabled={disabled}
                {...field}
              />
            );
          })}
        {col.fieldsets &&
          col.fieldsets.map((fieldset, i) => {
            const key = `set${i}`;
            return (
              <FieldsetContainer
                modelPrefix={modelPrefix}
                key={key}
                form={form}
                parentName={parentName}
                parentIndex={parentIndex}
                disabled={disabled}
                {...fieldset}
              />
            );
          })}
      </Col>
    )
  );
}

const enhance = compose(
  pure,
  getContext({
    store: PropTypes.object,
  }),
  withHandlers({
    resolveVisible: props => () => {
      const state = props.store.getState();
      let visible = get(props, 'col.visible');
      const expression = parseExpression(visible);
      const model = getFormValues(props.form)(state);

      if (expression) {
        visible = evalExpression(expression, model);
      }

      return visible;
    },
  }),
  mapProps(({ resolveVisible, ...props }) => ({
    ...props,
    colVisible: resolveVisible(),
  }))
);

export { FieldsetCol };
export default enhance(FieldsetCol);
