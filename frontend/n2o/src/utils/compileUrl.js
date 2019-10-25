import { each, isEmpty, includes } from 'lodash';
import pathToRegexp from 'path-to-regexp';
import queryString from 'query-string';
import linkResolver from './linkResolver';
import urlParse from 'url-parse';

export function getParams(mapping, state) {
  const params = {};
  each(mapping, (options, key) => {
    const value = linkResolver(state, options);
    params[key] = value || undefined;
  });
  return params;
}

export default function compileUrl(
  url,
  { pathMapping = {}, queryMapping = {} } = {},
  state,
  { extraPathParams = {}, extraQueryParams = {} } = {}
) {
  const { origin, pathname } = urlParse(url);
  const pathParams = getParams(pathMapping, state);
  const queryParams = getParams(queryMapping, state);
  let compiledUrl = pathname;

  if (!isEmpty(pathParams)) {
    compiledUrl = pathToRegexp.compile(compiledUrl)({
      ...pathParams,
      ...extraPathParams,
    });
  }
  if (!isEmpty(queryParams)) {
    compiledUrl = `${compiledUrl}?${queryString.stringify({
      ...queryParams,
      ...extraQueryParams,
    })}`;
  }

  if (includes(url, origin)) {
    compiledUrl = origin + compiledUrl;
  }

  return compiledUrl;
}
