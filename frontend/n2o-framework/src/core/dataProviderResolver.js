import pathToRegexp from 'path-to-regexp';
import urlParse from 'url-parse';
import queryString from 'query-string';
import isEmpty from 'lodash/isEmpty';
import includes from 'lodash/includes';
import each from 'lodash/each';
import isNil from 'lodash/isNil';

import linkResolver from '../utils/linkResolver';

/**
 * Получение basePath и baseQuery
 * @param state
 * @param dataProvider
 * @param query
 * @param options
 * @returns
 */
export function dataProviderResolver(state, dataProvider, query, options) {
  const { url, pathMapping, queryMapping } = dataProvider;
  const { origin, pathname } = urlParse(url);
  const pathParams = getParams(pathMapping, state);
  let basePath = pathToRegexp.compile(pathname)(pathParams);
  const queryParams = getParams(queryMapping, state);
  const baseQuery = Object.assign({}, query, options, queryParams);
  let compiledUrl = basePath;

  if (!isEmpty(queryParams) || !isEmpty(query)) {
    compiledUrl = `${compiledUrl}?${queryString.stringify({
      ...queryParams,
      ...query,
    })}`;
  }

  if (includes(url, origin)) {
    compiledUrl = origin + compiledUrl;
    basePath = origin + basePath;
  }

  return {
    basePath,
    baseQuery,
    url: compiledUrl,
  };
}

export function getParams(mapping, state) {
  const params = {};
  each(mapping, (options, key) => {
    const value = linkResolver(state, options);
    params[key] = !isNil(value) ? value : undefined;
  });
  return params;
}
