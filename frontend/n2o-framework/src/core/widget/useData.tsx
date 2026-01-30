import { useEffect, useState } from 'react'
import isEqual from 'lodash/isEqual'

import { local as localStorage } from '../../utils/Storage'

export const LOCAL_WIDGET_CONFIG_KEY = 'n2oLocalWidgetConfig'

export const getData = <
    T extends Record<string, unknown>,
    R extends Partial<T> = Partial<T>,
>(key: string): R => {
    const storageFullKey = `${LOCAL_WIDGET_CONFIG_KEY}_${key}`

    return localStorage.getItem<R>(storageFullKey) || {} as R
}

export const setData = <T extends Record<string, unknown>>(key: string, value: T | null): void => {
    const storageFullKey = `${LOCAL_WIDGET_CONFIG_KEY}_${key}`

    localStorage.setItem(storageFullKey, value)
}

/**
 * ХУК позволяет сохранять и впоследствии получать сохранённые данные
 */
export const useData = <T extends Record<string, unknown>>(key: string) => {
    const storageFullKey = `${LOCAL_WIDGET_CONFIG_KEY}_${key}`
    const [value, setStateValue] = useState<Partial<T>>(() => getData<T>(key))

    // synchronization between tabs
    useEffect(() => {
        return localStorage.subscribe((key, newValue) => {
            if (key === storageFullKey) {
                setStateValue(value => (
                    isEqual(newValue, value)
                        ? value
                        : newValue || {}
                ))
            }
        })
    }, [storageFullKey])

    // update method
    const setValue = (data: Partial<T> | null) => {
        if (data === null) {
            setData(key, data)
            setStateValue({})

            return
        }
        if (isEqual(data, value)) { return }

        const newValue = Object.assign(value, data)

        setData(key, newValue)
        setStateValue(newValue)
    }

    return { value, setValue }
}
