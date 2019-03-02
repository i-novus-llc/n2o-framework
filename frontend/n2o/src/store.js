import { createStore, applyMiddleware } from 'redux';
import thunkMiddleware from 'redux-thunk';
import createSagaMiddleware from 'redux-saga';
import { createLogger } from 'redux-logger';
import { batchDispatchMiddleware } from 'redux-batched-actions';
import { composeWithDevTools } from 'redux-devtools-extension';
import { connectRouter, routerMiddleware } from 'connected-react-router';

import rootReducer from './reducers';
import generateSagas from './sagas';

const loggerMiddleware = createLogger();
const sagaMiddleware = createSagaMiddleware();

export default function configureStore(initialState, history, config) {
  const store = createStore(
    connectRouter(history)(rootReducer),
    initialState,
    composeWithDevTools(
      applyMiddleware(
        batchDispatchMiddleware,
        thunkMiddleware,
        sagaMiddleware,
        routerMiddleware(history)
      )
    )
  );
  sagaMiddleware.run(generateSagas(store.dispatch, config));
  return store;
}
