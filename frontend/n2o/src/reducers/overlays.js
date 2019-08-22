import _ from 'lodash';
import {
  INSERT,
  DESTROY,
  HIDE,
  SHOW,
  SHOW_PROMPT,
  HIDE_PROMPT,
} from '../constants/overlays';

const defaultState = {
  visible: false,
  name: null,
  showPrompt: false,
  mod: 'modal',
  props: {
    title: null,
    closeButton: null,
    pageId: null,
    size: 'lg',
    src: null,
  },
};

function resolve(state = defaultState, action) {
  switch (action.type) {
    case INSERT:
      const { visible, name, mod, ...props } = action.payload;
      return Object.assign({}, state, {
        visible,
        name,
        mod,
        props: Object.assign({}, props),
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
 * Редюсер экшенов модалок
 */
export default function overlays(state = [], action) {
  const index = state.findIndex(
    modal => modal.name === _.get(action, 'payload.name')
  );
  switch (action.type) {
    case INSERT:
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
      return state.slice(0, -1);
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
