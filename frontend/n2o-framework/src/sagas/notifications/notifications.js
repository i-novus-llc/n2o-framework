import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import { eventChannel } from 'redux-saga'
import { take, call, fork, select, put, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

import { menuSelector } from '../../ducks/global/selectors'

export function subscribeMetadata(emitter, updater, dataSourceId, source) {
    // eslint-disable-next-line consistent-return
    return (response) => {
        if (response.body) {
            const metadata = JSON.parse(response.body) || {}
            const responseKeys = Object.keys(metadata)

            if (responseKeys.length) {
                for (const fieldKey of responseKeys) {
                    emitter(updater(source, dataSourceId, fieldKey, metadata[fieldKey]))
                }
            }
        } else {
            return {}
        }
    }
}

/* response from the stomp with meta for redux action */
export function doReduxAction(emitter) {
    return (response) => {
        if (response.body) {
            const action = JSON.parse(response.body) || {}

            if (!isEmpty(action)) {
                emitter(action)
            } else {
                return {}
            }
        }

        return {}
    }
}

export function createSocketChannel(
    stompClient,
    destinations,
    needToSubscribe,
    updater,
    dataSourceId,
    source,
    permanent,
) {
    return eventChannel((emitter) => {
        // unsubscribe function
        const unsubscribe = () => {
            stompClient.disconnect()
        }

        stompClient.connect(
            {},
            (frame) => {
            // eslint-disable-next-line no-console
                console.log(`isConnected: ${frame}`)
                for (const { destination } of destinations) {
                    if (permanent) {
                        stompClient.subscribe(`/user${destination}`, doReduxAction(emitter))
                    } else {
                        const subscribed = stompClient.subscribe(
                            destination,
                            subscribeMetadata(emitter, updater, dataSourceId, source),
                        )

                        if (!needToSubscribe) {
                            subscribed.unsubscribe()
                        }
                    }

                    stompClient.send(destination)
                }
            },
            (errorCallback) => {
            // eslint-disable-next-line no-console
                console.log(errorCallback)
            },
        )

        return unsubscribe
    })
}

function* connectionWS() {
    // yield take(requestConfigSuccess.type)
    /* FIXME bring it back it awaits config json*/
    const menu = yield select(menuSelector)
    const { wsPrefix } = menu

    if (wsPrefix) {
        const socket = yield new SockJS(wsPrefix)

        return Stomp.over(socket)
    }

    return null
}

// eslint-disable-next-line consistent-return
function* connectionExecutor({ dataSourceId, componentId, updater, source, connected, menu, type }) {
    const state = yield select()

    try {
        const stompClient = yield call(connectionWS)
        const permanent = type === 'n2o/global/REQUEST_CONFIG_SUCCESS' && menu?.events
        const isStompProvider = get(state, `${source}.${dataSourceId}.provider.type`) === 'stomp'

        let destinations
        let needToSubscribe = true

        /* connecting without a data source provider, this is from the config.json */
        if (permanent) {
            destinations = menu.events
        } else if (!isStompProvider) {
            return null
        } else {
            const connectedComponents = state[source][dataSourceId][connected] || []

            needToSubscribe = connectedComponents.length > 0

            /* user prefix for private messages */
            // eslint-disable-next-line sonarjs/no-nested-template-literals
            destinations = [{ destination: `/user${get(state, `${source}.${dataSourceId}.provider.destination`)}` }]
        }

        const socketChannel = yield call(
            createSocketChannel,
            stompClient,
            destinations,
            needToSubscribe,
            updater,
            dataSourceId,
            source,
            permanent,
        )

        while (true) {
            try {
                const action = yield take(socketChannel)

                yield put(action)
            } catch (error) {
                // eslint-disable-next-line no-console
                console.error('socketChannel Error: ', error)

                socketChannel.close()

                call(wsSagaWorker, dataSourceId, componentId, updater, source, connected, menu, type)
            }
        }
    } catch (error) {
        // eslint-disable-next-line no-console
        console.log(error)
    }
}

export function* wsSagaWorker(config) {
    const { observables, updater, source, connected } = config

    yield takeEvery(observables, ({ payload, type }) => connectionExecutor({
        ...payload,
        type,
        updater,
        source,
        connected,
    }))
}

export const sagas = config => [fork(wsSagaWorker, config)]
