import { MAX_PAGES, SEPARATION_LIMIT, FULL, FIRST, END, SIMPLE_PAGINATION_COUNT } from './constants'

export const getTotalPages = (count: number | undefined, size: number) => {
    if (!count) {
        return undefined
    }

    return Math.ceil(count / size)
}

const create = (length: number, firstPage: number) => [...Array((length)).keys()].map(i => i + firstPage)

export const usePagination = (
    totalPages: number | undefined,
    activePage: number,
    hasNext: boolean | undefined,
    showLast: boolean,
) => {
    if (totalPages && showLast) {
        if (totalPages <= SIMPLE_PAGINATION_COUNT) {
            return {
                pages: create(totalPages, 1),
                extraFirst: false,
                extraEnd: false,
            }
        }

        if (activePage < MAX_PAGES) {
            return {
                pages: create(MAX_PAGES, 1),
                ...END,
            }
        }

        if (totalPages - activePage <= SEPARATION_LIMIT) {
            return {
                pages: create(MAX_PAGES, totalPages - MAX_PAGES + 1),
                ...FIRST,
            }
        }

        return {
            pages: create(SEPARATION_LIMIT, activePage - 1),
            ...FULL,
        }
    }

    /* infinity pagination logic */
    const separate = activePage >= MAX_PAGES

    if (hasNext) {
        /* start of the infinity pagination */
        if (!separate) {
            return {
                pages: create(activePage + 1, 1),
                ...END,
            }
        }

        /* the count was received by-request */
        if (totalPages && activePage === totalPages) {
            return {
                pages: create(SEPARATION_LIMIT, activePage - 2),
                ...FIRST,
            }
        }

        return {
            pages: create(SEPARATION_LIMIT, activePage - 1),
            ...FULL,
        }
    }

    if (separate) {
        return {
            pages: create(SEPARATION_LIMIT, activePage - 2),
            ...FIRST,
        }
    }

    return {
        pages: create(activePage, 1),
        ...END,
    }
}
