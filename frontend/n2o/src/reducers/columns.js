import { omit, map, filter, mapValues } from 'lodash';
import {
  CHANGE_COLUMN_DISABLED,
  CHANGE_COLUMN_VISIBILITY,
  REGISTER_COLUMN,
  TOGGLE_COLUMN_VISIBILITY
} from '../constants/columns';
import { generateKey } from '../utils/id';
import { RESET_STATE } from '../constants/widgets';
import { buttonState } from './toolbar';

export const columnState = {
  isInit: true,
  isVisible: true,
  isDisabled: false
};

function resolve(state = columnState, action) {
  switch (action.type) {
    case CHANGE_COLUMN_VISIBILITY:
      return Object.assign({}, state, {
        isVisible: action.payload.visible
      });
    case CHANGE_COLUMN_DISABLED:
      return Object.assign({}, state, {
        isDisabled: action.payload.disabled
      });
    case TOGGLE_COLUMN_VISIBILITY:
      return Object.assign({}, state, {
        isVisible: !state.isVisible
      });
    case RESET_STATE:
      return Object.assign({}, state, { isInit: false });
    default:
      return state;
  }
}

/**
 * Редюсер колонок
 * @ignore
 */
export default function columns(state = {}, action) {
  const { key, columnId } = action.payload || {};
  switch (action.type) {
    case REGISTER_COLUMN:
      return Object.assign({}, state, {
        [key]: { ...state[key], [columnId]: Object.assign({}, columnState, action.payload) }
      });
    case CHANGE_COLUMN_VISIBILITY:
    case CHANGE_COLUMN_DISABLED:
    case TOGGLE_COLUMN_VISIBILITY:
      return Object.assign({}, state, {
        [key]: {
          ...state[key],
          [columnId]: resolve(state[key][columnId], action)
        }
      });
    case RESET_STATE:
      const { widgetId } = action.payload;
      return {
        ...state,
        [widgetId]: mapValues(state[widgetId], (column, columnId) =>
          resolve(state[widgetId][columnId], action)
        )
      };
    default:
      return state;
  }
}
