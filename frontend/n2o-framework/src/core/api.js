import _ from 'lodash';
import flatten from 'flat';
import queryString from 'query-string';
import request from '../utils/request';
import { generateFlatQuery } from '../tools/helpers';

export const API_PREFIX = 'n2o';
export const BASE_PATH_METADATA = '/page';
export const BASE_PATH_DATA = '/data/';
export const BASE_PATH_VALIDATION = '/validation';
export const BASE_PATH_CONFIG = '/config';

export const FETCH_APP_CONFIG = 'FETCH_APP_CONFIG';
export const FETCH_PAGE_METADATA = 'FETCH_PAGE_METADATA';
export const FETCH_WIDGET_DATA = 'FETCH_WIDGET_DATA';
export const FETCH_INVOKE_DATA = 'FETCH_INVOKE_DATA';
export const FETCH_VALIDATE = 'FETCH_VALIDATE';

function clearEmptyParams(obj) {
  return _(obj)
    .pickBy(_.identity)
    .pickBy(_.isObject)
    .omitBy(_.isEmpty)
    .assign(
      _(obj)
        .pickBy(_.identity)
        .omitBy(_.isObject)
        .value()
    )
    .value();
}

export default function apiProvider(type, options) {
  switch (type) {
    case FETCH_APP_CONFIG:
      return request(
        [
          API_PREFIX,
          BASE_PATH_CONFIG,
          '?',
          queryString.stringify(
            flatten(clearEmptyParams(options), { safe: true })
          ),
        ].join('')
      );
    case FETCH_PAGE_METADATA:
      return request(
        [API_PREFIX, BASE_PATH_METADATA, options.pageUrl].join('')
      );
    case FETCH_WIDGET_DATA:
      return request(
        [
          options.basePath,
          '?',
          queryString.stringify(
            flatten(clearEmptyParams(options.baseQuery), { safe: true })
          ),
        ].join('')
      );
    case FETCH_INVOKE_DATA:
      return request(
        [
          options.basePath,
          '?',
          queryString.stringify(
            flatten(clearEmptyParams(options.baseQuery), { safe: true })
          ),
        ].join(''),
        {
          method: options.baseMethod || 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(options.model || {}),
        }
      );
    case FETCH_VALIDATE:
      return request(
        [
          API_PREFIX,
          BASE_PATH_VALIDATION,
          '?',
          queryString.stringify(
            flatten(clearEmptyParams(options), { safe: true })
          ),
        ].join('')
      );
    default:
      return Promise.reject('Неверно задан тип для apiProvider!');
  }
}

export function fetchInputSelectData(
  options,
  model,
  settings = { apiPrefix: API_PREFIX, basePath: BASE_PATH_DATA }
) {
  return request(
    [
      settings.apiPrefix,
      settings.basePath,
      generateFlatQuery(options, '', {}, '.'),
    ].join('')
  );
}
