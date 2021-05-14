import { createAction } from 'redux-actions'

/**
 * дефолтный мэппер пэйлоада
 * @param payload
 * @param meta
 */
// eslint-disable-next-line no-unused-vars
function defaultPayload(payload = {}, meta = {}) {
    return payload
}

/**
 * дефолтный мэппер меты
 * @param payload
 * @param meta
 */
// eslint-disable-next-line no-unused-vars
function defaultMeta(payload = {}, meta = {}) {
    return meta
}

/**
 * Обертка вокруг createAction с дефолтными мапперами
 * @param type
 * @param payloadFunc
 * @param metaFunc
 */
export default function createActionHelper(
    type,
    payloadFunc = defaultPayload,
    metaFunc = defaultMeta,
) {
    return createAction(type, payloadFunc, metaFunc)
}
