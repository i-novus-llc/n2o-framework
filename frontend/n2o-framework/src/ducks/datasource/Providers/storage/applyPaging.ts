import { Paging } from '../../Provider'

export type PagingInfo = {
    page: number
    size: number
}

export function applyPaging<TData>(
    list: TData[],
    { page, size }: PagingInfo,
): { list: TData[], paging: Pick<Paging, 'page'> } {
    if (list.length <= size) {
        return { list, paging: { page: 1 } }
    }

    let newPage = page

    if (list.length < (page - 1) * size) {
        newPage = Math.floor(list.length / size) + 1
    }

    return {
        list: list.slice((newPage - 1) * size, newPage * size),
        paging: { page: newPage },
    }
}
