import React from 'react';
import PropTypes from 'prop-types';
import { getFormValues } from 'redux-form';
import { compose, withHandlers, mapProps, getContext } from 'recompose';
import has from 'lodash/has';

import { startInvoke } from '../../../../actions/actionImpl';

export default function withAutoSave(WrappedComponent) {
  let timeoutId = null;

  function hocComponent(props) {
    return <WrappedComponent {...props} />;
  }

  hocComponent.propTypes = {};

  const enhance = compose(
    getContext({
      store: PropTypes.object,
    }),
    withHandlers({
      parseValue: () => eventOrValue => {
        if (has(eventOrValue, 'target')) {
          return eventOrValue.target.value;
        }

        return eventOrValue;
      },
      prepareData: ({ autoSubmit, input, store }) => (form, value) => {
        if (autoSubmit) {
          const model = getFormValues(form)(store.getState());

          return {
            ...model,
            [input.name]: value,
          };
        }

        return {
          data: value,
        };
      },
    }),
    withHandlers({
      onBlur: ({
        parseValue,
        prepareData,
        input,
        store,
        autoSubmit,
        dataProvider,
        meta = {},
      }) => eventOrValue => {
        const value = parseValue(eventOrValue);
        const form = meta.form;
        const data = prepareData(form, value);

        input.onBlur(eventOrValue);
        store.dispatch(
          startInvoke(form, autoSubmit || dataProvider, data, null, {}, false)
        );
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

  return enhance(hocComponent);
}
