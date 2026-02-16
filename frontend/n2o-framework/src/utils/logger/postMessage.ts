import { N2O_LOGGER } from './const'
import { Logger, LogLevel } from './types'

const postMessage = (type: LogLevel) => {
    return (message: unknown) => window.parent.postMessage({ type, message, from: N2O_LOGGER }, '*')
}

export const logger: Logger = {
    log: postMessage('log'),
    warn: postMessage('warn'),
    error: postMessage('error'),
    info: postMessage('info'),
}
