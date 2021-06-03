import thunkMiddleware from 'redux-thunk'
import createSagaMiddleware from 'redux-saga'
import { createLogger } from 'redux-logger'
import { batchDispatchMiddleware } from 'redux-batched-actions'
import { routerMiddleware } from 'connected-react-router'
import { configureStore } from '@reduxjs/toolkit'

import generateReducer from './reducers'
import generateSagas from './sagas'

const loggerMiddleware = createLogger()
const sagaMiddleware = createSagaMiddleware()

export default (initialState, history, config = {}) => {
    const middlewares = [
        batchDispatchMiddleware,
        thunkMiddleware,
        () => next => (action) => {
            let extendedAction = action

            if (Object.prototype.toString.call(action) === '[object Object]') {
                extendedAction = { ...action, meta: action.meta || {} }
            }

            next(extendedAction)
        },
        sagaMiddleware,
        routerMiddleware(history),
    ]

    if (process.env.NODE_ENV === 'development') {
        middlewares.push(loggerMiddleware)
    }

    const store = configureStore({
        reducer: generateReducer(history, config.customReducers),
        preloadedState: initialState,
        middleware: middlewares,
        devTools: true,
    })

    sagaMiddleware.run(generateSagas(store.dispatch, config))

    return store
}
