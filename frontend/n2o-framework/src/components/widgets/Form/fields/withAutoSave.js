import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers, mapProps, getContext } from 'recompose';
import pathToRegexp from 'path-to-regexp';
import has from 'lodash/has';

import { callAlert } from '../../../../actions/meta';
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
        const baseQuery = mapping(queryMapping);
        const basePath = pathToRegexp.compile(url)(pathParams);

        return {
          basePath,
          pathParams,
          baseQuery,
        };
      },
      parseValue: () => eventOrValue => {
        if (has(eventOrValue, 'target')) {
          return eventOrValue.target.value;
        }

        return eventOrValue;
      },
    }),
    withHandlers({
      onChange: ({
        resolveDataProvider,
        parseValue,
        input,
        store,
      }) => eventOrValue => {
        const { basePath, baseQuery } = resolveDataProvider();

        input.onChange(eventOrValue);

        clearTimeout(timeoutId);

        timeoutId = setTimeout(async () => {
          try {
            const { meta } = await saveFieldData(basePath, {
              baseQuery,
              body: { data: parseValue(eventOrValue) },
            });

            store.dispatch(callAlert(meta));
          } catch (e) {
            const { meta } = e.body;

            store.dispatch(callAlert(meta));

            console.error(e);
          }
        }, 400);
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
