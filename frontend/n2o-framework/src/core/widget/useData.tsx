import { useEffect, useState } from 'react'
import isEqual from 'lodash/isEqual'

import { logger } from '../utils/logger'

export const LOCAL_WIDGET_CONFIG_KEY = 'n2oLocalWidgetConfig'

export const getData = (key: string) => {
    const storageFullKey = `${LOCAL_WIDGET_CONFIG_KEY}_${key}`

    try {
        const stored = localStorage.getItem(storageFullKey)

        return stored ? JSON.parse(stored) : {}
    } catch (error) {
        logger.error(error)

        return {}
    }
}

/**
 * ХУК позволяет сохранять и впоследствии получать сохранённые данные
 */
export const useData = (key: string) => {
    const storageFullKey = `${LOCAL_WIDGET_CONFIG_KEY}_${key}`
    const [value, setStateValue] = useState<Record<string, unknown>>(getData(key))

    // synchronization between tabs
    useEffect(() => {
        const handleStorageChange = (event: StorageEvent) => {
            if (event.key === storageFullKey) {
                try {
                    const newValue = event.newValue ? JSON.parse(event.newValue) : {}

                    if (!isEqual(newValue, value)) {
                        setStateValue(newValue)
                    }
                } catch (error) {
                    logger.error(error)
                }
            }
        }

        window.addEventListener('storage', handleStorageChange)

        return () => window.removeEventListener('storage', handleStorageChange)
    }, [storageFullKey, value])

    // update method
    const setValue = (data: Record<string, unknown> | null) => {
        try {
            if (data === null) {
                localStorage.removeItem(storageFullKey)
                setStateValue({})

                return
            }

            if (isEqual(data, value)) { return }

            const newValue = { ...value, ...data }

            localStorage.setItem(storageFullKey, JSON.stringify(newValue))
            setStateValue(newValue)
        } catch (error) {
            logger.error(error)
        }
    }

    return { value, setValue }
}
