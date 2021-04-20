import produce from 'immer';
import has from 'lodash/has';
import set from 'lodash/set';
import merge from 'deepmerge';
import { actionTypes } from 'redux-form';

import {
  DISABLE_FIELD,
  ENABLE_FIELD,
  SHOW_FIELD,
  HIDE_FIELD,
  REGISTER_FIELD_EXTRA,
  ADD_FIELD_MESSAGE,
  REMOVE_FIELD_MESSAGE,
  REGISTER_DEPENDENCY,
  SET_FIELD_FILTER,
  SHOW_FIELDS,
  HIDE_FIELDS,
  ENABLE_FIELDS,
  DISABLE_FIELDS,
  SET_REQUIRED,
  UNSET_REQUIRED,
  SET_LOADING,
} from '../constants/formPlugin';

const ACTION_TYPES = [
  DISABLE_FIELD,
  ENABLE_FIELD,
  SHOW_FIELD,
  HIDE_FIELD,
  REGISTER_FIELD_EXTRA,
  ADD_FIELD_MESSAGE,
  REMOVE_FIELD_MESSAGE,
  REGISTER_DEPENDENCY,
  SET_FIELD_FILTER,
  SHOW_FIELDS,
  HIDE_FIELDS,
  ENABLE_FIELDS,
  DISABLE_FIELDS,
  SET_REQUIRED,
  UNSET_REQUIRED,
  SET_LOADING,
];

const defaultState = {
  isInit: true,
  visible: true,
  disabled: false,
  message: null,
  filter: [],
  dependency: null,
  required: false,
  loading: false,
};

/**
 * Редюсер удаления/добваления алертов
 * @ignore
 */
const formPlugin = produce((state, { type, payload, meta }) => {
  if (ACTION_TYPES.includes(type)) {
    state.registeredFields = state.registeredFields || {};
    state.registeredFields[payload.name] =
      state.registeredFields[payload.name] || {};
  }
  switch (type) {
    case REGISTER_FIELD_EXTRA: {
      const initialState = merge(defaultState, payload.initialState || {});

      Object.assign(state.registeredFields[payload.name], initialState);
      break;
    }

    case DISABLE_FIELD:
      state.registeredFields[payload.name].disabled = true;
      break;

    case ENABLE_FIELD:
      state.registeredFields[payload.name].disabled = false;
      break;

    case SHOW_FIELD:
      state.registeredFields[payload.name].visible = true;
      break;

    case HIDE_FIELD:
      state.registeredFields[payload.name].visible = false;
      break;

    case ADD_FIELD_MESSAGE:
      state.registeredFields[payload.name].message = state.registeredFields[payload.name].message || {};
      Object.assign(state.registeredFields[payload.name].message, payload.message);
      break;

    case REMOVE_FIELD_MESSAGE:
      state.registeredFields[payload.name].message = null;
      break;

    case REGISTER_DEPENDENCY:
      state.registeredFields[payload.name].dependency = payload.dependency;
      break;

    case SET_FIELD_FILTER:
      state.registeredFields[payload.name].filter =
        state.registeredFields[payload.name].filter
          .filter(f => f.filterId !== payload.filter.filterId)
          .concat(payload.filter);
      break;

    case SET_REQUIRED:
      state.registeredFields[payload.name].required = true;
      break;

    case UNSET_REQUIRED:
      state.registeredFields[payload.name].required = false;
      break;

    case SET_LOADING:
      state.registeredFields[payload.name].loading = payload.loading;
      break;

    case SHOW_FIELDS:
      payload.names.forEach(name => {
        state.registeredFields[name].visible = true;
      });
      break;

    case HIDE_FIELDS:
      payload.names.forEach(name => {
        state.registeredFields[name].visible = false;
      });
      break;

    case DISABLE_FIELDS:
      payload.names.forEach(name => {
        state.registeredFields[name].disabled = true;
      });
      break;

    case ENABLE_FIELDS:
      payload.names.forEach(name => {
        state.registeredFields[name].disabled = false;
      });
      break;

    case actionTypes.CHANGE: {
      const { field } = meta;
      if (!field) {
        break;
      }
      const customFormAction = has(payload, 'keepDirty');
      const value = customFormAction ? payload.value : payload;

      /*
       * TODO придумать как аккуратно отказаться от _.set
       *  сейчас он раскручивает поля ввида values[field[index].property]
       */
      set(state, `values[${field}]`, value);

      if (customFormAction && !payload.keepDirty) {
        set(state, `initial[${field}]`, payload.value);
      }
      break;
    }
  }
}, defaultState);

export default formPlugin;
