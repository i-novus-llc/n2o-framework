import { getCookie } from './cookie'
import { COOKIE_KEY, LEVEL_PRIORITY, N2O_LOGGER } from './logger/const'
import { Logger, LogLevel } from './logger/types'
import { logger as consoleLogger } from './logger/console'
import { logger as postMessageLogger } from './logger/postMessage'

const currentLevel = getCookie(COOKIE_KEY) as LogLevel | null ?? 'error'
let currentLogger = consoleLogger

window.addEventListener('message', ({ data }) => {
    if (!data || data?.type !== N2O_LOGGER) {
        return
    }

    if (window.parent) {
        currentLogger = postMessageLogger
    } else {
        consoleLogger.error('N2O Logger: Не удалось выполнить postMessage - window.parent недоступен')
    }
})

const shouldLog = (methodLevel: LogLevel): boolean => {
    return LEVEL_PRIORITY[currentLevel] >= LEVEL_PRIORITY[methodLevel]
}

const createMethod = (methodLevel: LogLevel) => {
    return (message: unknown) => {
        if (!shouldLog(methodLevel)) { return }

        currentLogger[methodLevel](`=> ${message}`)
    }
}

export const logger: Logger = {
    log: createMethod('log'),
    warn: createMethod('warn'),
    error: createMethod('error'),
    info: createMethod('info'),
}
