/* eslint-disable no-restricted-globals */
import isEmpty from 'lodash/isEmpty'

import { logger } from '../core/utils/logger'

export enum Type {
    localStorage = 'localStorage',
    sessionStorage = 'sessionStorage',
}

const baseUrl = location.pathname

export type ObjectStorage = {
    setItem<T extends object>(key: string, item: T | null): void
    getItem<T extends object>(key: string): T | null
    removeItem(key: string): void
    clear(): void
    subscribe<T extends object>(callback: ((key: string, value: T | null) => void)): (() => void)
}

const stringify = (data: unknown): string | null => {
    if (!data || isEmpty(data)) { return null }

    try {
        return JSON.stringify(data)
    } catch (error) {
        logger.warn(error)

        return null
    }
}

const parse = <T extends object>(str: string): T | null => {
    try {
        return JSON.parse(str) as T
    } catch (error) {
        logger.warn(error)

        return null
    }
}

export const getFullKey = (key: string) => `${baseUrl}:/${key}`
export const getClearKey = (key: string) => key.replace(`${baseUrl}:/`, '')

// TODO: Сборка мусора на старте
const createStorage = (original: Storage): ObjectStorage => {
    // store.getItem('')

    return {
        // TODO: add custom expires
        setItem<T extends object>(key: string, item: T | null): void {
            try {
                const str = stringify(item)

                if (!str) {
                    original.removeItem(getFullKey(key))

                    return
                }

                original.setItem(getFullKey(key), str)
            } catch (error) {
                logger.warn(error)
                // TODO remove old values
            }
        },
        getItem<T extends object>(key: string): T | null {
            const item = original.getItem(getFullKey(key))

            if (!item) { return null }

            return parse(item)
        },
        removeItem(key: string): void {
            original.removeItem(getFullKey(key))
        },
        clear(): void {
            for (const key of Object.keys(original)) {
                if (key.startsWith(`${baseUrl}:/`)) { original.removeItem(key) }
            }
        },
        subscribe(callback) {
            const handler = (event: StorageEvent) => {
                if (event.key?.startsWith(`${baseUrl}:/`)) {
                    callback(getClearKey(event.key), event.newValue ? parse(event.newValue) : null)
                }
            }

            window.addEventListener('storage', handler)

            return () => {
                window.removeEventListener('storage', handler)
            }
        },
    }
}

export const local = createStorage(localStorage)
export const session = createStorage(sessionStorage)

export function getStorage(type: Type): ObjectStorage {
    return type === Type.localStorage
        ? local
        : session
}
