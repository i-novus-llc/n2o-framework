import { each, isEmpty, get } from 'lodash';
import pathToRegexp from 'path-to-regexp';
import queryString from 'query-string';
import linkResolver from './linkResolver';

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
  const domainWithProtocol = get(url.match(/^https?\:\/\/([^\/?#]+)(?:[\/?#]|$)/i), 0);
  const urlForCompile = url.split(domainWithProtocol);
  const pathParams = getParams(pathMapping, state);
  const queryParams = getParams(queryMapping, state);
  let compiledUrl = domainWithProtocol && get(urlForCompile, '[1]') ? urlForCompile[1] : url;
  if (!isEmpty(pathParams)) {
    compiledUrl = (domainWithProtocol || '') + pathToRegexp.compile(compiledUrl)({
      ...pathParams,
      ...extraPathParams,
    });
  }
  if (!isEmpty(queryParams)) {
    compiledUrl = (domainWithProtocol || '') + `${compiledUrl}?${queryString.stringify({
      ...queryParams,
      ...extraQueryParams,
    })}`;
  }
  return compiledUrl;
}
