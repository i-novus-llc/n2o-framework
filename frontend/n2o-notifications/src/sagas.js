import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import { take, call, fork, select, put, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import {
    requestConfigSuccess,
    userConfigSelector,
    menuSelector,
} from 'n2o-framework/lib/ducks/global/store'
import { eventChannel } from 'redux-saga'

export function subscribeMetadata(emitter, updater, dataSourceId, source) {
    return response => {
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

export function* createSocketChannel(stompClient, needAnOpenChannel, destination, updater, dataSourceId, source) {
    return eventChannel(emitter => {
        // unsubscribe function
        const unsubscribe = () => {
            stompClient.disconnect()
        }

        stompClient.connect({}, frame => {
                console.log('Connected: ' + frame)

                if (needAnOpenChannel) {
                    // subscribe to the update source from destination
                    stompClient.subscribe(
                        destination,
                        subscribeMetadata(emitter, updater, dataSourceId, source),
                    )
                } else {
                    return unsubscribe()
                }

                stompClient.send(destination)
            },

            errorCallback => {
                console.log(errorCallback)
            },
        )

        return unsubscribe
    })
}

function* connectionWS() {
    // yield take(requestConfigSuccess.type)
    /*FIXME bring it back it awaits config json*/
    const menu = yield select(menuSelector)
    const { wsPrefix } = menu

    if (wsPrefix) {
        const socket = yield new SockJS(wsPrefix)

        return Stomp.over(socket)
    }

    return null
}

function* connectionExecutor({ dataSourceId, componentId, updater, source, connected }) {
    const state = yield select()

    const connectedComponents = state[source][dataSourceId][connected] || []

    const isStompProvider = get(state, `${source}.${dataSourceId}.provider.type`) === 'stomp'
    /* user prefix for private messages */
    const destination = '/user' + get(state, `${source}.${dataSourceId}.provider.destination`)

    const needAnOpenChannel = connectedComponents.length > 0 && isStompProvider

    try {
        const stompClient = yield call(connectionWS)

        const socketChannel = yield call(
            createSocketChannel,
            stompClient,
            needAnOpenChannel,
            destination,
            updater,
            dataSourceId,
            source,
        )

        while (true) {
            try {
                const action = yield take(socketChannel)

                //event that updates the source
                yield put(action)
            } catch (error) {
                console.error('socketChannel error: ', error)

                socketChannel.close()
                call(wsSagaWorker, dataSourceId, componentId, updater, source, connected)
            }

        }
    } catch (e) {
        /**/
    }
}


export function* wsSagaWorker(config) {
    const { observables, updater, source, connected } = config
    yield takeEvery(observables, ({ payload }) => connectionExecutor({
        ...payload,
        updater,
        source,
        connected,
    }))
}

export const sagas = (config) => [fork(wsSagaWorker, config)]
