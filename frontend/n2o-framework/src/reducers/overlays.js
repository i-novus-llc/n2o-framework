import get from 'lodash/get';
import {
  INSERT_MODAL,
  INSERT_DRAWER,
  INSERT_DIALOG,
  DESTROY,
  DESTROY_OVERLAYS,
  HIDE,
  SHOW,
  SHOW_PROMPT,
  HIDE_PROMPT,
} from '../constants/overlays';

const defaultState = {
  visible: false,
  name: null,
  showPrompt: false,
  mode: 'modal',
  props: {},
};

function resolve(state = defaultState, action) {
  switch (action.type) {
    case INSERT_MODAL:
      return Object.assign({}, state, {
        visible: action.payload.visible,
        name: action.payload.name,
        mode: 'modal',
        props: Object.assign({}, action.payload),
      });
    case INSERT_DRAWER:
      return Object.assign({}, state, {
        visible: action.payload.visible,
        name: action.payload.name,
        mode: 'drawer',
        props: Object.assign({}, action.payload),
      });
    case INSERT_DIALOG:
      return Object.assign({}, state, {
        visible: action.payload.visible,
        name: action.payload.name,
        mode: 'dialog',
        props: Object.assign({}, action.payload),
      });
    case SHOW:
      return Object.assign({}, state, {
        visible: true,
      });
    case HIDE:
      return Object.assign({}, state, {
        visible: false,
      });
    default:
      return state;
  }
}

/**
 * Редюсер экшенов оверлеев
 */
export default function overlays(state = [], action) {
  const index = state.findIndex(
    overlay => overlay.name === get(action, 'payload.name')
  );
  const indexByMode = state.findIndex(
    ({ mode }) => mode === get(action, 'payload.mode')
  );
  switch (action.type) {
    case INSERT_MODAL:
    case INSERT_DRAWER:
    case INSERT_DIALOG:
      return [...state, resolve({}, action)];
    case SHOW:
      if (index >= 0) {
        state[index].visible = true;
        return state.slice();
      }
      return state;
    case HIDE:
      if (index >= 0) {
        state[index].visible = false;
        return state.slice();
      }
      return state;
    case DESTROY:
      if (indexByMode >= 0 && get(action, 'payload.mode')) {
        return state.splice(indexByMode, 1);
      } else if (!get(action, 'payload.mode')) return state.slice(0, -1);
      else return state;
    case DESTROY_OVERLAYS:
      return state.slice(0, -action.payload.count);
    case SHOW_PROMPT:
      state[index].showPrompt = true;
      return state.slice();
    case HIDE_PROMPT:
      state[index].showPrompt = false;
      return state.slice();
    default:
      return state;
  }
}
