import { initialState } from '../ducks/alerts/store'

export function generateErrorMeta(msg: Record<string, unknown>) {
    const metaMsg = { ...initialState, ...msg }

    return { alert: { messages: [metaMsg] } }
}
