import { createStore, applyMiddleware } from 'redux';
import thunkMiddleware from 'redux-thunk';
import createSagaMiddleware from 'redux-saga';
import { createLogger } from 'redux-logger';
import { batchDispatchMiddleware } from 'redux-batched-actions';
import { composeWithDevTools } from 'redux-devtools-extension';
import { routerMiddleware } from 'connected-react-router';

import generateReducer from './reducers';
import generateSagas from './sagas';

const loggerMiddleware = createLogger();
const sagaMiddleware = createSagaMiddleware();

export default function configureStore(initialState, history, config = {}) {
  const store = createStore(
    generateReducer(history, config.customReducers),
    initialState,
    composeWithDevTools(
      applyMiddleware(
        batchDispatchMiddleware,
        thunkMiddleware,
        sagaMiddleware,
        loggerMiddleware,
        routerMiddleware(history)
      )
    )
  );
  sagaMiddleware.run(generateSagas(store.dispatch, config));
  return store;
}
