import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import { take, call, fork, select, put, takeEvery } from 'redux-saga/effects'
import get from 'lodash/get'
import {
    requestConfigSuccess,
    userConfigSelector,
} from 'n2o-framework/lib/ducks/global/store'
import { eventChannel } from 'redux-saga'

export function subscribeMetadata(emitter, updater, dataSourceId, source) {
    return response => {
        const metadata = JSON.parse(response.body) || {}
        const responseKeys = Object.keys(metadata)

        if (responseKeys.length) {
            for (const fieldKey of responseKeys) {
                emitter(updater(source, dataSourceId, fieldKey, metadata[fieldKey]))
            }
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

function* connectionWS(wsUrl) {
    // yield take(requestConfigSuccess.type)
    /*FIXME bring it back it awaits config json*/

    const user = yield select(userConfigSelector) || {}
    const { webSocketSessionid: token } = user
    const finalWsUrl = token ? `${wsUrl}?wsid=${token}` : wsUrl

    const socket = yield new SockJS(finalWsUrl)

    return Stomp.over(socket)
}

function* connectionExecutor({ dataSourceId, componentId, updater, source, connected, wsUrl }) {
    const state = yield select()

    const connectedComponents = state[source][dataSourceId][connected] || []
    const isStompProvider = get(state, `${source}.${dataSourceId}.provider.type`) === 'stomp'
    // const destination = get(state, `${source}.${dataSourceId}.provider.destination`)
    /*FIXME bring it back*/

    const destination = '/user/exchange/default/message.count' /*FIXME for debugging remove it and return destination from dataSource*/
    const needAnOpenChannel = connectedComponents.length > 0 /* FIXME isStompProvider check will be added */

    try {
        const stompClient = yield call(connectionWS, wsUrl)

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
                call(wsSagaWorker, dataSourceId, componentId, updater, source, connected, wsUrl)
            }

        }
    } catch (e) {
        console.log('connectionExecutor error: ', e)
    }
}


export function* wsSagaWorker(config) {
    const { observables, updater, source, connected, wsUrl } = config

    yield takeEvery(observables, ({ payload }) => connectionExecutor({
        ...payload,
        updater,
        source,
        connected,
        wsUrl,
    }))
}

export const sagas = (config) => [fork(wsSagaWorker, config)]
