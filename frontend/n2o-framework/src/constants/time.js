export const SEC = 1000
// export const MIN = 60 * SEC
// export const HOUR = 60 * MIN

/**
 * Время задержки перед запросом при изменении фильтра
 * Используется, чтобы не отправлять пачку запросов при непрерывном вводе текста
 * @type {number}
 */
export const FILTER_DELAY = 0.5 * SEC

/**
 * Время кеширования запроса, при котором мы считаем, что два идентичных запроса
 * могут использовать один и тот же ответ от сервера
 * @type {number}
 */
export const REQUEST_CACHE_TIMEOUT = 0.3 * SEC
