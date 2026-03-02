import { Handler, Predicate, Subscriber } from './types'

let subscribers: Subscriber[] = []

export const getSubscribers = () => {
    return [...subscribers]
}

export const subscribe = (handler: Handler, predicate?: Predicate) => {
    const subscriber = { handler, predicate }

    subscribers.push(subscriber)

    return () => {
        subscribers = subscribers.filter(s => (s !== subscriber))
    }
}
