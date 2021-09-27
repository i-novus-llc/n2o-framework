import { createStore, applyMiddleware, compose } from 'redux'
import thunkMiddleware from 'redux-thunk'
import createSagaMiddleware from 'redux-saga'
import { createLogger } from 'redux-logger'
import { batchDispatchMiddleware } from 'redux-batched-actions'
import { routerMiddleware } from 'connected-react-router'

import generateReducer from './reducers'
import generateSagas from './sagas'

const sagaMiddleware = createSagaMiddleware()

export default function configureStore(initialState, history, config = {}) {
    const [squasherMiddleware, squasherStorePatcher] = createSquasher([
        '@@redux-form/CHANGE',
        '@@redux-form/INITIALIZE',
        'n2o/models/SET',
        'n2o/widgets/CHANGE_PAGE',
        'n2o/widgets/DATA_SUCCESS',
        'n2o/widgets/RESOLVE',
        // Странный баг, что дровер рендерится, когда его уже быть не должно. Нужно синхронно обрабатывать этот экшен
        'n2o/overlays/DESTROY',
        'n2o/overlays/DESTROY_OVERLAYS',
    ])

    const middlewares = [
        batchDispatchMiddleware,
        thunkMiddleware,
        sagaMiddleware,
        routerMiddleware(history),
        squasherMiddleware,
    ]
    let composeEnhancers = compose

    if (process.env.NODE_ENV === 'development') {
        const loggerMiddleware = createLogger()

        middlewares.push(loggerMiddleware)
    }

    if (
        typeof window === 'object' &&
        window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ &&
        window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    ) {
        composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({})
    }

    const enhancers = [applyMiddleware(...middlewares)]

    const store = createStore(
        generateReducer(history, config.customReducers),
        initialState,
        composeEnhancers(...enhancers),
    )

    squasherStorePatcher(store)

    sagaMiddleware.run(generateSagas(store.dispatch, config))

    return store
}

/**
 * По умолчанию, redux синхронно вызывает своих слушателей (connect`ы) для каждого экшена,
 * соответственно, при большом количестве синхронных экшенов мы получаем множество синхронных ререндеров
 * СКОРЕЕ ВСЕГО промежуточные ререндеры не нужны. Исходя из этой вводной мы можем схлопнуть почти все
 * синхронные экшены, а экшены, после которых обязательно нужен ререндер - передаём параметром
 */
function createSquasher(nonSquashedActions = []) {
    // Накапливать ли изменения синхронно поступающих экшенов
    let squashing = true

    // Таймер для отложенного вызова подписчиков стора
    let timer = null

    // Колбэки подписчиков
    const listeners = []

    nonSquashedActions = nonSquashedActions.reduce((nonSquashedActions, action) => {
        nonSquashedActions[action] = true

        return nonSquashedActions
    }, {})

    return [
        () => next => (action) => {
            const { type } = action

            squashing = true

            if (type && nonSquashedActions[type]) {
                squashing = false
            }

            return next(action)
        },

        function storePatcher(store) {
            // Первыми подписываемся на стор. Что-бы больше ни кто напрямую не смог подписаться - переопределяем
            // встроенный метод подписки.
            store.subscribe(() => {
                if (squashing) {
                    if (timer) {
                        return
                    }

                    timer = setTimeout(callListeners, 0)
                } else {
                    callListeners()
                }

                function callListeners() {
                    const currentListeners = [...listeners]

                    clearTimeout(timer)
                    timer = null

                    for (let i = 0; i < currentListeners.length; i++) {
                        const listener = currentListeners[i]

                        listener()
                    }
                }
            })

            // Заменяем стандартный метод подписки на изменения, чтоб самим контролировать когда дергать подписчиков
            store.subscribe = (listener) => {
                let isSubscribed = true

                listeners.push(listener)

                return function unsubscribe() {
                    if (!isSubscribed) {
                        return
                    }

                    isSubscribed = false

                    const index = listeners.indexOf(listener)

                    listeners.splice(index, 1)
                }
            }

            return store
        },
    ]
}
