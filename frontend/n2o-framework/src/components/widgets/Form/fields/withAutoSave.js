import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers, mapProps, getContext } from 'recompose';
import pathToRegexp from 'path-to-regexp';

import { saveFieldData } from '../../../../core/api';
import { getParams } from '../../../../utils/compileUrl';

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
      resolveDataProvider: ({ store, dataProvider }) => () => {
        const { url, pathMapping, queryMapping } = dataProvider;
        const mapping = mapping => getParams(mapping, store.getState());
        const pathParams = mapping(pathMapping);
        const queryParams = mapping(queryMapping);
        const basePath = pathToRegexp.compile(url)(pathParams);

        return {
          basePath,
          pathParams,
          queryParams,
        };
      },
    }),
    withHandlers({
      onChange: ({ resolveDataProvider, input }) => eventOrValue => {
        input.onChange(eventOrValue);
      },
      onBlur: ({ input }) => eventOrValue => input.onBlur(eventOrValue),
    }),
    mapProps(({ input, onChange, onBlur, ...rest }) =>
      Object.assign(
        {},
        {
          input: Object.assign({}, input, { onChange, onBlur }),
        },
        rest
      )
    )
  );

  return enhance(hocComponent);
}
