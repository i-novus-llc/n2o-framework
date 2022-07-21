import type { SortDirection } from '../../../../core/datasource/const'

export function applySorting<TData>(
    list: TData[],
    sorting: Partial<Record<string, SortDirection>>,
): TData[] {
    // TODO

    return list
}
