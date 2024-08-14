import { UserConfig, Security } from './types'
import { AuthProvider, defaultProvider } from './Provider'

async function check(
    json: object & { security?: Security, disabled?: boolean, enabled?: boolean },
    user: UserConfig,
    provider: AuthProvider,
) {
    const { security, ...props } = json

    if (!security) {
        return json
    }

    const securityList = (Array.isArray(security) ? security : [security])
        .map(config => Object.values(config))
        .flat()
    let newJson = props

    for (const config of securityList) {
        let hasAccess = false

        try {
            hasAccess = await provider.check(config, user)
        } catch (e) {
            // TODO: log?
        }

        if (!hasAccess) {
            if (config.behavior === 'disabled') {
                newJson = { ...props, disabled: true, enabled: false }
            } else {
                return null
            }
        }
    }

    return newJson
}

/**
 * Рекурсивный обход JSON метаданных с проверкой на security
 * @param {object} metadata "сырые" метаданные
 * @param {UserConfig} user Данные пользователя
 * @param {string[]} ignoreList список ключей, которые можно не обходить
 * @returns {object} Отфильтрованный объект метаданных
 */
export async function resolveMetadata<
    T = unknown,
>(
    metadata: T,
    user: object,
    ignoreList: string[] = [],
    provider: AuthProvider = defaultProvider,
): Promise<T | null> {
    if (!metadata || typeof metadata !== 'object') {
        return metadata
    }

    if (Array.isArray(metadata)) {
        // В метаданных массив должен быть однородным
        // проваливаемся внутрь, если массив объектов
        if (typeof metadata[0] === 'object') {
            const result = []

            for (const item of metadata) {
                const resolved = await resolveMetadata(item, user, ignoreList, provider)

                if (resolved) { result.push(resolved) }
            }

            return result as unknown as T
        }

        return metadata
    }

    const resolved = await check(metadata, user, provider)

    if (!resolved) { return null }

    const result: Record<PropertyKey, unknown> = {}

    for (const [key, value] of Object.entries(resolved)) {
        if (ignoreList.includes(key)) {
            result[key] = value
            // eslint-disable-next-line no-continue
            continue
        }

        result[key] = await resolveMetadata(value, user, ignoreList, provider)
    }

    return result as T
}
