import set from 'lodash/set';

import {
  METADATA_REQUEST,
  METADATA_SUCCESS,
  METADATA_FAIL,
  RESET,
  ENABLE,
  DISABLE,
  SET_STATUS,
} from '../constants/pages';
import { SET_WIDGET_METADATA } from '../constants/widgets';

/**
 * Описание объекта состояния страницы в хранилище
 * @type {Object}
 * @property {object} metadata - JSON метаданных
 * @property {boolean} loading - состоания загрузки метаданных
 * @property {string} timestamp - дата и время актуальной версии метаданных
 */
export const pageState = {
  metadata: {},
  loading: false,
  error: false,
  disabled: false,
  status: null,
};

function resolve(state = pageState, action) {
  switch (action.type) {
    case RESET:
      return Object.assign({}, pageState);
    case METADATA_REQUEST:
      return Object.assign({}, state, {
        loading: true,
        error: false,
        metadata: {},
      });
    case METADATA_SUCCESS:
      return Object.assign({}, state, {
        loading: false,
        error: false,
        metadata: action.payload.json,
      });
    case METADATA_FAIL:
      return Object.assign({}, state, {
        error: action.payload.err,
        loading: false,
      });
    case SET_WIDGET_METADATA:
      return set(
        state,
        ['metadata', 'widgets', action.payload.widgetId],
        action.payload.metadata
      );
    case ENABLE:
      return Object.assign({}, state, {
        disabled: false,
      });
    case DISABLE:
      return Object.assign({}, state, {
        disabled: true,
      });
    case SET_STATUS:
      return Object.assign({}, state, {
        status: action.payload.status,
      });
    default:
      return state;
  }
}

/** Редюсей для страниц
 * @ignore
 */
export default function pages(state = {}, action) {
  switch (action.type) {
    case METADATA_REQUEST:
    case METADATA_SUCCESS:
    case METADATA_FAIL:
    case SET_WIDGET_METADATA:
    case RESET:
    case ENABLE:
    case DISABLE:
    case SET_STATUS:
      return Object.assign({}, state, {
        [action.payload.pageId]: resolve(state[action.payload.pageId], action),
      });
    default:
      return state;
  }
}
