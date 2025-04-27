/* eslint-disable @typescript-eslint/no-explicit-any */

export const createParameterSelector =
    <T extends (arg: any) => any>
    (selector: T) => (_: unknown, params: Parameters<T>[0]): ReturnType<T> => selector(params)

export function reorderElement<T extends { id: string }>(arr: T[], reorderId: string, targetId: string) {
    const reorderIndex = arr.findIndex(item => item.id === reorderId)
    const targetIndex = arr.findIndex(item => item.id === targetId)

    if (reorderIndex === -1 || targetIndex === -1 || reorderIndex === targetIndex) { return arr }

    const newArr = [...arr]
    const [movedElement] = newArr.splice(reorderIndex, 1)

    newArr.splice(targetIndex, 0, movedElement)

    return newArr
}
