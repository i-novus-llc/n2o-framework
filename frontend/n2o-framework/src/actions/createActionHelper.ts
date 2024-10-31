import { createAction } from 'redux-actions'

/**
 * Дефолтный мэппер пэйлоада
 * @param payload
 * @param meta
 */
function defaultPayload<Payload>(payload: Payload = {} as Payload, meta: Record<string, unknown> = {}): Payload {
    return payload
}

/**
 * Дефолтный мэппер меты
 * @param payload
 * @param meta
 */
function defaultMeta<Meta>(payload: Record<string, unknown> = {}, meta: Meta = {} as Meta): Meta {
    return meta
}

/**
 * Обертка вокруг createAction с дефолтными мапперами
 * @param type
 * @param payloadFunc
 * @param metaFunc
 */

export default function createActionHelper(
    type: string,
    payloadFunc = defaultPayload,
    metaFunc = defaultMeta,
) {
    return createAction(type, payloadFunc, metaFunc)
}
