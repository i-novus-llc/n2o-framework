import { AnyAction } from 'redux'

import { StompEvent } from '../../ducks/watchEvents/watchEvents'

export type Emitter = (action: AnyAction) => void

export interface StompMessage {
    body: string
    headers?: { [key: string]: string }
}

export interface Subscription {
    unsubscribe(): void
}

/**
 * Типизация STOMP клиента
 */
export interface StompClient {
    /**
     * Отключает клиент от STOMP-сервера.
     */
    disconnect(): void

    /**
     * Устанавливает соединение с STOMP-сервером.
     * @param headers - Заголовки подключения (например, { login, passcode }).
     * @param connectCallback - Колбэк, вызываемый при успешном подключении. Получает кадр CONNECTED в виде строки.
     * @param errorCallback - Колбэк, вызываемый при ошибке подключения. Получает строку с описанием ошибки.
     */
    connect(
        headers: Record<string, unknown>,
        connectCallback: (frame: string) => void,
        errorCallback: (error: string) => void
    ): void;

    /**
     * Подписывается на указанный пункт назначения (destination).
     * @param destination - Путь подписки (строка).
     * @param callback - Функция, вызываемая при получении сообщения. Принимает объект сообщения (тип зависит от библиотеки).
     * @returns Объект с методом unsubscribe для отписки.
     */
    subscribe(
        destination: string,
        callback: (message: StompMessage) => void
    ): Subscription

    /**
     * Отправляет сообщение в указанный пункт назначения.
     * @param destination - Путь назначения.
     * @param headers - Дополнительные заголовки сообщения (опционально).
     * @param body - Тело сообщения (опционально).
     */
    send(destination: string, headers?: object, body?: string): void
}

export interface ConnectionExecutorProps {
    destinations: StompEvent[]
    key: string
    datasource?: string
    isPermanent?: boolean
}

export interface CreateSocketChannelOptions extends ConnectionExecutorProps {
    stompClient: StompClient
}
