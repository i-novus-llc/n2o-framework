import { COUNT_BY_REQUEST, COUNT_NEVER, showCountType } from './constants'

export const getTotalVisibility = (
    showCount?: showCountType,
    showLast?: boolean,
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

interface Options {
    loading?: boolean
    totalPages?: number
    activePage: number
    propsHasNext: boolean
}

export const getHasNext = (options: Options) => {
    const { loading } = options

    if (loading) { return false }

    const { totalPages, activePage, propsHasNext } = options

    return totalPages !== undefined && totalPages > 1 && activePage === 1 ? true : propsHasNext
}
