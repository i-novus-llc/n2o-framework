/* eslint-disable no-console */
import { Logger } from './types'

// 👉 гарантируем корректный this у console
export const logger: Logger = {
    error: console.error.bind(console),
    warn: console.warn.bind(console),
    log: console.log.bind(console),
    info: console.info.bind(console),
}
