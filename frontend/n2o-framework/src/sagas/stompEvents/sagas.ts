import { eventChannel, EventChannel } from 'redux-saga'
import { call, fork, put, select, take, takeEvery } from 'redux-saga/effects'
import isEmpty from 'lodash/isEmpty'
import { type AnyAction } from 'redux'

import { addComponent, removeComponent } from '../../ducks/datasource/store'
import { metadataSuccess, resetPage } from '../../ducks/pages/store'
import { requestConfigSuccess } from '../../ducks/global/store'
import { logger } from '../../utils/logger'
import { type Provider, ProviderType } from '../../ducks/datasource/Provider'
import { EventTypes, getStompEvents, type StompEvent } from '../../ducks/watchEvents/watchEvents'
import { dataSourceComponentsSelector, dataSourceProviderSelector } from '../../ducks/datasource/selectors'
import type { AddComponentAction, RemoveComponentAction } from '../../ducks/datasource/Actions'
import { type DataSourceState } from '../../ducks/datasource/DataSource'
import type { MetadataSuccess, Reset } from '../../ducks/pages/Actions'
import { type RequestConfigAction } from '../../ducks/global/Actions'

import type { ConnectionExecutorProps, CreateSocketChannelOptions, StompClient } from './types'
import { subscribeMetadata, doReduxAction } from './reactions'
import { createStompClient } from './createStompClient'
import { subscriptionManager } from './subscriptionManager'

/**
 * Создаёт канал событий (eventChannel) для STOMP-подключения.
 *
 * @remarks
 * Функция устанавливает соединение с STOMP-сервером через переданный клиент.
 * Для каждого destination из списка выполняет:
 * 1. Подписку на `/user${destination}` с обработчиком из reactions (redux action).
 * 2. Отправку запроса на этот destination (stompClient.send).
 *
 * Управление подписками:
 * - Если `isPermanent === false`, каждая подписка добавляется в `subscriptionManager`
 *   по указанному ключу для возможности последующей отписки через `subscriptionManager.remove(key)`.
 * - Для постоянных соединений (`isPermanent: true`) подписки не сохраняются в менеджере.
 */
export function createSocketChannel(options: CreateSocketChannelOptions) {
    const {
        stompClient,
        destinations,
        isPermanent,
        key,
        datasource,
    } = options

    return eventChannel((emitter) => {
        const unsubscribe = () => { stompClient.disconnect() }

        stompClient.connect(
            {},
            (frame: string) => {
                logger.log(`stompClient is connected: ${frame}`)
                destinations.forEach(({ destination }) => {
                    const handler = datasource
                        ? subscribeMetadata(emitter, key)
                        : doReduxAction(emitter)

                    const subscribed = stompClient.subscribe(`/user${destination}`, handler)

                    if (!isPermanent) { subscriptionManager.add(key, subscribed) }

                    stompClient.send(destination)
                })
            },
            (error) => { logger.log(error) },
        )

        return unsubscribe
    })
}

/**
 * Сага-исполнитель для управления STOMP-подключением.
 *
 * @remarks
 * Выполняет следующие шаги:
 * 1. Создаёт STOMP-клиент через `createStompClient`.
 * 2. Создаёт канал событий через `createSocketChannel` с переданными параметрами.
 * 3. В бесконечном цикле ожидает экшены из канала и диспатчит их в Redux.
 * 4. При возникновении ошибки в канале закрывает его и перезапускает сагу `stompEventsWorker`
 */
function* connectionExecutor({
    destinations,
    key,
    datasource,
    isPermanent,
}: ConnectionExecutorProps) {
    try {
        const stompClient: StompClient = yield call(createStompClient)

        const socketChannel: EventChannel<AnyAction> = yield call(createSocketChannel, {
            stompClient,
            destinations,
            key,
            datasource,
            isPermanent,
        })

        while (true) {
            try {
                const action: AnyAction = yield take(socketChannel)

                yield put(action)
            } catch (error) {
                logger.error('socketChannel Error')

                socketChannel.close()

                yield call(stompEventsWorker as never)
            }
        }
    } catch (error) {
        logger.log(error)
    }
}

/**
 * Устанавливает постоянное STOMP-подключение через application menu.events.
 * в ответе ожидает redux action.
 */
function* applicationConnection({ payload }: RequestConfigAction) {
    const { menu } = payload

    const { events } = menu

    if (!events) { return }

    const destinations = getStompEvents(events)

    if (isEmpty(destinations)) { return }

    yield call(connectionExecutor, { destinations, key: 'application', isPermanent: true })
}

/**
 * Устанавливает STOMP-подключение через datasource provider type Stomp
 * в ответе ожидает данные для обнновления модели.
 */
function* datasourceConnection({ payload }: AddComponentAction) {
    const { id } = payload
    const dataSourceProvider: Provider = yield select(dataSourceProviderSelector(id))

    if (dataSourceProvider?.type !== ProviderType.stomp) { return }

    const destinations: StompEvent[] = [{ destination: dataSourceProvider?.destination || '', id, type: EventTypes.stomp }]

    yield call(connectionExecutor, { destinations, key: id, datasource: id })
}

/**
 * Отписывает подключения провайдера типа Stomp, если к нему не подключены компоненты.
 */
function* datasourceDisconnection({ payload }: RemoveComponentAction) {
    const { id } = payload

    if (!subscriptionManager.get(id)) { return }

    const dataSourceProvider: Provider = yield select(dataSourceProviderSelector(id))

    if (dataSourceProvider?.type !== ProviderType.stomp) { return }

    const components: DataSourceState['components'] = yield select(dataSourceComponentsSelector(id))

    if (components.length === 0) { subscriptionManager.remove(id) }
}

/**
 * Устанавливает STOMP-подключение через page.events
 * в ответе ожидает redux action.
 */
function* pagesConnection({ payload }: MetadataSuccess) {
    const { pageId, json } = payload
    const { events } = json

    if (!events) { return }

    const destinations = getStompEvents(events)

    if (isEmpty(destinations)) { return }

    yield call(connectionExecutor, { destinations, key: pageId })
}

/**
 * Отписывает подключения page при его удалении.
 */
function pagesDisconnection({ payload: pageId }: Reset) {
    if (!subscriptionManager.get(pageId)) { return }

    subscriptionManager.remove(pageId)
}

export function* stompEventsWorker() {
    yield takeEvery([requestConfigSuccess], applicationConnection)
    yield takeEvery([addComponent], datasourceConnection)
    yield takeEvery([removeComponent], datasourceDisconnection)
    yield takeEvery([metadataSuccess], pagesConnection)
    yield takeEvery([resetPage], pagesDisconnection)
}

export const stompEventsSagas = [fork(stompEventsWorker)]
