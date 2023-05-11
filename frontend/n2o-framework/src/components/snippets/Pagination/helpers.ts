import { COUNT_BY_REQUEST, COUNT_NEVER, showCountType } from './constants'

export const getTotalVisibility = (
    showCount: showCountType,
    showLast: boolean,
    count?: number,
) => {
    if (!showCount || showCount === COUNT_NEVER) {
        return false
    }

    if (showCount === COUNT_BY_REQUEST) {
        /* eliminates blinking when there is a conflict
           between the showLast and by-request */
        return !(showLast && !count)
    }

    /* removes blinking until the count is received */
    return !!count
}
