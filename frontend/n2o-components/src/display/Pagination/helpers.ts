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
