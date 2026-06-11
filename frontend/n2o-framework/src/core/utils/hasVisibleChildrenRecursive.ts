interface Item {
    visible?: boolean
}

export const hasVisibleChildrenRecursive = <TItem extends Item>(
    items: TItem[] | null | undefined,
    getChildren: (item: TItem) => TItem[] | null | undefined,
): boolean => {
    return items?.some((item) => {
        const children = getChildren(item)

        if (children?.length) {
            return hasVisibleChildrenRecursive(children, getChildren)
        }

        return item.visible ?? true
    }) ?? false
}
