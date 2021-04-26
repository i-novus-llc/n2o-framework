import { createStore, applyMiddleware, compose } from 'redux'
import thunkMiddleware from 'redux-thunk'
import createSagaMiddleware from 'redux-saga'
import { createLogger } from 'redux-logger'
import { batchDispatchMiddleware } from 'redux-batched-actions'
import { routerMiddleware } from 'connected-react-router'

import generateReducer from './reducers'
import generateSagas from './sagas'

const loggerMiddleware = createLogger()
const sagaMiddleware = createSagaMiddleware()

export default function configureStore(initialState, history, config = {}) {
    const middlewares = [
        batchDispatchMiddleware,
        thunkMiddleware,
        sagaMiddleware,
        routerMiddleware(history),
    ]
    let composeEnhancers = compose

    if (process.env.NODE_ENV === 'development') {
        middlewares.push(loggerMiddleware)
    }

    if (
        typeof window === 'object' &&
    window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    ) {
    /* eslint-disable no-underscore-dangle */
        if (window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__) { composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({}) }
    /* eslint-enable */
    }

    const enhancers = [applyMiddleware(...middlewares)]

    const store = createStore(
        generateReducer(history, config.customReducers),
        initialState,
        composeEnhancers(...enhancers),
    )
    sagaMiddleware.run(generateSagas(store.dispatch, config))
    return store
}
