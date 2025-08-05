import { type HeaderCell } from '../table/Table'

export function reorderElement<T extends { id: string }>(arr: T[], reorderId: string, targetId: string) {
    const reorderIndex = arr.findIndex(item => item.id === reorderId)
    const targetIndex = arr.findIndex(item => item.id === targetId)

    if (reorderIndex === -1 || targetIndex === -1 || reorderIndex === targetIndex) { return arr }

    const newArr = [...arr]
    const [movedElement] = newArr.splice(reorderIndex, 1)

    newArr.splice(targetIndex, 0, movedElement)

    return newArr
}

export const findItemRecursive = (items: HeaderCell[], targetId: string): HeaderCell | null => {
    for (const item of items) {
        if (item.id === targetId) { return item }

        if (item.children) {
            const found = findItemRecursive(item.children, targetId)

            if (found) { return found }
        }
    }

    return null
}
