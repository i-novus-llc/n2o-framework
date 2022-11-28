export function create<TItem extends object>(list: TItem[], item: TItem | TItem[]): TItem[] {
    const tail = Array.isArray(item) ? item : [item]

    return [
        ...list,
        ...tail,
    ]
}
