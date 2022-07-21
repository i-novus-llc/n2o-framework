export type PagingInfo = {
    page: number
    size: number
}

export function applyPaging<TData>(
    list: TData[],
    { page, size }: PagingInfo,
): { list: TData[], page: number } {
    if (list.length <= size) {
        return { list, page: 1 }
    }

    let newPage = page

    if (list.length < (page - 1) * size) {
        newPage = Math.floor(list.length / size) + 1
    }

    return {
        list: list.slice((newPage - 1) * size, newPage * size),
        page: newPage,
    }
}
