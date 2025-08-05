import { type HeaderCell } from '../../../ducks/table/Table'

export function getLabel(columnId: string, label?: string, icon?: string) {
    if (!label && !icon) { return columnId }
    if (!label) { return null }

    return label
}

export const getVisibilityInfo = (items: HeaderCell[]): { checked: boolean; indeterminate: boolean } => {
    if (items.length === 0) {
        return { checked: false, indeterminate: false }
    }

    let allVisible = true
    let someVisible = false

    for (const item of items) {
        if (item.children) {
            const childResult = getVisibilityInfo(item.children)

            if (!childResult.checked) { allVisible = false }
            if (childResult.checked || childResult.indeterminate) { someVisible = true }
        } else if (!item.visibleState) {
            allVisible = false
        } else {
            someVisible = true
        }
    }

    return {
        checked: allVisible,
        indeterminate: !allVisible && someVisible,
    }
}

export const callbackRecursive = (items: HeaderCell[], callback: (item: HeaderCell) => void): void => items.forEach(item => (
    item.children
        ? callbackRecursive(item.children, callback)
        : callback(item)))
