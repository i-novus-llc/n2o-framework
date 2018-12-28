import { each, isEmpty } from 'lodash';
import pathToRegexp from 'path-to-regexp';
import queryString from 'query-string';
import linkResolver from './linkResolver';

export function getParams(mapping, state) {
  const params = {};
  each(mapping, (options, key) => {
    params[key] = linkResolver(state, options);
  });
  return params;
}

export default function compileUrl(
  url,
  { pathMapping = {}, queryMapping = {} } = {},
  state,
  { extraPathParams = {}, extraQueryParams = {} } = {}
) {
  const pathParams = getParams(pathMapping, state);
  const queryParams = getParams(queryMapping, state);
  if (!isEmpty(pathParams)) {
    if (!isEmpty(queryParams)) {
      return `${pathToRegexp.compile(url)({
        ...pathParams,
        ...extraPathParams
      })}?${queryString.stringify({ ...queryParams, ...extraQueryParams })}`;
    } else {
      return pathToRegexp.compile(url)(pathParams);
    }
  }
  return url;
}
