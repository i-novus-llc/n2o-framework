import { type Subscription } from './types'

/**
 * Хранилище групп подписок, сгруппированных по строковым ключам.
 * Каждая группа представляет собой Set объектов подписок, имеющих метод unsubscribe.
 *
 * @remarks
 * Используется для управления подписками STOMP-соединений в sagas.
 * Позволяет добавлять подписку в группу, отписывать все подписки группы и получать группу.
 */
const subscriptions = new Map<string, Set<Subscription>>()

/**
 * Менеджер для управления группами подписок.
 * Предоставляет методы для добавления, удаления и получения подписок по ключу.
 */
export const subscriptionManager = {
    add(key: string, subscribed: Subscription) {
        let subs = subscriptions.get(key)

        if (!subs) {
            subs = new Set()
            subscriptions.set(key, subs)
        }
        subs.add(subscribed)
    },
    remove(key: string) {
        const subs = subscriptions.get(key)

        if (subs) {
            subs.forEach((sub: Subscription) => sub.unsubscribe())
            subscriptions.delete(key)
        }
    },

    get(key: string) {
        return subscriptions.get(key) || null
    },
}
