import { initialState } from '../ducks/alerts/store'

export function generateErrorMeta(msg) {
    const metaMsg = { ...initialState, ...msg }

    return {
        alert: {
            messages: [metaMsg],
        },
    }
}
