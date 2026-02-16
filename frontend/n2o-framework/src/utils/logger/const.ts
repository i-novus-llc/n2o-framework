import { LogLevel } from './types'

export const LEVEL_PRIORITY: Record<LogLevel, number> = {
    error: 0,
    warn: 1,
    log: 2,
    info: 3,
}

export const N2O_LOGGER = 'N2O/useExternalLogger'
export const COOKIE_KEY = 'logLevel'
