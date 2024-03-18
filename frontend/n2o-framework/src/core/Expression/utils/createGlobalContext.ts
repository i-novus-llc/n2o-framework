import { getGlobal } from './getGlobal'

let context: object | undefined

// Список допустимых переменных из глобального окружения
const allowList: Array<string | symbol> = [
    'undefined', 'null', 'NaN', 'Infinity', 'console', 'JSON', 'Math',
    // тут сомнительные пропсы, но пусть пока будут, будем убирать по мере разбора
    'location', 'history', 'navigator', 'crypto',
]

const selfKeys: Array<string | symbol> = ['window', 'self', 'global', 'globalThis']

/**
 * Получение обёртки над глобальым контекстом для контроля доступа к пропертям
 */
export function createGlobalContext(): object {
    if (context) { return context }

    const self = getGlobal()

    context = new Proxy(self, {
        // Для того чтобы выполнение не падало, если не сделали проверку 'typeof поле_модели'
        has() { return true },
        // Иммутабельность глобального контекста
        set() { return false },
        get(target, key: keyof typeof self) {
            const value = target[key]

            if (typeof value === 'function' || allowList.includes(key)) {
                return value
            }
            if (selfKeys.includes(key)) {
                return context
            }

            return undefined
        },
    })

    return context
}
