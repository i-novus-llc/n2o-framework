import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';

import widgets from './reducers/widgets';
import models from './reducers/models';
import global from './reducers/global';
import pages from './reducers/pages';
import alerts from './reducers/alerts';
import modals from './reducers/modals';
import columns from './reducers/columns';
import toolbar from './reducers/toolbar';
import formPlugin from './reducers/formPlugin';
import user from './reducers/auth';

const formHack = (state, action) => {
  return action.meta && action.meta.form
    ? formReducer.plugin({
        [action.meta.form]: (state, action) => {
          return Object.assign({}, state, formPlugin(state, action));
        }
      })(state, action)
    : formReducer(state, action);
};
const rootReducer = combineReducers({
  form: formHack,
  widgets,
  models,
  global,
  pages,
  alerts,
  modals,
  columns,
  toolbar,
  user
});

export default rootReducer;
