import { handleActions } from "redux-actions";
import { ADD, DESTROY, CLEAR_ALL, SET_COUNTER } from "./actions";

const defaultState = {
  stack: [],
  counters: {}
};

export default handleActions(
  {
    [ADD]: (state, action) => ({
      ...state,
      stack: state.stack.concat(action.payload)
    }),
    [DESTROY]: (state, action) => ({
      ...state,
      stack: state.stack.filter(item => item.id !== action.payload.id)
    }),
    [CLEAR_ALL]: (state, action) => ({
      ...state,
      stack: []
    }),
    [SET_COUNTER]: (state, action) => ({
      ...state,
      counters: {
        ...state.counters,
        [action.payload.counterId]: action.payload.value
      }
    })
  },
  defaultState
);
