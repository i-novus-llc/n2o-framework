export const N2O_LOGGER = 'N2O/useExternalLogger'

type Logger = {
    log(message: unknown): void
    warn(message: unknown): void
    error(message: unknown): void
    info(message: unknown): void
}

const consoleLogger: Logger = console

let currentLogger = consoleLogger

const postMessage = ({ type, message }: {
    type: keyof Logger,
    message: unknown,
}) => {
    window.parent.postMessage({ type, message, from: N2O_LOGGER }, '*')
}

const postMessageLogger: Logger = {
    log(message: unknown) { return postMessage({ type: 'log', message }) },
    warn(message: unknown) { return postMessage({ type: 'warn', message }) },
    error(message: unknown) { return postMessage({ type: 'error', message }) },
    info(message: unknown) { return postMessage({ type: 'info', message }) },
}

window.onmessage = function onMessage({ data }) {
    if (!data || data?.type !== N2O_LOGGER) {
        return
    }

    if (window.parent) {
        currentLogger = postMessageLogger
    } else {
        consoleLogger.error('N2O Logger: Не удалось выполнить postMessage - window.parent недоступен')
    }
}

export const logger: Logger = {
    log(message: unknown) { return currentLogger.log(message) },
    warn(message: unknown) { return currentLogger.warn(message) },
    error(message: unknown) { return currentLogger.error(message) },
    info(message: unknown) { return currentLogger.info(message) },
}
