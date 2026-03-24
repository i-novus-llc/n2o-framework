import { select } from 'redux-saga/effects'

import { menuSelector } from '../../ducks/global/selectors'
import { type Config } from '../../ducks/global/Global'

export function* createStompClient() {
    const menu: Config = yield select(menuSelector)
    const { wsPrefix } = menu

    if (wsPrefix) {
        // @ts-ignore sockjs-client not typed
        const SockJSModule = yield import('sockjs-client')
        const SockJS = SockJSModule.default

        const socket = new SockJS(wsPrefix)

        // @ts-ignore stompjs not typed
        const { Stomp } = yield import('stompjs')

        return Stomp.over(socket)
    }

    return null
}
