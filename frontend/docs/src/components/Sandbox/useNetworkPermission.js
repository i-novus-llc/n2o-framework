import { useState, useEffect, useRef, useCallback } from 'react'

const LOCAL_NETWORK_DOMAINS = ['next-n2o.i-novus.ru']
const LOOPBACK_DOMAINS = ['localhost', '127.0.0.1', '[::1]', '::1']

/**
 * Проверяет, является ли хост loopback-адресом.
 * @param {string} hostname
 * @returns {boolean}
 */
function isLoopbackHost(hostname) {
    const name = hostname.toLowerCase()
    return LOOPBACK_DOMAINS.includes(name) || name.startsWith('127.')
}

/**
 * Определяет, требуется ли разрешение local-network.
 * @param {string} urlString
 * @returns {boolean}
 */
function requiresLocalNetworkPermission(urlString) {
    try {
        const url = new URL(urlString)
        return LOCAL_NETWORK_DOMAINS.includes(url.hostname)
    } catch {
        return false
    }
}

/**
 * Определяет, требуется ли разрешение loopback-network.
 * @param {string} urlString
 * @returns {boolean}
 */
function requiresLoopbackPermission(urlString) {
    try {
        const url = new URL(urlString)
        return isLoopbackHost(url.hostname)
    } catch {
        return false
    }
}

/**
 * Хук для проверки разрешений доступа к локальной сети.
 * @param {string} url - Абсолютный URL ресурса.
 * @returns {{
 *   permissionState: 'granted' | 'denied' | 'prompt' | null,
 *   error: string | null,
 *   isLoading: boolean
 * }}
 */
export function useNetworkPermission(url) {
    const [permissionState, setPermissionState] = useState(null)
    const [error, setError] = useState(null)
    const [isLoading, setIsLoading] = useState(false)
    const listenerRef = useRef(null)

    const checkPermission = useCallback(async () => {
        setIsLoading(true)
        setError(null)

        if (!url) {
            setPermissionState(null)
            setIsLoading(false)
            return
        }

        let permissionDescriptor = null

        if (requiresLocalNetworkPermission(url)) {
            permissionDescriptor = { name: 'local-network' }
        } else if (requiresLoopbackPermission(url)) {
            permissionDescriptor = { name: 'loopback-network' }
        }

        // Разрешение не требуется
        if (!permissionDescriptor) {
            setPermissionState('granted')
            setIsLoading(false)

            return
        }

        // поддержка Permissions API
        if (!navigator.permissions?.query) {
            setPermissionState('granted')
            setIsLoading(false)

            return
        }

        try {
            const status = await navigator.permissions.query(permissionDescriptor)

            setPermissionState(status.state)
            setError(status.state === 'denied' ? 'Доступ к локальной сети запрещён.' : null)

            const handleChange = () => {
                setPermissionState(status.state)
                setError(status.state === 'denied' ? 'Доступ к локальной сети запрещён.' : null)
            };

            status.addEventListener('change', handleChange)
            listenerRef.current = { status, handler: handleChange }

        } catch (err) {
            if (err instanceof TypeError) {
                setPermissionState('granted')
            } else {
                setError(`Ошибка при проверке разрешения: ${err.message}`)
                setPermissionState(null)
            }
        } finally {
            setIsLoading(false)
        }
    }, [url])

    useEffect(() => {
        checkPermission()

        return () => {
            if (listenerRef.current) {
                const { status, handler } = listenerRef.current
                status.removeEventListener('change', handler)
                listenerRef.current = null
            }
        };
    }, [checkPermission])

    return { permissionState, error, isLoading }
}

export const NETWORK_PERMISSION_INSTRUCTION = `Вы ограничили доступ к локальной сети.
Чтобы его восстановить, измените настройки разрешений в браузере.

Посмотрите в адресную строку, нажмите на значок слева от адреса сайта.
В появившемся меню найдите пункт «Локальная сеть» и активируйте переключатель «Локальная сеть: выберите разрешение». После этого обновите страницу.

Если пункт «Локальная сеть» отсутствует, в том же меню перейдите в раздел «Настройки сайтов».
Откроется новое окно с настройками для этого сайта. Найдите там пункт «Локальная сеть» и выберите «Разрешить». Затем обновите страницу.`
