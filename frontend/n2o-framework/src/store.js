import thunkMiddleware from 'redux-thunk'
import createSagaMiddleware from 'redux-saga'
import { createLogger } from 'redux-logger'
import { routerMiddleware } from 'connected-react-router'
import { configureStore } from '@reduxjs/toolkit'

import generateReducer from './reducers'
import generateSagas from './sagas'
import { updateModel } from './ducks/models/store'

const sagaMiddleware = createSagaMiddleware()

export default (initialState, history, config = {}) => {
    const [squasherMiddleware, squasherStorePatcher] = createSquasher([
        updateModel.type,
        'n2o/models/SET',
        'n2o/widgets/CHANGE_PAGE',
        'n2o/widgets/DATA_SUCCESS',
        'n2o/widgets/RESOLVE',
        // Странный баг, что дровер рендерится, когда его уже быть не должно. Нужно синхронно обрабатывать этот экшен
        'n2o/overlays/DESTROY',
        'n2o/overlays/DESTROY_OVERLAYS',
    ])

    const middlewares = [
        thunkMiddleware,
        ({ getState }) => (next) => {
            let prevState = {}

            return (action) => {
                let nextAction = action

                if (Object.prototype.toString.call(action) === '[object Object]') {
                    const { payload = {}, meta = {}, ...actionFields } = action

                    nextAction = { ...actionFields, payload, meta: { ...meta, prevState } }
                }

                prevState = getState()

                return next(nextAction)
            }
        },
        sagaMiddleware,
        routerMiddleware(history),
        squasherMiddleware,
    ]

    if (process.env.NODE_ENV === 'development') {
        const loggerMiddleware = createLogger()

        middlewares.push(loggerMiddleware)
    }

    const store = configureStore({
        reducer: generateReducer(history, config.customReducers),
        preloadedState: initialState,
        middleware: middlewares,
        devTools: true,
    })

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
