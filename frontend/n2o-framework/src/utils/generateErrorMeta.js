import { defaultState } from '../reducers/alerts'

export function generateErrorMeta(msg) {
    const metaMsg = { ...defaultState, ...msg }

    return {
        alert: {
            messages: [metaMsg],
        },
    }
}
