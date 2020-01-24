import omit from 'lodash/omit';
import mapValues from 'lodash/mapValues';
import isEmpty from 'lodash/isEmpty';
import get from 'lodash/get';
import isNaN from 'lodash/isNaN';
import merge from 'deepmerge';
import {
  REGISTER,
  DATA_REQUEST,
  DATA_SUCCESS,
  DATA_FAIL,
  RESOLVE,
  REMOVE,
  SHOW,
  HIDE,
  ENABLE,
  DISABLE,
  LOADING,
  UNLOADING,
  SORT_BY,
  CHANGE_COUNT,
  CHANGE_SIZE,
  CHANGE_PAGE,
  CHANGE_FILTERS_VISIBILITY,
  TOGGLE_FILTERS_VISIBILITY,
  RESET_STATE,
  SET_TABLE_SELECTED_ID,
  SET_ACTIVE,
  DISABLE_ON_FETCH,
} from '../constants/widgets';

/**
 * Описание объекта состояния виджета в хранилище
 * @type {Object}
 * @property {boolean} isInit - инициализирован ли виджета
 * @property {boolean} isEnabled - доступен ли виджет
 * @property {boolean} isVisible - виден ли виджет
 * @property {boolean} isLoading - состояние загрузки
 * @property {string} selectedId - id выбраной записи в виджете
 * @property {number} size - размер выборки
 * @property {number} page - страница выборки
 */
export const widgetState = {
  isInit: false,
  isEnabled: true,
  isVisible: true,
  isLoading: false,
  isResolved: false,
  selectedId: null,
  isFilterVisible: true,
  isActive: false,
  type: null,
  dataProvider: {},
  filter: {
    key: null,
    type: null,
  },
  /* Query props */
  count: 0,
  /* System props */
  pageId: null,
  containerId: null,
  validation: {},
  error: null,
};

function resolve(state = widgetState, action) {
  switch (action.type) {
    case DATA_REQUEST:
      return Object.assign({}, state, {
        isLoading: true,
      });
    case DATA_SUCCESS:
      return Object.assign({}, state, {
        isLoading: false,
      });
    case DATA_FAIL:
      return Object.assign({}, state, {
        isLoading: false,
        error: get(action, 'payload.err', true),
      });
    case RESOLVE:
      return Object.assign({}, state, {
        isResolved: true,
      });
    case SHOW:
      return Object.assign({}, state, {
        isVisible: true,
      });
    case HIDE:
      return Object.assign({}, state, {
        isVisible: false,
      });
    case ENABLE:
      return Object.assign({}, state, {
        isEnabled: true,
      });
    case DISABLE:
    case DISABLE_ON_FETCH:
      return Object.assign({}, state, {
        isEnabled: false,
      });
    case LOADING:
      return Object.assign({}, state, {
        isLoading: true,
      });
    case UNLOADING:
      return Object.assign({}, state, {
        isLoading: false,
      });
    case SORT_BY:
      const sortingKey = get(action, 'payload.sortParam', 'sorting');

      if (action.payload.sortDirection == 'NONE') {
        return {
          ...state,
          [sortingKey]: {},
        };
      }
      return {
        ...state,
        [sortingKey]: {
          [action.payload.fieldKey]: action.payload.sortDirection,
        },
      };
    case CHANGE_SIZE:
      return Object.assign({}, state, {
        size: action.payload.size,
      });
    case CHANGE_PAGE:
      return Object.assign({}, state, {
        page: action.payload.page,
      });
    case CHANGE_COUNT:
      return Object.assign({}, state, {
        count: action.payload.count,
      });
    case CHANGE_FILTERS_VISIBILITY:
      return Object.assign({}, state, {
        isFilterVisible: action.payload.isFilterVisible,
      });
    case TOGGLE_FILTERS_VISIBILITY:
      return Object.assign({}, state, {
        isFilterVisible: !state.isFilterVisible,
      });
    case RESET_STATE:
      return Object.assign({}, state, { isInit: false });
    case SET_TABLE_SELECTED_ID:
      return Object.assign({}, state, {
        selectedId: resolveSelectedId(action.payload.value),
      });
    case SET_ACTIVE:
      return Object.assign({}, state, { isActive: true });
    default:
      return state;
  }
}

function resolveSelectedId(selectedId) {
  if (selectedId !== '' && !isNaN(+selectedId)) {
    selectedId = +selectedId;
  }

  return selectedId;
}

/**
 * Редюсер виджета
 * @ignore
 */
export default function widgets(state = {}, action) {
  switch (action.type) {
    case REGISTER:
      let smartState = {};
      const sortParam = get(action, 'payload.sortParam', 'sorting');
      const currentState = state[action.payload.widgetId] || {};

      if (!isEmpty(currentState)) {
        smartState = {
          selectedId: currentState.selectedId ? currentState.selectedId : null,
        };

        if (!isEmpty(currentState[sortParam])) {
          smartState[sortParam] = currentState[sortParam];
        }
      }
      return {
        ...state,
        [action.payload.widgetId]: {
          ...merge.all([widgetState, currentState, action.payload.initProps]),
          ...smartState,
          isInit: true,
          type: action.payload.initProps.type,
        },
      };
      break;
    case DATA_REQUEST:
    case DATA_SUCCESS:
    case DATA_FAIL:
    case RESOLVE:
    case DISABLE:
    case DISABLE_ON_FETCH:
    case ENABLE:
    case SHOW:
    case HIDE:
    case LOADING:
    case UNLOADING:
    case SORT_BY:
    case CHANGE_SIZE:
    case CHANGE_COUNT:
    case CHANGE_PAGE:
    case CHANGE_FILTERS_VISIBILITY:
    case TOGGLE_FILTERS_VISIBILITY:
    case SET_TABLE_SELECTED_ID:
    case RESET_STATE:
      return Object.assign({}, state, {
        [action.payload.widgetId]: resolve(
          state[action.payload.widgetId],
          action
        ),
      });
      break;
    case SET_ACTIVE:
      return Object.assign(
        {},
        mapValues(state, value => ({ ...value, isActive: false })),
        {
          [action.payload.widgetId]: resolve(
            state[action.payload.widgetId],
            action
          ),
        }
      );
      break;
    case REMOVE:
      return omit(state, action.payload.widgetId);
      break;
    default:
      return state;
  }
}
